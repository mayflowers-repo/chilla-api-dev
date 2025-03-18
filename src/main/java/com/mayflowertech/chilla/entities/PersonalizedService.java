package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Proxy;

@Proxy(lazy = false)
@Entity
@Table(name = "services")
public class PersonalizedService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 30)
	private String name;

	@Column(name = "description", nullable = false, length = 500)
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "value_added", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean valueAdded;

	public boolean isValueAdded() {
		return valueAdded;
	}

	public void setValueAdded(boolean valueAdded) {
		this.valueAdded = valueAdded;
	}
	
	
	@Override
	public String toString() {
	    return "PersonalizedService{" +
	           "id=" + id +
	           ", name='" + name + '\'' +
	           '}';
	}

}
