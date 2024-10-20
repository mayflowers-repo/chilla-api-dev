package com.mayflowertech.chilla.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.User;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByRegisteredUser(User registeredUser);	
	Optional<Customer> findById(Long id);
}