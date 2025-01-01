package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Proxy(lazy = false)
@Entity
@Table(name = "emailotp")
public class EmailOtpRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false, length = 30)
	private String email;

	@Column(name = "otp", nullable = false, length = 10)
	private String otp;

	@Column(name = "createdAt", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "verifiedAt")
	private LocalDateTime verifiedAt;

	@Column(name = "status", nullable = true, length = 25)
	private String status;
	
	   @PrePersist
	    protected void onCreate() {
	        createdAt = LocalDateTime.now();
	    }


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getVerifiedAt() {
		return verifiedAt;
	}

	public void setVerifiedAt(LocalDateTime verifiedAt) {
		this.verifiedAt = verifiedAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isExpired(int validityDurationMinutes) {
		return createdAt.plusMinutes(validityDurationMinutes).isBefore(LocalDateTime.now());
	}

	
	@Override
	public String toString() {
		return "otp="+otp+"  email="+email;
	}
	
	@Column(name = "purpose", nullable = true, length = 30)
	private String purpose;

	public String getPurpose() {
		return purpose;
	}


	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
}
