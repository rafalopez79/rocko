package com.bzsoft.rocko.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bzsoft.rocko.api.DataContainerDTO;
import com.bzsoft.rocko.api.DataResultDTO;
import com.bzsoft.rocko.api.StringMessageDTO;
import com.bzsoft.rocko.config.AppConfiguration;

@RestController
@RequestMapping("/api/data")
public class DataController {

	private final AppConfiguration config;

	@Autowired
	public DataController(final AppConfiguration config) {
		this.config = config;
	}

	@GetMapping(path = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DataContainerDTO<DataResultDTO> find(@PathVariable("key") final String key) {

		return null;
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<StringMessageDTO> handleAllExceptions(final Exception ex) {
		final StringMessageDTO error = new StringMessageDTO(ex.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
