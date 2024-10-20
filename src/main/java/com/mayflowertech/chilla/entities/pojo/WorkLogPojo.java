package com.mayflowertech.chilla.entities.pojo;

import java.time.Duration;
import java.time.LocalDateTime;

import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Student;

public class WorkLogPojo {
	
    private Long logId;
	
    private Student student;

    private BookingRequest bookingRequest;
    
    private String workDescription;
    
    private LocalDateTime workStartTime;
    
    private LocalDateTime workEndTime;
    
    private Duration duration;
    
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getWorkDescription() {
		return workDescription;
	}
	public void setWorkDescription(String workDescription) {
		this.workDescription = workDescription;
	}
	public LocalDateTime getWorkStartTime() {
		return workStartTime;
	}
	public void setWorkStartTime(LocalDateTime workStartTime) {
		this.workStartTime = workStartTime;
	}
	public LocalDateTime getWorkEndTime() {
		return workEndTime;
	}
	public void setWorkEndTime(LocalDateTime workEndTime) {
		this.workEndTime = workEndTime;
	}
	public Duration getDuration() {
		return duration;
	}
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public BookingRequest getBookingRequest() {
		return bookingRequest;
	}
	public void setBookingRequest(BookingRequest bookingRequest) {
		this.bookingRequest = bookingRequest;
	}

	
	
}
