package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mayflowertech.chilla.enums.Gender;

@JsonFilter("BookingRequestFilter")
@Proxy(lazy = false)
@Entity
@Table(name = "booking_requests")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class BookingRequest extends BaseEntity implements Serializable {
	
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
	private Customer enrolledByCustomer;
		
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "booking_request_services", 
        joinColumns = @JoinColumn(name = "booking_request_id"), 
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )    
	private List<PersonalizedService> requestedServices;
	
    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_gender", nullable = true)
	private Gender preferredGender;
			
    
	@Column(name = "description", length = 50)
	String description;
	
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "booking_request_patients", 
        joinColumns = @JoinColumn(name = "booking_request_id"), 
        inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
	private List<Patient> requestedFor;

    @Column(name = "status", nullable = true, length = 25)
    private String status;
    
    
    
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}



	public List<PersonalizedService> getRequestedServices() {
		return requestedServices;
	}


	public void setRequestedServices(List<PersonalizedService> requestedServices) {
		this.requestedServices = requestedServices;
	}


	public Gender getPreferredGender() {
		return preferredGender;
	}


	public void setPreferredGender(Gender preferredGender) {
		this.preferredGender = preferredGender;
	}


	public List<Patient> getRequestedFor() {
		return requestedFor;
	}


	public void setRequestedFor(List<Patient> requestedFor) {
		this.requestedFor = requestedFor;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Customer getEnrolledByCustomer() {
		return enrolledByCustomer;
	}


	public void setEnrolledByCustomer(Customer enrolledByCustomer) {
		this.enrolledByCustomer = enrolledByCustomer;
	}
	
	
	
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "bookingrequest_students", 
        joinColumns = @JoinColumn(name = "booking_request_id"), 
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> assignedStudents;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = true)
    @JsonIgnore
    private Manager assignedByManager;

    public List<Student> getAssignedStudents() {
        return assignedStudents;
    }

    public void setAssignedStudents(List<Student> assignedStudents) {
        this.assignedStudents = assignedStudents;
    }

    public Manager getAssignedByManager() {
        return assignedByManager;
    }

    public void setAssignedByManager(Manager assignedByManager) {
        this.assignedByManager = assignedByManager;
    }


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

    
    
}
