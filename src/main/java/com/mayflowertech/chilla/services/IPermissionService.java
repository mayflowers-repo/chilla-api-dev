package com.mayflowertech.chilla.services;

import java.util.List;

import com.mayflowertech.chilla.entities.Permission;

public interface IPermissionService {
  List<Permission> getAllPermissions();
  boolean isExists(Permission permission);
  Permission createPermission(Permission permission);
}
