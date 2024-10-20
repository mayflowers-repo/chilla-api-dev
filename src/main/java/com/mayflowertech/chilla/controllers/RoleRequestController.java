package com.mayflowertech.chilla.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.RoleRequest;
import com.mayflowertech.chilla.entities.pojo.RoleRequestPojo;
import com.mayflowertech.chilla.services.IRoleRequestService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/rolerequest")
public class RoleRequestController {

 	   private static final Logger logger = LoggerFactory.getLogger(RoleRequestController.class);
	
	   @Autowired
	    private IRoleRequestService roleRequestService;

	    @ApiOperation(value = "Get all pending role requests")
	    @ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully retrieved pending role requests"),
	        @ApiResponse(code = 404, message = "No pending role requests found"),
	        @ApiResponse(code = 500, message = "An unexpected error occurred")
	    })
	    @GetMapping("/pending")
	    public ApiResult<List<RoleRequest>> getPendingRoleRequests() {
	        try {
	            List<RoleRequest> pendingRequests = roleRequestService.getPendingRoleRequests();
	            return new ApiResult<>(HttpStatus.OK.value(), "Pending role requests retrieved successfully", pendingRequests);

	        } catch (Exception e) {
	            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	        }
	    }
	    
	    
	    @ApiOperation(value = "Get all role requests")
	    @ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully retrieved all role requests"),
	        @ApiResponse(code = 404, message = "No role requests found"),
	        @ApiResponse(code = 500, message = "An unexpected error occurred")
	    })
	    @GetMapping("/all")
	    public ApiResult<List<RoleRequest>> getAllRoleRequests() {
	        try {
	            List<RoleRequest> allRequests = roleRequestService.getAllRoleRequests();
	            return new ApiResult<>(HttpStatus.OK.value(), "Role requests retrieved successfully", allRequests);
	        } catch (Exception e) {
	            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	        }
	    }
	    
	    
	    @ApiOperation(value = "Update Role Request Status")
	    @ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Role request status updated successfully"),
	        @ApiResponse(code = 404, message = "Role request not found"),
	        @ApiResponse(code = 500, message = "An unexpected error occurred")
	    })
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
