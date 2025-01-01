package com.mayflowertech.chilla.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.AuthUser;

public interface IAuthUserRepository  extends JpaRepository<AuthUser, Serializable> {
	AuthUser findByUsername(String username);

	AuthUser findByEmail(String email);

	AuthUser findByMobile(String mobile);

	AuthUser findById(UUID id);

	AuthUser findByUsernameAndActive(String username, boolean active);

	List<AuthUser> findByActiveOrderByUsernameAsc(boolean active);

	AuthUser findByUsernameAndPassword(String username, String password);

	AuthUser findByUsernameAndPasswordAndActive(String username, String password, boolean active);

	AuthUser findByEmailAndPassword(String email, String password);
}
