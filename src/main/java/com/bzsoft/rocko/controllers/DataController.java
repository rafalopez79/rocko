package com.bzsoft.rocko.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bzsoft.rocko.api.DataResultDTO;
import com.bzsoft.rocko.api.StringMessageDTO;
import com.bzsoft.rocko.config.AppConfiguration;
import com.bzsoft.rocko.mapper.DataMapper;
import com.bzsoft.rocko.model.DataResult;
import com.bzsoft.rocko.services.DataService;

@RestController
@RequestMapping("/api/data")
public class DataController {

	private final AppConfiguration config;
	private final DataService dataService;
	private final DataMapper dataMapper;

	@Autowired
	public DataController(final AppConfiguration config, final DataService dataService, final DataMapper dataMapper) {
		this.config = config;
		this.dataService = dataService;
		this.dataMapper = dataMapper;
	}

	@GetMapping(path = "/{sku}/{location}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DataResultDTO find(@PathVariable("sku") final String sku, @PathVariable("location") final int location) {
		final DataResult dr = dataService.find(sku, location);
		return dataMapper.adapt(dr);
	}

	@PostMapping(path = "/{sku}/{location}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public StringMessageDTO save(@PathVariable("sku") final String sku, @PathVariable("location") final int location,
			@RequestBody final DataResultDTO data) {
		final DataResult dr = dataMapper.adapt(data);
		dataService.save(sku, location, dr);
		return new StringMessageDTO("ok");
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<StringMessageDTO> handleAllExceptions(final Exception ex) {
		final StringMessageDTO error = new StringMessageDTO(ex.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<StringMessageDTO> handleException(final MethodArgumentNotValidException exception) {
		final String errorMsg = exception.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().orElse(exception.getMessage());
		final StringMessageDTO error = new StringMessageDTO(errorMsg);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
