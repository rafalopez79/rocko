package com.bzsoft.rocko.api;

import java.io.Serializable;
import java.util.List;

public class DataResultInfoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int reason;
	private List<Integer> includedLocations;
	private List<Integer> excludedLocations;

	public DataResultInfoDTO() {
		super();
	}

	public DataResultInfoDTO(final int reason, final List<Integer> includedLocations,
			final List<Integer> excludedLocations) {
		super();
		this.reason = reason;
		this.includedLocations = includedLocations;
		this.excludedLocations = excludedLocations;
	}

	public int getReason() {
		return reason;
	}

	public void setReason(final int reason) {
		this.reason = reason;
	}

	public List<Integer> getIncludedLocations() {
		return includedLocations;
	}

	public void setIncludedLocations(final List<Integer> includedLocations) {
		this.includedLocations = includedLocations;
	}

	public List<Integer> getExcludedLocations() {
		return excludedLocations;
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
		final DataResultInfoDTO other = (DataResultInfoDTO) obj;
		if (reason != other.reason) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DataResultInfoDTO [reason=").append(reason).append(", includedLocations=")
				.append(includedLocations).append(", excludedLocations=").append(excludedLocations).append("]");
		return builder.toString();
	}

}
