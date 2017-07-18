package com.ipfaffen.ovenbird.spring.rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

/**
 * @author Isaias Pfaffenseller
 */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	private final ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return errorHandler.hasError(response);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		throw new RestClientException(IOUtils.toString(response.getBody(), StandardCharsets.UTF_8));
	}
}