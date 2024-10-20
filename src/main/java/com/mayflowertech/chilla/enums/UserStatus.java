package com.mayflowertech.chilla.enums;


public enum UserStatus {
	  ACTIVE("Active"), INACTIVE("Inactive"), WAITING("Waiting");

	  private final String code;

	  private UserStatus(String code) {
	      this.code = code;
	  }

	  public String getCode() {
	      return this.code;
	  }
	}
