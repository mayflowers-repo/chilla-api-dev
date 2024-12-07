package com.mayflowertech.chilla.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Address;

public interface IAddressRepository extends JpaRepository<Address, UUID>{
	List<Address> findByUsers_Id(UUID userId);
	 List<Address> findByPatients_PatientId(Long patientId);
	
}
