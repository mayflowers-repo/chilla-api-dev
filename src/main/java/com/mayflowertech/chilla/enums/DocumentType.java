package com.mayflowertech.chilla.enums;


public enum DocumentType {

	DRIVING_LICENCE("Driving Licence"),
    PASSPORT("Passport"),
    RATION_CARD("Ration Card"),
    BANK_STATEMENT("Bank Statement");
    
    
	private final String code;

	private DocumentType(String code) {
	      this.code = code;
	  }

	public String getCode() {
		return this.code;
	}
}
