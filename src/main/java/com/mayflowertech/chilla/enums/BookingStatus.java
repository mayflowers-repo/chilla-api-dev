package com.mayflowertech.chilla.enums;

public enum BookingStatus {

	PENDING("Pending"), ASSIGNED("Assigned"), COMPLETED("Completed"), REJECTED("Rejected"), TERMINATED("Terminated");

	private final String code;

	private BookingStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
