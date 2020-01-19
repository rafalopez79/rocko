package com.bzsoft.rocko.model;

import java.util.List;

public class DataResultInfo {

	private int reason;
	private List<Integer> includedLocations;
	private List<Integer> excludedLocations;

	public DataResultInfo() {
		super();
	}

	public DataResultInfo(final int reason, final List<Integer> includedLocations,
			final List<Integer> excludedLocations) {
		super();
		this.reason = reason;
		this.includedLocations = includedLocations;
		this.excludedLocations = excludedLocations;
	}

	public int getReason() {
		return reason;
	}

	public List<Integer> getIncludedLocations() {
		return includedLocations;
	}

	public List<Integer> getExcludedLocations() {
		return excludedLocations;
	}

	public void setReason(final int reason) {
		this.reason = reason;
	}

	public void setIncludedLocations(final List<Integer> includedLocations) {
		this.includedLocations = includedLocations;
	}

	public void setExcludedLocations(final List<Integer> excludedLocations) {
		this.excludedLocations = excludedLocations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + reason;
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
		final DataResultInfo other = (DataResultInfo) obj;
		if (reason != other.reason) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DataResultInfo [reason=").append(reason).append(", includedLocations=")
				.append(includedLocations).append(", excludedLocations=").append(excludedLocations).append("]");
		return builder.toString();
	}

}
