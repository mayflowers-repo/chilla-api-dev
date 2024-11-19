package com.mayflowertech.chilla.repositories;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.EmailOtpRequest;

public interface IOtpRepository extends JpaRepository<EmailOtpRequest, Serializable>{
    // Find the most recent OTP request by email
    Optional<EmailOtpRequest> findTopByEmailOrderByCreatedAtDesc(String email);

    // Optional: Find a specific OTP request by email and OTP for direct verification
    Optional<EmailOtpRequest> findByEmailAndOtp(String email, String otp);

    // Optional: Delete OTP records older than a specific timestamp (for cleanup)
    void deleteByCreatedAtBefore(LocalDateTime timestamp);

    // Optional: Find all OTP requests with a specific status
    List<EmailOtpRequest> findByStatus(String status);
    
    // New: Find the most recent OTP request by email and status
    Optional<EmailOtpRequest> findTopByEmailAndStatusOrderByCreatedAtDesc(String email, String status);
}
