package com.mayflowertech.chilla.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Proxy;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Proxy(lazy = false)
@Entity
@Table(name = "address", uniqueConstraints=
@UniqueConstraint(columnNames={"map"}))
public class Address {

	@Id
	@Column(name = "id", length = 50,updatable = false, nullable = false)
	@JdbcTypeCode(SqlTypes.UUID)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	
	@Column(name = "first", length=100)
	protected String first;
	
	@Column(name = "second", length=100)
	protected String second;

	@Column(name = "pincode", length=10)
	protected String pincode;
	
	@Column(name = "landmark", length=100)
	protected String landmark;
	
	@Column(name = "map", length=500)
	protected String map;
	

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

	
	@Column(name = "district", length=30)
	protected String district;
	
	@Column(name = "state", length=20)
	protected String state;


	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@JsonIgnore
    @ManyToMany(mappedBy = "addresses", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
	@JsonIgnore
	@ManyToMany(mappedBy = "addresses", fetch = FetchType.EAGER)
    private Set<Patient> patients = new HashSet<>();

    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public void addPatient(Patient patient) {
        this.patients.add(patient);
        patient.getAddresses().add(this);
    }

    public void removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.getAddresses().remove(this);
    }

    
    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", first='" + first + '\'' +
                ", second='" + second + '\'' +
                ", pincode='" + pincode + '\'' +
                ", landmark='" + landmark + '\'' +
                '}';
    }

}
