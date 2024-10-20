package com.mayflowertech.chilla.entities.pojo;

import com.mayflowertech.chilla.entities.User;

public class ManagerPojo {
	private Long managerId;
	private User registeredUser;
	private String email;
	
	private String firstName;
	private String lastName;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public User getRegisteredUser() {
		return registeredUser;
	}
	public void setRegisteredUser(User registeredUser) {
		this.registeredUser = registeredUser;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
