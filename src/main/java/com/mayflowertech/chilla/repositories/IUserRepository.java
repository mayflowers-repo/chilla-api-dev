package com.mayflowertech.chilla.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.User;

public interface IUserRepository extends JpaRepository<User, Serializable> {
	User findByUsername(String username);

	User findByEmail(String email);

	User findByMobile(String mobile);

	User findById(UUID id);

	User findByUsernameAndActive(String username, boolean active);

	List<User> findByActiveOrderByUsernameAsc(boolean active);

	User findByUsernameAndPassword(String username, String password);

	User findByUsernameAndPasswordAndActive(String username, String password, boolean active);

	User findByEmailAndPassword(String email, String password);

}
