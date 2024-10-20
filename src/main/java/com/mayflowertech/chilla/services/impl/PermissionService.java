package com.mayflowertech.chilla.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.entities.Permission;
import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.repositories.IPermissionRepository;
import com.mayflowertech.chilla.services.IPermissionService;

@Service
public class PermissionService  implements IPermissionService{

  @Autowired
  private IPermissionRepository permissionRepository;

  
  @Override
  public List<Permission> getAllPermissions() {
    return permissionRepository.findAll();
  }

  @Override
  public boolean isExists(Permission permission) {
    Permission p = permissionRepository.findByName(permission.getName());
    if(p == null) {
      return false;
    }else {
      return true;
    }
  
  }

  @Override
  public Permission createPermission(Permission permission) {
    if(isExists(permission)) {
      return permissionRepository.findByName(permission.getName());
    }else {
      return permissionRepository.save(permission);      
    }
  }


}
