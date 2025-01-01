package com.mayflowertech.chilla.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.JwtAuthorizationProvider;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.entities.AuthUser;
import com.mayflowertech.chilla.enums.SystemRoles;
import com.mayflowertech.chilla.enums.UserStatus;
import com.mayflowertech.chilla.services.IAuthUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1")
public class AuthUserController {
	
	  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	  
	  @Autowired
	  private JwtAuthorizationProvider jwtTokenUtil;

	  @Autowired
	  private AuthenticationManager authenticationManager;
	  

	  @Autowired
	  private IAuthUserService userService;

	  @ApiOperation(value = "Validate user and get a token", response = User.class)
	  @ApiResponses(value = {@ApiResponse(code = 200, message = "Valid Token retrieved"),
	      @ApiResponse(code = 401, message = "Invalid Credentials"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
	  @RequestMapping(value = "/usermanagement/login", method = {RequestMethod.POST},
	      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<AuthUser> loginAndGetToken(@RequestBody AuthUser user) {
	      logger.info("-------- loginAndGetToken " + user);

	      try {
	          if (user != null) {

	              // Authenticate with username and password
	              final Authentication authentication = authenticationManager.authenticate(
	                  new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
	              );

	              user = userService.getUser(user.getUsername());

	              // Check if the user is active
	              if (!UserStatus.ACTIVE.getCode().equalsIgnoreCase(user.getStatus())) {
	                  logger.error("User is inactive");
	                  return new ApiResult<>(HttpStatus.FORBIDDEN.value(), "User is inactive", null);
	              }

	              // Set security context and generate token
	              SecurityContextHolder.getContext().setAuthentication(authentication);
	              final String token = jwtTokenUtil.generateToken(authentication);
	              user.setAuthtoken(token);

	              // Log roles and handle additional user role details
	              logger.info(user.getUsername() + "--------------" + user.getRoles());
	              List<String> assignedRoles = new ArrayList<>();
	              for (Role role : user.getRoles()) {
	                  assignedRoles.add(role.getRolename());

	                  // Assign Manager ID if role is Manager
	                  if (SystemRoles.MANAGER.getRoleCode().equals(role.getRolename())) {
	                      try {
	                          Long idForRole = userService.getManagerId(user);
	                          user.setManagerId(idForRole);
	                      } catch (CustomException e) {
	                          e.printStackTrace();
	                          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error assigning manager ID", null);
	                      }
	                  }
	                  // Assign Student ID if role is Student
	                  else if (SystemRoles.STUDENT.getRoleCode().equals(role.getRolename())) {
	                      try {
	                          Long idForRole = userService.getStudentId(user);
	                          user.setStudentId(idForRole);
	                      } catch (CustomException e) {
	                          e.printStackTrace();
	                          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error assigning student ID", null);
	                      }
	                  } else if (SystemRoles.CUSTOMER.getRoleCode().equals(role.getRolename())) {
	                	  try {
	                          Long idForRole = userService.getCustomerId(user);
	                          user.setCustomerId(idForRole);
	                      } catch (CustomException e) {
	                          e.printStackTrace();
	                          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error assigning customer ID", null);
	                      }
	                  }
	              }

	              // Set assigned roles
	              user.setAssignedRoles(assignedRoles);
	              jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "active", "firstName", "lastName", "photoUrl",
	            		  "authtoken", "assignedRoles", "status", "mobile", "studentId", "managerId", "customerId", "otpWaiting", "registered");
	              jacksonFilterConfig.printExistingFilters();
	              logger.info(user.getUsername() + " successfully logged in " + assignedRoles);
	              return new ApiResult<>(HttpStatus.OK.value(), "Login successful", user);
	          } else {
	              return new ApiResult<>(HttpStatus.BAD_REQUEST.value(), "Invalid user details", null);
	          }
	      } catch (AuthenticationException e) {
	          logger.error("Authentication failed for user: " + user.getUsername(), e);
	          return new ApiResult<>(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password", null);
	      } catch (Exception e) {
	          logger.error("Unexpected error during login", e);
	          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	      }finally {
	    	  jacksonFilterConfig.clearFilters();
	      }
	  }


	  
	  
}
