package com.mayflowertech.chilla.entities.pojo;

import java.time.LocalDateTime;

import com.mayflowertech.chilla.entities.User;

public class RoleRequestPojo {
    private String status;
    private String comment;
    private User requestedByUser;
    private User approvedBy;
    private Long id;
    private LocalDateTime requestDate;
    
    private String requestedRole;
    
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public User getRequestedByUser() {
		return requestedByUser;
	}
	public void setRequestedByUser(User requestedByUser) {
		this.requestedByUser = requestedByUser;
	}

	public User getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}
	public String getRequestedRole() {
		return requestedRole;
	}
	public void setRequestedRole(String requestedRole) {
		this.requestedRole = requestedRole;
	}
    
    
}
