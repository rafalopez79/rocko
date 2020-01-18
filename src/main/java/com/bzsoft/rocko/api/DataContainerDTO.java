package com.bzsoft.rocko.api;

import java.io.Serializable;
import java.util.List;

public class DataContainerDTO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<T> data;

	public DataContainerDTO() {
		//
	}

	public DataContainerDTO(final List<T> data) {
		this.data = data;
	}

	public static final <T> DataContainerDTO<T> of(final List<T> list) {
		return new DataContainerDTO<T>(list);
	}

	public List<T> getData() {
		return data;
	}

	public void setData(final List<T> data) {
		this.data = data;
	}

}
