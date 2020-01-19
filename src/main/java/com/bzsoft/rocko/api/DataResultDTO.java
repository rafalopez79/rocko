package com.bzsoft.rocko.api;

import java.io.Serializable;
import java.util.List;

public class DataResultDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sku;
	private int location;
	private List<DataResultInfoDTO> info;

	public DataResultDTO() {
		super();
	}

	public DataResultDTO(final String sku, final int location, final List<DataResultInfoDTO> info) {
		this.sku = sku;
		this.location = location;
		this.info = info;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(final String sku) {
		this.sku = sku;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(final int location) {
		this.location = location;
	}

	public List<DataResultInfoDTO> getInfo() {
		return info;
	}

	public void setInfo(final List<DataResultInfoDTO> info) {
		this.info = info;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + location;
		result = (prime * result) + ((sku == null) ? 0 : sku.hashCode());
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
		final DataResultDTO other = (DataResultDTO) obj;
		if (location != other.location) {
			return false;
		}
		if (sku == null) {
			if (other.sku != null) {
				return false;
			}
		} else if (!sku.equals(other.sku)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DataResultDTO [sku=").append(sku).append(", location=").append(location).append(", info=")
				.append(info).append("]");
		return builder.toString();
	}

}
