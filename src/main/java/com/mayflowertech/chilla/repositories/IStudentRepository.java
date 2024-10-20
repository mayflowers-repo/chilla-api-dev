package com.mayflowertech.chilla.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;

public interface IStudentRepository extends JpaRepository<Student, Long>{
	Optional<Student> findByRegisteredUser(User registeredUser);
}
