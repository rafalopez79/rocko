package com.bzsoft.rocko.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bzsoft.rocko.api.StringMessageDTO;
import com.bzsoft.rocko.config.AppConfiguration;
import com.bzsoft.rocko.services.ShutdownService;

@RestController
public class AliveController {

	private final AppConfiguration config;
	private final ShutdownService shutdownService;

	@Autowired
	public AliveController(final AppConfiguration config, final ShutdownService shutdownService) {
		this.config = config;
		this.shutdownService = shutdownService;
	}

	@GetMapping(path = "/alive", produces = MediaType.APPLICATION_JSON_VALUE)
	public StringMessageDTO alive() {
		return new StringMessageDTO(config.getAliveMessage());
	}

	@GetMapping(path = "/shutdown", produces = MediaType.APPLICATION_JSON_VALUE)
	public StringMessageDTO shutdown() {
		shutdownService.shutDown();
		return new StringMessageDTO("Shuting down ...");
	}
}
