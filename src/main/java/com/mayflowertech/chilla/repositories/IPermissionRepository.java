package com.mayflowertech.chilla.repositories;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Permission;

public interface IPermissionRepository  extends JpaRepository<Permission,Serializable>{
  Permission findById(UUID id);
  Permission findByName(String name);
  
}
