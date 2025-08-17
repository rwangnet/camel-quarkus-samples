package com.example.hotelscheduler.infrastructure.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GlobalExceptionHandler extends RouteBuilder {
	@Override
	public void configure() {
		onException(IllegalArgumentException.class)
			.handled(true)
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
			.setBody(simple("${exception.message}"));
	}
}