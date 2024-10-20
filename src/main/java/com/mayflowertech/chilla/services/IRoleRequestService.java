package com.mayflowertech.chilla.services;

import java.util.List;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.RoleRequest;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.RoleRequestPojo;

public interface IRoleRequestService {
	public List<RoleRequest> getPendingRoleRequests();
	public List<RoleRequest> getAllRoleRequests();
	public RoleRequest updateRoleRequestStatus(Long requestId, String status, String comment) throws CustomException;
}
