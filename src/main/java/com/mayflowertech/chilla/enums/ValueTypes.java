package com.mayflowertech.chilla.enums;

public enum ValueTypes {
  BOOLEAN("BOOLEAN"), STRING("STRING"),INTEGER("INTEGER"),DECIMAL("DECIMAL"),DATE("DATE"), DATETIME("DATETIME");

  private final String code;

  private ValueTypes(String code) {
      this.code = code;
  }

  public String getCode() {
      return this.code;
  }
}
