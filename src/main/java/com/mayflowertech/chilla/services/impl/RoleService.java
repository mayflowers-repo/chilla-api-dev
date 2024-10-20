package com.mayflowertech.chilla.services.impl;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.repositories.IRoleRepository;
import com.mayflowertech.chilla.services.IRoleService;

@Service
public class RoleService implements  IRoleService{

	@Autowired
	private IRoleRepository roleRepository;
	
	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role getRoleByName(String rolename) {
		return roleRepository.findByRolename(rolename);
	}

	@Override
	public boolean isExists(String rolename) {
		Role role = roleRepository.findByRolename(rolename);
		if(role == null)
			return false;
		return true;
	}

	@Override
	public Role createRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role getById(String id) {
		return roleRepository.findByIdAndActive(UUID.fromString(id), true);
	}

	@Override
	public boolean isExists(Role role) {
		List<Role> roles = roleRepository.findByRolecategory(role.getRolecategory());
		if(roles == null)
			return false;
		if(roles.size() <= 0)
			return false;
		return true;
	}

	@Override
	public List<Role> getRoleByCategory(String rolecategory) {
		// TODO Auto-generated method stub
		return null;
	}

  @Override
  public  Set<Role> getRolesForUser(User user) {
    return roleRepository.findByUsers(user);
  }

}
