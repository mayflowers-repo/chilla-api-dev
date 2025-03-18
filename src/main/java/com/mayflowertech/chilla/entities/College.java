package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Proxy;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Proxy(lazy = false)
@Entity
@Table(name = "colleges")
public class College  extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", length = 50,updatable = false, nullable = false)
	@JdbcTypeCode(SqlTypes.UUID)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
