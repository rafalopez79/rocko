package com.bzsoft.rocko;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bzsoft.rocko.api.DataResultDTO;
import com.bzsoft.rocko.api.DataResultInfoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
class RockoApplicationTests {

	private final MockMvc mvc;
	private final Logger logger;
	private final ObjectMapper objectMapper;

	@Autowired
	public RockoApplicationTests(final MockMvc mvc, final Logger logger, final ObjectMapper objectMapper) {
		this.mvc = mvc;
		this.logger = logger;
		this.objectMapper = objectMapper;
	}

	@Test
	public void processTest() throws UnsupportedEncodingException, Exception {
		final String sku = "1212";
		final int location = 2;
		final List<DataResultInfoDTO> info = Arrays.asList(new DataResultInfoDTO(4, null, null),
				new DataResultInfoDTO(5, null, null));
		final DataResultDTO data = new DataResultDTO(sku, location, info);
		{
			final String response = mvc
					.perform(post("/api/data/{sku}/{loc}", sku, location).content(objectMapper.writeValueAsString(data))
							.contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().is(HttpStatus.OK.value())).andReturn().getResponse().getContentAsString();
			logger.info("response: " + response);
		}
		{
			final String response = mvc.perform(get("/api/data/{sku}/{loc}", sku, location))
					.andExpect(status().is(HttpStatus.OK.value())).andReturn().getResponse().getContentAsString();
			logger.info("response: " + response);
		}
	}

	@Test
	public void loadTest() throws UnsupportedEncodingException, Exception {
		int keys = 0;
		for (int location = 1; location < 10000; location++) {
			for (int sku = 1000000; sku <= 2000000; sku++) {
				keys++;
				final List<DataResultInfoDTO> info = Arrays.asList(new DataResultInfoDTO(4, null, null),
						new DataResultInfoDTO(5, null, null));
				final DataResultDTO data = new DataResultDTO(String.valueOf(sku), location, info);
				{
					final String response = mvc
							.perform(post("/api/data/{sku}/{loc}", sku, location)
									.content(objectMapper.writeValueAsString(data))
									.contentType(MediaType.APPLICATION_JSON_VALUE))
							.andExpect(status().is(HttpStatus.OK.value())).andReturn().getResponse()
							.getContentAsString();
				}
				if ((keys % 10000) == 0) {
					logger.info("inserted {} keys", keys);
				}
			}
		}
	}
}
