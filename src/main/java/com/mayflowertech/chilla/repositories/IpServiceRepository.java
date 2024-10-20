package com.mayflowertech.chilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.PersonalizedService;

public interface IpServiceRepository extends JpaRepository<PersonalizedService, Long>{
	 List<PersonalizedService> findByValueAdded(boolean valueAdded);
}
