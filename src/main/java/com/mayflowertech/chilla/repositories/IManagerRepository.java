package com.mayflowertech.chilla.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.User;

public interface IManagerRepository extends JpaRepository<Manager, Long> {
	Optional<Manager> findByRegisteredUser(User registeredUser);
}
