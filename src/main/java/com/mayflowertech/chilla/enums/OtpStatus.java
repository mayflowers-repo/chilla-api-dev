package com.mayflowertech.chilla.enums;

public enum OtpStatus {
	GENERATED("Generated"), SENT("Sent"), VERIFIED("Verified");

	private final String code;

	private OtpStatus(String code) {
	      this.code = code;
	  }

	public String getCode() {
		return this.code;
	}
}