package com.example.hotelscheduler.infrastructure.error;

public enum ErrorCode {
	VALIDATION_ERROR(400, "Validation error"),
	CONSTRAINT_VIOLATION(400, "Constraint violation"),
	RESOURCE_NOT_FOUND(404, "Resource not found"),
	CONFLICT(409, "Conflict"),
	UNAUTHORIZED(401, "Unauthorized"),
	FORBIDDEN(403, "Forbidden"),
	TIMEOUT(504, "Timeout"),
	INTERNAL_ERROR(500, "Internal server error");

	private final int httpStatus;
	private final String defaultMessage;

	ErrorCode(int httpStatus, String defaultMessage) {
		this.httpStatus = httpStatus;
		this.defaultMessage = defaultMessage;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}