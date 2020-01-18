package com.bzsoft.rocko.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CLFilter implements Filter {

	protected final static class BufferingOutputStream extends ServletOutputStream {

		private ByteArrayOutputStream baos;
		private final ServletOutputStream sos;
		private final int maxSize;
		private int size;
		private boolean buffering;
		private OutputStream os;

		protected BufferingOutputStream(final ServletOutputStream sos, final int maxSize) {
			baos = new ByteArrayOutputStream(maxSize);
			this.sos = sos;
			os = baos;
			this.maxSize = maxSize;
			size = 0;
			buffering = true;
		}

		private final boolean switchStream(final int nsize) {
			size += nsize;
			final boolean mustSwitch = buffering && (size > maxSize);
			if (mustSwitch) {
				buffering = false;
				os = sos;
				size = 0;
				return true;
			}
			return false;
		}

		@Override
		public void write(final int b) throws IOException {
			if (switchStream(1)) {
				baos.writeTo(os);
				baos = null;
			}
			os.write(b);
		}

		@Override
		public void write(final byte[] b) throws IOException {
			if (switchStream(b.length)) {
				baos.writeTo(os);
				baos = null;
			}
			os.write(b);
		}

		@Override
		public void write(final byte[] b, final int off, final int len) throws IOException {
			if (switchStream(len)) {
				baos.writeTo(os);
				baos = null;
			}
			os.write(b, off, len);
		}

		@Override
		public boolean isReady() {
			return buffering || sos.isReady();
		}

		@Override
		public void setWriteListener(final WriteListener listener) {
			sos.setWriteListener(listener);
		}

		public void dump() throws IOException {
			if (buffering) {
				baos.writeTo(sos);
			}
		}

		public boolean isBuffering() {
			return buffering;
		}

		public int getBufferLen() {
			return size;
		}
	}

	protected final static class BufferingHttpServletResponse extends HttpServletResponseWrapper {

		private static final String CONTENT_LENGTH = "Content-Length";

		private final HttpServletResponse httpResponse;
		private final int maxSize;

		private boolean writerAcquired;
		private boolean osAcquired;
		private PrintWriter writer;
		private ServletOutputStream outputStream;
		private boolean buffering;

		public BufferingHttpServletResponse(final HttpServletResponse response, final int maxSize) {
			super(response);
			httpResponse = response;
			this.maxSize = maxSize;
			osAcquired = false;
			writerAcquired = false;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			if (writerAcquired) {
				throw new IllegalStateException("Character stream already acquired.");
			}
			if (outputStream != null) {
				return outputStream;
			}
			if (hasContentLength()) {
				outputStream = super.getOutputStream();
			} else {
				outputStream = new BufferingOutputStream(super.getOutputStream(), maxSize);
				buffering = true;
			}
			osAcquired = true;
			return outputStream;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			if (osAcquired) {
				throw new IllegalStateException("Binary stream already acquired.");
			}
			if (writer != null) {
				return writer;
			}
			if (hasContentLength()) {
				writer = super.getWriter();
			} else {
				final ServletOutputStream os = new BufferingOutputStream(super.getOutputStream(), maxSize);
				outputStream = os;
				writer = new PrintWriter(new OutputStreamWriter(os, getCharacterEncoding()), false);
				buffering = true;
			}
			writerAcquired = true;
			return writer;
		}

		private boolean hasContentLength() {
			return containsHeader(CONTENT_LENGTH);
		}

		public void dump() throws IOException {
			if (buffering) {
				final BufferingOutputStream bos = (BufferingOutputStream) outputStream;
				if (bos.isBuffering()) {
					httpResponse.setContentLength(bos.getBufferLen());
					bos.dump();
				}
			}
		}
	}

	private final int maxSize;

	public CLFilter(final int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain)
			throws IOException, ServletException {
		final BufferingHttpServletResponse wrapped = new BufferingHttpServletResponse((HttpServletResponse) resp,
				maxSize);
		chain.doFilter(req, wrapped);
		if (wrapped.buffering) {
			wrapped.dump();
		}
	}

}
