package com.example.hotelscheduler.infrastructure.camel;

import com.example.hotelscheduler.application.exception.ResourceNotFoundException;
import com.example.hotelscheduler.infrastructure.error.ErrorCode;
import com.example.hotelscheduler.infrastructure.error.ErrorResponse;
import com.example.hotelscheduler.infrastructure.error.ErrorResponseFactory;
import jakarta.validation.ConstraintViolationException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
public class GlobalExceptionHandler extends RouteBuilder {
	@Override
	public void configure() {
		onException(ResourceNotFoundException.class)
			.handled(true)
			.process(exchange -> {
				Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				ErrorResponse body = ErrorResponseFactory.build(e, exchange, ErrorCode.RESOURCE_NOT_FOUND);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, body.status);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
				exchange.getMessage().setBody(body);
			});

		onException(IllegalArgumentException.class, ConstraintViolationException.class)
			.handled(true)
			.process(exchange -> {
				Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				ErrorResponse body = ErrorResponseFactory.build(e, exchange, ErrorCode.VALIDATION_ERROR);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, body.status);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
				exchange.getMessage().setBody(body);
			});

		onException(DataIntegrityViolationException.class)
			.handled(true)
			.process(exchange -> {
				Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				ErrorResponse body = ErrorResponseFactory.build(e, exchange, ErrorCode.CONFLICT);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, body.status);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
				exchange.getMessage().setBody(body);
			});

		onException(TimeoutException.class)
			.handled(true)
			.process(exchange -> {
				Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				ErrorResponse body = ErrorResponseFactory.build(e, exchange, ErrorCode.TIMEOUT);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, body.status);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
				exchange.getMessage().setBody(body);
			});

		// Fallback catch-all
		onException(Exception.class)
			.handled(true)
			.process(exchange -> {
				Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
				ErrorResponse body = ErrorResponseFactory.build(e, exchange, ErrorCode.INTERNAL_ERROR);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, body.status);
				exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
				exchange.getMessage().setBody(body);
			});
	}
}