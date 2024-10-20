package com.mayflowertech.chilla.entities.pojo;

import java.util.List;

public class BookingRequestAssignPojo {
    private Long managerId;
    private Long bookingRequestId;
    private List<Long> studentIds;
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public Long getBookingRequestId() {
		return bookingRequestId;
	}
	public void setBookingRequestId(Long bookingRequestId) {
		this.bookingRequestId = bookingRequestId;
	}
	public List<Long> getStudentIds() {
		return studentIds;
	}
	public void setStudentIds(List<Long> studentIds) {
		this.studentIds = studentIds;
	}
    
    
}
