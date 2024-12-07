package com.mayflowertech.chilla.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("CustomerFilter")
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id") 
    private Long customerId;

	@Column(name = "job",length = 50, nullable = true)
	private String job;

	@Column(name = "city",length = 50, nullable = true)
	private String city;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = true)
    private Country country;

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
	
	
	
}
