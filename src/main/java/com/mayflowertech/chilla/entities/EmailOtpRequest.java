package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Proxy(lazy = false)
@Entity
@Table(name = "emailotp")
public class EmailOtpRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", length = 50,updatable = false, nullable = false)
	@org.hibernate.annotations.Type(type = "pg-uuid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
    @Column(name = "email", nullable = false, length = 150)
    private String email;
    
	@ManyToOne
	@JoinColumn(name = "user_id") 
	private User requestedUser;
	
    @Column(name = "otp", nullable = false, length = 25)
    private String otp;

    @Column(name = "status", nullable = true, length = 25)
    private String status;

}
