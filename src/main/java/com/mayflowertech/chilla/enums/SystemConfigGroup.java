package com.mayflowertech.chilla.enums;

public enum SystemConfigGroup {
  EMAIL("EMAIL"), SMS("SMS"),APPLICATION("APPLICATION"), DATATYPES("DATATYPES"), REPORTS("REPORTS"),REJECTMODES("REJECTMODES"),
  DISPLAYTYPES("DISPLAYTYPES"), CUSTOMER("CUSTOMER");

  private final String code;

  private SystemConfigGroup(String code) {
      this.code = code;
  }

  public String getCode() {
      return this.code;
  }
}
