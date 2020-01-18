package com.bzsoft.rocko.services.impl;

import org.rocksdb.RocksDB;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bzsoft.rocko.services.DataService;

@Service
public class DataServiceImpl implements DataService {

	private final RocksDB db;
	private final Logger logger;

	@Autowired
	public DataServiceImpl(final RocksDB db, final Logger logger) {
		this.db = db;
		this.logger = logger;
	}

	@Override
	public void find() {

	}
}
