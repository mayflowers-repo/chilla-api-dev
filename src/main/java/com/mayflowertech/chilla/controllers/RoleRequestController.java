package com.mayflowertech.chilla.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.JacksonFilterConfig;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.RoleRequest;
import com.mayflowertech.chilla.entities.pojo.RoleRequestPojo;
import com.mayflowertech.chilla.services.IRoleRequestService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/rolerequest")
public class RoleRequestController {

    private static final Logger logger = LoggerFactory.getLogger(RoleRequestController.class);

    @Autowired
    private IRoleRequestService roleRequestService;

    @Autowired
    private JacksonFilterConfig jacksonFilterConfig;
    
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN"})
    @GetMapping("/pending")
    public ApiResult<List<RoleRequest>> getPendingRoleRequests() {
        try {
            List<RoleRequest> pendingRequests = roleRequestService.getPendingRoleRequests();
            jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
            jacksonFilterConfig.applyFilters("RoleRequestFilter", "id", "requestedByUser", "requestedRole", "status", "approvedBy", "comments");
            return new ApiResult<>(HttpStatus.OK.value(), "Pending role requests retrieved successfully", pendingRequests);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        } finally {
            jacksonFilterConfig.clearFilters();
        }
    }
    
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN"})
    @GetMapping("/all")
    public ApiResult<List<RoleRequest>> getAllRoleRequests() {
        try {
            List<RoleRequest> allRequests = roleRequestService.getAllRoleRequests();
            jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
            jacksonFilterConfig.applyFilters("RoleRequestFilter", "id", "requestedByUser", "requestedRole", "status", "approvedBy", "comments");
            return new ApiResult<>(HttpStatus.OK.value(), "Role requests retrieved successfully", allRequests);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        } finally {
            jacksonFilterConfig.clearFilters();
        }
    }
    
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN"})
    @RequestMapping(value = "/update-status", method = RequestMethod.PUT)
    public ApiResult<RoleRequest> updateRoleRequestStatus(
            @RequestBody RoleRequestPojo roleRequestPojo) {
        try {
            logger.info("Updating role request status for requestId " + roleRequestPojo.getId());
            RoleRequest updatedRequest = roleRequestService.updateRoleRequestStatus(
                roleRequestPojo.getId(),
                roleRequestPojo.getStatus(),
                roleRequestPojo.getComment()
            );
            return new ApiResult<>(HttpStatus.OK.value(), "Role request status updated successfully", updatedRequest);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        }
    }
}
