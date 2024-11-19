package com.mayflowertech.chilla.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Address;

public interface IAddressRepository extends JpaRepository<Address, UUID>{
	Optional<Address> findById(UUID id);
}
