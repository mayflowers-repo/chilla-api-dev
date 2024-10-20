package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdOn", "updatedOn"},
        allowGetters = true
)
public class BaseEntity implements Serializable {
	
    @JsonIgnore
    @CreationTimestamp	
	@Column(name = "createdOn", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdOn;
	
	@JsonIgnore
	@UpdateTimestamp
	@Column(name = "updatedOn", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date updatedOn;
	
	@JsonIgnore
	@Column(name = "deletedOn")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date deletedOn;
	
	@Column(name = "active")
	protected boolean active = true;
	

	
	@JsonIgnore
	@Column(name = "deleted")
	protected boolean deleted = false;
	
	@JsonIgnore
	@Column(name = "createdBy")
	protected String createdBy;

	@JsonIgnore
	@Column(name = "modifiedBy")
	protected String modifiedBy;

	@JsonIgnore
	@Column(name = "deletedBy")
	protected String deletedBy;
	
	@Column(name = "reasonForDeactivate", length=500)
	protected String reasonForDeactivate;
	
	@JsonIgnore
	@Column(name = "reasonForDelete", length=500)
	protected String reasonForDelete;

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public String getReasonForDeactivate() {
		return reasonForDeactivate;
	}

	public void setReasonForDeactivate(String reasonForDeactivate) {
		this.reasonForDeactivate = reasonForDeactivate;
	}

	public String getReasonForDelete() {
		return reasonForDelete;
	}

	public void setReasonForDelete(String reasonForDelete) {
		this.reasonForDelete = reasonForDelete;
	}


}
