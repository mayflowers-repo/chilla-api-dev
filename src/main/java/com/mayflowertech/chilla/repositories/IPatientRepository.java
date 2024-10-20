package com.mayflowertech.chilla.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Patient;

public interface IPatientRepository extends JpaRepository<Patient, Long> {
	Optional<Patient> findByPatientId(Long id);
	List<Patient> findByEnrolledByCustomerId(Long customerId);
}
