package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Proxy(lazy = false)
@Entity
@Table(name = "work_log")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WorkLog implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", updatable = false, nullable = false)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;



    @Column(name = "work_description", nullable = false, length = 1000)
    private String workDescription;

    @Column(name = "work_start_time", nullable = false)
    private LocalDateTime workStartTime;

    @Column(name = "work_end_time")
    private LocalDateTime workEndTime;

    // This field is computed dynamically based on the difference between workStartTime and workEndTime
    @Transient
    private Duration duration;

    // Getters and Setters

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }


    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public LocalDateTime getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(LocalDateTime workStartTime) {
        this.workStartTime = workStartTime;
    }

    public LocalDateTime getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(LocalDateTime workEndTime) {
        this.workEndTime = workEndTime;
        // Calculate duration when the end time is set and both times are not null
        if (this.workStartTime != null && this.workEndTime != null) {
            this.duration = Duration.between(this.workStartTime, this.workEndTime);
        }
    }

    public Duration getDuration() {
        return duration;
    }

    // Optionally, you can create a method to return the duration in a human-readable format
    public String getFormattedDuration() {
        if (duration == null) {
            return "N/A";
        }
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%d hours %d minutes", hours, minutes);
    }

    // Method to check if the work has been completed (i.e., workEndTime is set)
    public boolean isWorkCompleted() {
        return this.workEndTime != null;
    }
    
    @JsonIgnore
    @CreationTimestamp	
	@Column(name = "createdOn", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdOn;
    
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "booking_request_id")
	private BookingRequest bookingRequest;

	public BookingRequest getBookingRequest() {
		return bookingRequest;
	}

	public void setBookingRequest(BookingRequest bookingRequest) {
		this.bookingRequest = bookingRequest;
	}
	
}
