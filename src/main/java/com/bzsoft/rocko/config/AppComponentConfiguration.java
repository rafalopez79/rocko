package com.bzsoft.rocko.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bzsoft.rocko.servlet.CLFilter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppComponentConfiguration {

	private final AppConfiguration appConfiguration;
	private final Logger logger;

	@Autowired
	public AppComponentConfiguration(final AppConfiguration appConfiguration, final Logger logger) {
		this.appConfiguration = appConfiguration;
		this.logger = logger;
	}

	@Bean
	public FilterRegistrationBean<CLFilter> contentLengthServletFilter() {
		final FilterRegistrationBean<CLFilter> fb = new FilterRegistrationBean<CLFilter>(
				new CLFilter(appConfiguration.getClBufferSize()));
		fb.setName("clFilter");
		fb.addUrlPatterns("/*");
		fb.setOrder(0);
		return fb;
	}

	@Bean
	public RocksDB rocksDB() throws Exception {
		RocksDB.loadLibrary();
		try (Options options = new Options()) {
			options.setCreateIfMissing(true);
			final File dbDir = new File(appConfiguration.getDatadir(), appConfiguration.getDbName());

			Files.createDirectories(dbDir.getParentFile().toPath());
			final RocksDB db = RocksDB.open(options, dbDir.getAbsolutePath());
			logger.info("RocksDB initialized and ready to use");
			return db;
		} catch (IOException | RocksDBException e) {
			logger.error(
					"Error initializng RocksDB, check configurations and permissions, exception: {}, message: {}, stackTrace: {}",
					e.getCause(), e.getMessage(), e.getStackTrace());
			throw e;
		}
	}

	@Bean
	public ObjectMapper objectMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

}
