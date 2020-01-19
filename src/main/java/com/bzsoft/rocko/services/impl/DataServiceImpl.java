package com.bzsoft.rocko.services.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.rocksdb.RocksDBException;
import org.rocksdb.TransactionDB;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bzsoft.rocko.model.DataResult;
import com.bzsoft.rocko.model.DataResultInfo;
import com.bzsoft.rocko.services.DataService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DataServiceImpl implements DataService {

	private final TransactionDB db;
	private final Logger logger;
	private final ObjectMapper objectMapper;

	@Autowired
	public DataServiceImpl(final TransactionDB db, final Logger logger, final ObjectMapper objectMapper) {
		this.db = db;
		this.logger = logger;
		this.objectMapper = objectMapper;
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
				list = readData(objectMapper, value);
			}
			return new DataResult(sku, location, list);
		} catch (final RocksDBException | IOException e) {
			logger.error("Error accessing db", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void save(final String sku, final int location, final DataResult data) {
		try {
			final byte[] key = getKey(sku, location);
			final byte[] value = writeData(objectMapper, data.getInfo());
			db.put(key, value);
		} catch (final RocksDBException | JsonProcessingException e) {
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

	private static final byte[] writeData(final ObjectMapper mapper, final List<DataResultInfo> list)
			throws JsonProcessingException {
		if ((list == null) || list.isEmpty()) {
			return new byte[0];
		}
		return mapper.writeValueAsBytes(list);
	}

	private static final List<DataResultInfo> readData(final ObjectMapper mapper, final byte[] bytes)
			throws JsonParseException, JsonMappingException, IOException {
		if ((bytes == null) || (bytes.length == 0)) {
			return Collections.emptyList();
		}
		final JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, DataResultInfo.class);
		return mapper.readValue(bytes, type);
	}
}
