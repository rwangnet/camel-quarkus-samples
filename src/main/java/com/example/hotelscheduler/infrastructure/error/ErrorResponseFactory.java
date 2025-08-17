package com.example.hotelscheduler.infrastructure.error;

import org.apache.camel.Exchange;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public final class ErrorResponseFactory {
	private ErrorResponseFactory() {}

	public static ErrorResponse build(Exception ex, Exchange exchange, ErrorCode code) {
		ErrorResponse r = new ErrorResponse();
		r.status = code.getHttpStatus();
		r.code = code.name();
		r.error = code.getDefaultMessage();
		r.message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : code.getDefaultMessage();
		r.timestamp = OffsetDateTime.now();
		r.path = exchange.getMessage().getHeader(Exchange.HTTP_PATH, String.class);
		if (r.path == null) {
			r.path = exchange.getMessage().getHeader(Exchange.HTTP_URI, String.class);
		}
		r.method = exchange.getMessage().getHeader(Exchange.HTTP_METHOD, String.class);
		r.exchangeId = exchange.getExchangeId();
		r.routeId = exchange.getFromRouteId();
		r.requestId = exchange.getMessage().getHeader("X-Request-Id", String.class);
		Map<String, Object> details = new HashMap<>();
		details.put("exceptionClass", ex != null ? ex.getClass().getName() : null);
		r.details = details;
		return r;
	}
}