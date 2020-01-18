package com.bzsoft.rocko.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.bzsoft.rocko.naming.AppNaming;

@Component
@ConfigurationProperties(AppNaming.APP_PREFIX)
public class AppConfiguration {

	private String datadir;
	private String dbName;

	private String aliveMessage;

	private int clBufferSize;

	public AppConfiguration() {
		//
	}

	public String getDatadir() {
		return datadir;
	}

	public void setDatadir(final String datadir) {
		this.datadir = datadir;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(final String dbName) {
		this.dbName = dbName;
	}

	public String getAliveMessage() {
		return aliveMessage;
	}

	public void setAliveMessage(final String aliveMessage) {
		this.aliveMessage = aliveMessage;
	}

	public int getClBufferSize() {
		return clBufferSize;
	}

	public void setClBufferSize(final int clBufferSize) {
		this.clBufferSize = clBufferSize;
	}

}
