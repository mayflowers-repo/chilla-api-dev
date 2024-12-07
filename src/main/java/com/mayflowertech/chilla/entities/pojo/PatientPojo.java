package com.mayflowertech.chilla.entities.pojo;

import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.enums.Gender;
import com.mayflowertech.chilla.enums.PatientRelation;

public class PatientPojo {

	private Long patientId;
	private User registeredUser;
	private int age;
	private Gender gender;
	private String healthDescription;
	private String email;
	private String mobile;
	private String firstName;
	private String lastName;
	private Customer enrolledBy;
	private PatientRelation relationWithPatient;

	
	private AddressPojo address;
	
	public PatientPojo(Patient patient) {
		this.patientId = patient.getPatientId();
		this.firstName = patient.getFirstName();
		this.lastName = patient.getLastName();
		this.age = patient.getAge();
		this.gender = patient.getGender();
	}
	
	public PatientPojo() {
	}
	
	public Long getPatientId() {
		return patientId;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	public User getRegisteredUser() {
		return registeredUser;
	}
	public void setRegisteredUser(User registeredUser) {
		this.registeredUser = registeredUser;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Customer getEnrolledBy() {
		return enrolledBy;
	}
	public void setEnrolledBy(Customer enrolledBy) {
		this.enrolledBy = enrolledBy;
	}


	public String getHealthDescription() {
		return healthDescription;
	}

	public void setHealthDescription(String healthDescription) {
		this.healthDescription = healthDescription;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public AddressPojo getAddress() {
		return address;
	}

	public void setAddress(AddressPojo address) {
		this.address = address;
	}


	public PatientRelation getRelationWithPatient() {
		return relationWithPatient;
	}

	public void setRelationWithPatient(PatientRelation relationWithPatient) {
		this.relationWithPatient = relationWithPatient;
	}


	
	
}
