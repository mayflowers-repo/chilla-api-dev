package com.mayflowertech.chilla.enums;

public enum RoleRequestStatus {

	PENDING("PENDING"), APPROVED("APPROVED"),  REJECTED("REJECTED");

	private final String code;

	private RoleRequestStatus(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
