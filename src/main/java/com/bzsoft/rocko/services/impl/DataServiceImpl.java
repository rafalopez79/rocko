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
import com.google.flatbuffers.FlatBufferBuilder;

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
			final byte[] key = getKey(sku, location);
			final byte[] value = db.get(key);
			List<DataResultInfo> list;
			if (value == null) {
				list = Collections.emptyList();
			} else {
				list = readData(value);
			}
			return new DataResult(sku, location, list);
		} catch (final RocksDBException e) {
			logger.error("Error accessing db", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void save(final String sku, final int location, final DataResult data) {
		try {
			final byte[] key = getKey(sku, location);
			final byte[] value = writeData(data.getInfo());
			db.put(key, value);
		} catch (final RocksDBException e) {
			logger.error("Error accessing db", e);
			throw new IllegalStateException(e);
		}
	}

	private static final byte[] getKey(final String sku, final int location) {
		final StringBuilder sb = new StringBuilder(255);
		sb.append(sku);
		sb.append('@');
		sb.append(String.format("%07d", location));
		return sb.toString().getBytes();
	}

	private static final byte[] writeData(final List<DataResultInfo> list) {
		if ((list == null) || list.isEmpty()) {
			return new byte[0];
		}
		final int len = list.size();
		final FlatBufferBuilder builder = new FlatBufferBuilder(4096);
		FBData.startFBData(builder);
		FBData.startInfoVector(builder, len);
		for (int i = 0; i < len; i++) {
			final DataResultInfo di = list.get(i);
			FBDataInfo.startFBDataInfo(builder);
			FBDataInfo.addReason(builder, (short) di.getReason());
			{
				final List<Integer> il = di.getIncludedLocations();
				if ((il != null) && !il.isEmpty()) {
					FBDataInfo.createIncludedLocationsVector(builder,
							il.stream().mapToInt(num -> num.intValue()).toArray());
				}
			}
			{
				final List<Integer> il = di.getExcludedLocations();
				if ((il != null) && !il.isEmpty()) {
					FBDataInfo.createExcludedLocationsVector(builder,
							il.stream().mapToInt(num -> num.intValue()).toArray());
				}
			}

			FBDataInfo.endFBDataInfo(builder);
		}
		FBData.endFBData(builder);
		return builder.sizedByteArray();
	}

	private static final List<DataResultInfo> readData(final byte[] bytes) {
		if ((bytes == null) || (bytes.length == 0)) {
			return Collections.emptyList();
		}
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
