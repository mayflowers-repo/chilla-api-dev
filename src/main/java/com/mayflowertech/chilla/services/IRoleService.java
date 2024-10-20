package com.mayflowertech.chilla.services;

import java.util.List;
import java.util.Set;

import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.entities.User;


public interface IRoleService {
	List<Role> getAllRoles();
	Role getRoleByName(String rolename);
	List<Role> getRoleByCategory(String rolecategory);
	boolean isExists(Role role);
	boolean isExists(String rolename);
	Role createRole(Role role);
	Role getById(String id);
	Set<Role> getRolesForUser(User user);
}
