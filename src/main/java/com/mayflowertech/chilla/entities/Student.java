package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mayflowertech.chilla.enums.Gender;

@JsonFilter("StudentFilter")
@Entity
@Table(name = "students")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student  extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id") 
    private Long studentId;
    
	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}


	@Column(name = "age")
	private int age;

	@Column(name = "course")
	private String course;

	@Column(name = "completion_year")
	private String completionYear;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "college_id")
	private College college;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getCompletionYear() {
		return completionYear;
	}

	public void setCompletionYear(String completionYear) {
		this.completionYear = completionYear;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
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
	
	
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_offer_services", 
        joinColumns = @JoinColumn(name = "student_id"), 
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )    
	private List<PersonalizedService> offeredServices;

	public List<PersonalizedService> getOfferedServices() {
		return offeredServices;
	}

	public void setOfferedServices(List<PersonalizedService> offeredServices) {
		this.offeredServices = offeredServices;
	}
	
    
    @ManyToMany(mappedBy = "assignedStudents", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookingRequest> assignedBookingRequests;

    public List<BookingRequest> getAssignedBookingRequests() {
        return assignedBookingRequests;
    }

    public void setAssignedBookingRequests(List<BookingRequest> assignedBookingRequests) {
        this.assignedBookingRequests = assignedBookingRequests;
    }
	
    

}
