package com.bzsoft.rocko.services.impl;

import org.rocksdb.RocksDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.bzsoft.rocko.services.ShutdownService;

@Service
public class ShutdownServiceImpl implements ShutdownService {

	private final ApplicationContext appContext;
	private final RocksDB db;
	private final ThreadPoolTaskExecutor thPool;

	@Autowired
	public ShutdownServiceImpl(final ApplicationContext appContext, final RocksDB db,
			final ThreadPoolTaskExecutor thPool) {
		this.appContext = appContext;
		this.db = db;
		this.thPool = thPool;
	}

	@Override
	public void shutDown() {
		thPool.submit(() -> {
			db.close();
			SpringApplication.exit(appContext, () -> 100);
		});
	}

}
