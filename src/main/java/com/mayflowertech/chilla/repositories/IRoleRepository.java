package com.mayflowertech.chilla.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.entities.User;


public interface IRoleRepository  extends JpaRepository<Role,Serializable>{
	Role findByRolename(String rolename);
	Role findByIdAndActive(UUID id, boolean active);
	List<Role> findByRolecategory(String rolecategory);
	Set<Role> findByUsers(User user);
}
