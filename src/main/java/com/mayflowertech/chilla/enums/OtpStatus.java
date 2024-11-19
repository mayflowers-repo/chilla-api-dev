package com.mayflowertech.chilla.enums;

public enum OtpStatus {
	GENERATED("GENERATED"), SENT("SENT"), VERIFIED("VERIFIED"), FAILED("FAILED");

	private final String code;

	private OtpStatus(String code) {
	      this.code = code;
	  }

	public String getCode() {
		return this.code;
	}
}