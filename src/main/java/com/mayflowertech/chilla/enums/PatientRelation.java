package com.mayflowertech.chilla.enums;

/**
 * Relationship with patient
 */
public enum PatientRelation {
	  SON("Son"), DAUGHTER("Daugher"), FATHER("Father"), MOTHER("Mother"), HUSBAND("Husband"), WIFE("Wife"), OTHER("Other");

	  private final String code;

	  private PatientRelation(String code) {
	      this.code = code;
	  }

	  public String getCode() {
	      return this.code;
	  }
}
