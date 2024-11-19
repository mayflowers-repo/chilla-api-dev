package com.mayflowertech.chilla.entities.pojo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Student;

public class WorkLogPojo {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    public String getWorkStartTime() {
        return workStartTime != null ? workStartTime.format(formatter) : null;
    }
    
	public void setWorkStartTime(LocalDateTime workStartTime) {
		this.workStartTime = workStartTime;
	}
    public String getWorkEndTime() {
        return workEndTime != null ? workEndTime.format(formatter) : null;
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
