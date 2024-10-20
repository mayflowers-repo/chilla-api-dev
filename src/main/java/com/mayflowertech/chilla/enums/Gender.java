package com.mayflowertech.chilla.enums;


public enum Gender {
	  MALE("Male"), FEMALE("Female"), TRANSGENDER("Transgender");

	  private final String code;

	  private Gender(String code) {
	      this.code = code;
	  }

	  public String getCode() {
	      return this.code;
	  }
	}
