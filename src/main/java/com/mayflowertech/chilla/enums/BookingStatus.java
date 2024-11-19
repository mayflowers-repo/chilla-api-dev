package com.mayflowertech.chilla.enums;

public enum BookingStatus {

	PENDING("PENDING"), ASSIGNED("ASSIGNED"), COMPLETED("COMPLETED"), REJECTED("REJECTED"), TERMINATED("TERMINATED");

	private final String code;

	private BookingStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
