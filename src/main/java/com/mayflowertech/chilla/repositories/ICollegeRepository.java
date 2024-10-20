package com.mayflowertech.chilla.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.College;

public interface ICollegeRepository extends JpaRepository<College, UUID>{
	Optional<College> findById(UUID id);
}
