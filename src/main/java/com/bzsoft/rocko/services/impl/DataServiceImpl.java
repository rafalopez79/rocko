package com.bzsoft.rocko.services.impl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rocksdb.RocksDBException;
import org.rocksdb.TransactionDB;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bzsoft.rocko.model.DataResult;
import com.bzsoft.rocko.model.DataResultInfo;
import com.bzsoft.rocko.model.fbdata.FBData;
import com.bzsoft.rocko.model.fbdata.FBDataInfo;
import com.bzsoft.rocko.services.DataService;

@Service
public class DataServiceImpl implements DataService {

	private final TransactionDB db;
	private final Logger logger;

	@Autowired
	public DataServiceImpl(final TransactionDB db, final Logger logger) {
		this.db = db;
		this.logger = logger;
	}

	@Override
	public DataResult find(final String sku, final int location) {
		try {
			final byte[] key = sku.getBytes();
			final byte[] value = db.get(key);
			List<DataResultInfo> list;
			if (value == null) {
				list = Collections.emptyList();
			} else {
				list = parseData(value);
			}
			return new DataResult(sku, location, list);
		} catch (final RocksDBException e) {
			logger.error("Error accessing db", e);
			throw new IllegalStateException(e);
		}
	}

	private static final List<DataResultInfo> parseData(final byte[] bytes) {
		final ByteBuffer buf = ByteBuffer.wrap(bytes);
		// Get an accessor to the root object inside the buffer.
		final FBData fbData = FBData.getRootAsFBData(buf);
		final int count = fbData.infoLength();
		final List<DataResultInfo> outList = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			final FBDataInfo fbDataInfo = fbData.info(i);
			final int reason = fbDataInfo.reason();
			final List<Integer> includedLocations;
			{
				int ilc = fbDataInfo.includedLocationsLength();
				if (ilc > 0) {
					includedLocations = new ArrayList<Integer>(ilc);
					for (final int j = 0; j < ilc; ilc++) {
						final int loc = fbDataInfo.includedLocations(j);
						includedLocations.add(loc);
					}
				} else {
					includedLocations = null;
				}
			}
			final List<Integer> excludedLocations;
			{
				int ilc = fbDataInfo.excludedLocationsLength();
				if (ilc > 0) {
					excludedLocations = new ArrayList<Integer>(ilc);
					for (final int j = 0; j < ilc; ilc++) {
						final int loc = fbDataInfo.excludedLocations(j);
						excludedLocations.add(loc);
					}
				} else {
					excludedLocations = null;
				}
			}
			final DataResultInfo dri = new DataResultInfo(reason, includedLocations, excludedLocations);
			outList.add(dri);
		}
		return outList;
	}
}
