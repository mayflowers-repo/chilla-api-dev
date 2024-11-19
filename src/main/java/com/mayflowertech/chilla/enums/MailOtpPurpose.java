package com.mayflowertech.chilla.enums;

public enum MailOtpPurpose {
	  EMAIL_VERIFICATION("EMAIL_VERIFICATION"), PASSWORD_CHANGE("PASSWORD_CHANGE"), PAYMENT_STATUS("PAYMENT_STATUS");

	  private final String code;

	  private MailOtpPurpose(String code) {
	      this.code = code;
	  }

	  public String getCode() {
	      return this.code;
	  }
	}