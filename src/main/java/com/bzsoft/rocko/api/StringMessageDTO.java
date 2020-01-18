package com.bzsoft.rocko.api;

import java.io.Serializable;

public class StringMessageDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;

	public StringMessageDTO() {
		//
	}

	public StringMessageDTO(final String value) {
		message = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (message == null ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final StringMessageDTO other = (StringMessageDTO) obj;
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AliveDTO [value=").append(message).append("]");
		return builder.toString();
	}

}
