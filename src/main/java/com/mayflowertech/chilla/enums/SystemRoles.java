package com.mayflowertech.chilla.enums;

public enum SystemRoles {

	SYSTEMADMIN("SYSTEMADMIN"), ADMIN("ADMIN"), GUEST("GUEST"), CUSTOMER("CUSTOMER"), MANAGER("MANAGER"), CAREGIVER("CAREGIVER"), STUDENT("STUDENT");
	private final String roleCode;

	private SystemRoles(String code) {
		this.roleCode = code;
	}

	public String getRoleCode() {
		return this.roleCode;
	}
}
