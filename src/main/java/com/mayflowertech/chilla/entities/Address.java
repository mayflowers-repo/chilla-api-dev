package com.mayflowertech.chilla.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Proxy;

@Proxy(lazy = false)
@Entity
@Table(name = "address", uniqueConstraints=
@UniqueConstraint(columnNames={"map"}))
public class Address {

	@Id
	@Column(name = "id", length = 50,updatable = false, nullable = false)
	@org.hibernate.annotations.Type(type = "pg-uuid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	
	@Column(name = "first", length=500)
	protected String first;
	
	@Column(name = "second", length=500)
	protected String second;

	@Column(name = "pincode", length=500)
	protected String pincode;
	
	@Column(name = "landmark", length=500)
	protected String landmark;
	
	@Column(name = "map", length=500)
	protected String map;
	
    @OneToOne(mappedBy = "address")
    private User user;
	  

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
}
