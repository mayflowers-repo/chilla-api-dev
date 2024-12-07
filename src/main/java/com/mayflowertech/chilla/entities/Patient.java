package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mayflowertech.chilla.enums.Gender;
import com.mayflowertech.chilla.enums.PatientRelation;

@JsonFilter("PatientFilter")
@Entity
@Table(name = "patients")
public class Patient  extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patient_id") 
    private Long patientId;
    
	@Column(name = "age")
	private int age;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	
	@Column(name = "health_description",  nullable = true, length = 500)
	private String healthDescription;

	
	@Enumerated(EnumType.STRING)
	@Column(name = "relation")
	private PatientRelation relationWithPatient;
	
	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
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

	public String getHealthDescription() {
		return healthDescription;
	}

	public void setHealthDescription(String healthDescription) {
		this.healthDescription = healthDescription;
	}

	@ManyToOne
	@JoinColumn(name = "user_id") 
	private User registeredUser;

	public User getRegisteredUser() {
		return registeredUser;
	}

	public void setRegisteredUser(User registeredUser) {
		this.registeredUser = registeredUser;
	}
	
	
	@Column(name = "first_name",  nullable = true, length = 50)
	private String firstName;
	
	@Column(name = "last_name",  nullable = true, length = 50)
	private String lastName;



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
	

    @ManyToOne 
    @JoinColumn(name = "enrolled_by_customer", nullable = true) 
    private Customer enrolledBy;



	public Customer getEnrolledBy() {
		return enrolledBy;
	}

	public void setEnrolledBy(Customer enrolledBy) {
		this.enrolledBy = enrolledBy;
	}

	public PatientRelation getRelationWithPatient() {
		return relationWithPatient;
	}

	public void setRelationWithPatient(PatientRelation relationWithPatient) {
		this.relationWithPatient = relationWithPatient;
	}
    
    
	@Column(name = "email",  nullable = true, length = 50)
	private String email;
	
	
	@Column(name = "mobile",  nullable = true, length = 15)
	private String mobile;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
  
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "patient_address",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "address_id"))
	private Set<Address> addresses = new HashSet<>();

	public Set<Address> getAddresses() {
	    return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
	    this.addresses = addresses;
	}
	
	public void addAddress(Address address) {
	    this.addresses.add(address);
	    address.getPatients().add(this);
	}

	public void removeAddress(Address address) {
	    this.addresses.remove(address);
	    address.getPatients().remove(this);
	}

	@Override
	public String toString() {
	    return "Patient{" +
	            "patientId=" + patientId +
	            ", firstName='" + firstName + '\'' +
	            ", lastName='" + lastName + '\'' +
	            ", mobile='" + mobile + '\'' +
	            ", email='" + email + '\'' +
	            ", age=" + age +
	            ", gender='" + gender + '\'' +
	            ", enrolledBy='" + enrolledBy + '\'' +
	            '}';
	}

}
