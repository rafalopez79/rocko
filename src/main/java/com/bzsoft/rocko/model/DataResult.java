package com.bzsoft.rocko.model;

import java.util.List;

public class DataResult {

	private final String sku;
	private final int location;
	private final List<DataResultInfo> info;

	public DataResult(final String sku, final int location, final List<DataResultInfo> info) {
		this.sku = sku;
		this.location = location;
		this.info = info;
	}

	public String getSku() {
		return sku;
	}

	public int getLocation() {
		return location;
	}

	public List<DataResultInfo> getInfo() {
		return info;
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
		final DataResult other = (DataResult) obj;
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
		builder.append("DataResult [sku=").append(sku).append(", location=").append(location).append(", info=")
				.append(info).append("]");
		return builder.toString();
	}

}
