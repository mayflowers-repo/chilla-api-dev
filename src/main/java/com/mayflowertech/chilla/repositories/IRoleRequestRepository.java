package com.mayflowertech.chilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mayflowertech.chilla.entities.RoleRequest;

@Repository
public interface IRoleRequestRepository extends JpaRepository<RoleRequest, Long> {
    
    // Custom query to find role requests by status
    List<RoleRequest> findByStatus(String status);
    
    // Custom query to find all role requests by the user
    List<RoleRequest> findByRequestedByUserId(Long userId);
    
    // Custom query to find pending role requests
    List<RoleRequest> findByStatusOrderByRequestDateAsc(String status);
}