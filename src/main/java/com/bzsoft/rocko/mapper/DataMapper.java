package com.bzsoft.rocko.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.bzsoft.rocko.api.DataResultDTO;
import com.bzsoft.rocko.api.DataResultInfoDTO;
import com.bzsoft.rocko.model.DataResult;
import com.bzsoft.rocko.model.DataResultInfo;

@Mapper(componentModel = "spring")
public interface DataMapper {

	public DataResultInfoDTO adapt(DataResultInfo d);

	public DataResultDTO adapt(DataResult d);

	public List<DataResultInfo> adaptDataResultInfoDTO(List<DataResultInfoDTO> list);

	public default DataResultInfo adapt(final DataResultInfoDTO d) {
		if (d == null) {
			return null;
		}
		return new DataResultInfo(d.getReason(), d.getIncludedLocations(), d.getExcludedLocations());
	}

	public default DataResult adapt(final DataResultDTO d) {
		if (d == null) {
			return null;
		}
		return new DataResult(d.getSku(), d.getLocation(), adaptDataResultInfoDTO(d.getInfo()));
	}
}
