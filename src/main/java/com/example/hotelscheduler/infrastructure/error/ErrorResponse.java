package com.example.hotelscheduler.infrastructure.error;

import java.time.OffsetDateTime;
import java.util.Map;

public class ErrorResponse {
	public String code;
	public String error;
	public String message;
	public int status;
	public OffsetDateTime timestamp;
	public String path;
	public String method;
	public String exchangeId;
	public String routeId;
	public String requestId;
	public Map<String, Object> details;
}