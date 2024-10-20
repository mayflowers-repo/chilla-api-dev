package com.mayflowertech.chilla.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.EmailOtpRequest;

public interface IOtpRepository extends JpaRepository<EmailOtpRequest, Serializable>{

}
