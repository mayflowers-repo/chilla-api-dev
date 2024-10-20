package com.mayflowertech.chilla.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mayflowertech.chilla.GoogleTokenInfo;
import com.mayflowertech.chilla.config.JacksonFilterConfig;
import com.mayflowertech.chilla.config.JwtAuthorizationProvider;
import com.mayflowertech.chilla.config.custom.ApplicationConfigParams;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.AppConfig;
import com.mayflowertech.chilla.entities.CustomStatusMessage;
import com.mayflowertech.chilla.entities.Permission;
import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.UserProfile;
import com.mayflowertech.chilla.entities.pojo.ResetPasswordPojo;
import com.mayflowertech.chilla.enums.SystemConfigGroup;
import com.mayflowertech.chilla.enums.SystemRoles;
import com.mayflowertech.chilla.enums.UserStatus;
import com.mayflowertech.chilla.services.GoogleAuthService;
import com.mayflowertech.chilla.services.IRoleService;
import com.mayflowertech.chilla.services.IUserService;
import com.mayflowertech.chilla.services.impl.AppConfigService;
import com.mayflowertech.chilla.services.impl.UserProfileService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1")
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private GoogleAuthService googleAuthService;
  
  @Autowired
  private JwtAuthorizationProvider jwtTokenUtil;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  AppConfigService appConfigService;

  
  @Autowired
  ApplicationConfigParams applicationConfigParams;


  @Autowired
  private IUserService userService;

  @Autowired
  private UserProfileService userProfileService;

  @Autowired
  private IRoleService roleService;

  @Autowired
  private JacksonFilterConfig jacksonFilterConfig;

  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully retrieved list"),
      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
      @ApiResponse(code = 500, message = "Internal server error")
  })
  @RequestMapping(value = "/usermanagement/users", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResult<List<User>> getUsers(Model model) {
      try {
          List<User> users = userService.getAllUsers();
          logger.info("usermanagement GET users");
          
          jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email");
          jacksonFilterConfig.printExistingFilters();
          // Return success response with users list in ApiResult
          return new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved users", users);

      } catch (Exception ex) {
          logger.error("Error: An unexpected error occurred", ex);
          // Return error response for general server error in ApiResult
          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
      } finally {
    	  jacksonFilterConfig.clearFilters();
      }
  }

  


  @ApiOperation(value = "Validate user and get a token", response = User.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Valid Token retrieved"),
      @ApiResponse(code = 401, message = "Invalid Credentials"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/login", method = {RequestMethod.POST},
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResult<User> loginAndGetToken(@RequestBody User user) {
      logger.info("-------- loginAndGetToken " + user);

      try {
          if (user != null) {
              // Check for social login
              if (user.getSocialId() != null) {
                  user = userService.checkSocialUser(user);
                  return new ApiResult<>(HttpStatus.OK.value(), "Social login successful", user);
              }

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
                  }
              }

              // Set assigned roles
              user.setAssignedRoles(assignedRoles);
              jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "active", "firstName", "lastName", 
            		  "authtoken", "assignedRoles", "status", "mobile", "studentId", "managerId", "otpWaiting", "registered");
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



  


  @ApiOperation(value = "Add roles to a particular user")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Updated user role"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/{userid}/roles", method = {RequestMethod.POST},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> addUserRole(@PathVariable String userid,
      @RequestBody List<Role> roles) {

    User user = userService.getById(userid);
    if (user == null) {
      System.out.println("user is null");
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    if (user.getUsername().trim().equalsIgnoreCase("systemadmin")) {
      System.out.println("No Additional role to systemadmin");
      return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

    for(Role inputRole : roles) {
      Role role = roleService.getById(inputRole.getId().toString());
      if (role == null) {
        logger.error("Invalid role");
        return new ResponseEntity(HttpStatus.NOT_FOUND);
      }
      // user.setPassword(null);
      // user.getRoles().add(role);
      userService.addRoletoUser(user, role);
      
    }
    return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
  }

  /**
   * @param userId
   * @param inputRoles
   * @return
   */
  @ApiOperation(value = "Update roles for a user (remove and add)")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Update user roles"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/{userId}/roles", method = {RequestMethod.PUT},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> removeUserRoles(@PathVariable("userId") String userId,  @RequestBody List<Role> inputRoles) {
    User user = userService.getById(userId);
    Set<Role> currentRoles = user.getRoles();
    if (user == null) {
      logger.debug("User is null");
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    List<Role> validInputRoles =  new ArrayList<Role>();
    
    for(Role inputRole: inputRoles) {
      if(inputRole.getId() == null) {
        return new ResponseEntity("Missing ID field for role", HttpStatus.NOT_ACCEPTABLE);
      }
      Role role = roleService.getById(inputRole.getId().toString());
      if (role == null) {
        logger.error("Invalid role");
        return new ResponseEntity("Invalid role", HttpStatus.NOT_FOUND);
      }
      validInputRoles.add(role);
    }
    

    for(Role inputRole: validInputRoles) {
      if(! currentRoles.contains(inputRole)) {
        userService.addRoletoUser(user, inputRole);        
      }
    }
    
    currentRoles = roleService.getRolesForUser(user);
    Iterator<Role> iter = currentRoles.iterator();
    while (iter.hasNext()) {
      Role cRole = iter.next();
      if(! validInputRoles.contains(cRole)) {
        //iter.remove();
        userService.removeRolefromUser(user, cRole);         
      }
    }
    currentRoles = roleService.getRolesForUser(user);
    logger.debug("final roles for user "+user+"  "+currentRoles);
    return new ResponseEntity<User>(user, HttpStatus.OK);
  }
  
  

  @ApiOperation(value = "List roles for a user")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Listed user roles"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/{userId}/roles", method = {RequestMethod.GET},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Set<Role>> listUserRoles(@PathVariable("userId") String userId) {
    User user = userService.getById(userId);
    Set<Role> roles = user.getRoles();
    return new ResponseEntity<Set<Role>>(roles, HttpStatus.OK);
  }
  
  

  @ApiOperation(value = "List permissions for a user")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Listed user permissions"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/{userId}/permissions", method = {RequestMethod.GET},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Set<Permission>> listUserPermissions(@PathVariable("userId") String userId) {
    User user = userService.getById(userId);
    Set<Role> roles = user.getRoles();
    Set<Permission> permissions = new HashSet<Permission>();
    if(roles != null) {
      for(Role role : roles) {
        permissions.addAll(role.getPermissions());
      }
    }
    return new ResponseEntity<Set<Permission>>(permissions, HttpStatus.OK);
  }
  
  
  @ApiOperation(value = "List roles in the system")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Listed ALL user roles"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/allroles", method = {RequestMethod.GET},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Role>> listAllUserRoles() {
    User user = userService.getCurrentLoggedInUser();
    Role adminRole =  roleService.getRoleByName(SystemRoles.ADMIN.getRoleCode());
    Role systemAdminRole =  roleService.getRoleByName(SystemRoles.SYSTEMADMIN.getRoleCode());
    if(userService.hasRole(user, adminRole) || userService.hasRole(user, systemAdminRole)) {
      List<Role> roles = roleService.getAllRoles();
      Role role =  roleService.getRoleByName(SystemRoles.SYSTEMADMIN.getRoleCode());
      roles.remove(role);
      return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);      
    }else {
      return new ResponseEntity<List<Role>>(new ArrayList<>(), HttpStatus.FORBIDDEN);
    }
    
  }
  
  
  @ApiOperation(value = "Retrieve user details for an user", response = User.class)
  @ApiResponses(value = {@ApiResponse(code = 202, message = "retrieved user details"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/updateuser", method = {RequestMethod.PUT},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResult<User> updateUser(@RequestBody User user) {

	if(user.getUsername() == null || user.getUsername().isEmpty()) {
		if(user.getEmail() == null || user.getEmail().isEmpty()) {
			return new ApiResult<User>(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid email/username ", null);
			
		}
		user.setUsername(user.getEmail());
	}
	  
	if (! userService.isExist(user)) {
		return new ApiResult<>(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid user "+user.getUsername(), user);
	}
	User existingUser = userService.getUser(user.getUsername());
	logger.info("updating the user details :"+user);
	if(user.getAddress() != null) {
		logger.info("address :"+user.getAddress());
		existingUser.setAddress(user.getAddress());
	}
		
	if(user.getCity() != null) {
		logger.info("city :"+user.getCity());		
		existingUser.setCity(user.getCity());
	}
		
	if(user.getFirstName() != null) {
		logger.info("firstName :"+user.getFirstName());
		existingUser.setFirstName(user.getFirstName());
	}

	if(user.getLastName() != null) {
		logger.info("lastName :"+user.getLastName());
		existingUser.setLastName(user.getLastName());
	}
	if(user.getMobile() != null) {
		logger.info("mobile :"+user.getMobile());
		existingUser.setMobile(user.getMobile());
	}
	
	if(user.getPhotoUrl() != null) {
		logger.info("photoUrl :"+user.getPhotoUrl());
		existingUser.setPhotoUrl(user.getPhotoUrl());
	}


	try {
		user = userService.updateUser(existingUser, false);
	} catch (Exception e) {
		e.printStackTrace();
		 return new ApiResult<User>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while updating: "+e.getMessage(), user);
	}
    return new ApiResult<User>(HttpStatus.OK.value(), "Successully updated the user details", user);
  }

  
  @ApiOperation(value = "Retrieve user profile photo for an user", response = UserProfile.class)
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Retrieved user photo details"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/userprofile/{userId}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public ResponseEntity<UserProfile> getUserProfilePhoto(@PathVariable String userId) {
	  //String userId = "98501acc-3038-4977-842c-5c3e6611bea8";
	  User user = userService.getById(userId);
	    if (user == null) {
	      System.out.println("user is null");
	      return new ResponseEntity(new CustomStatusMessage("Invalid userId"),
	              HttpStatus.NOT_FOUND);
	    }
	    UserProfile userProfile =  userProfileService.getByUserId(userId);
	    if (userProfile == null) {
		      System.out.println("userprofile is null");
		      return new ResponseEntity(new CustomStatusMessage("Invalid userId for userProfile"),
		              HttpStatus.NOT_FOUND);
		    }

	    if (userProfile.getPhoto() == null) {
		      System.out.println("userprofile photo is null");
		      return new ResponseEntity(new CustomStatusMessage("No photo for userProfile"),
		              HttpStatus.NOT_FOUND);
		    }
	   System.out.println("returning photo "+userProfile.getPhoto()); 
//	    return ResponseEntity
//                .ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(userProfile.getPhoto());

	    return new ResponseEntity<UserProfile>(userProfile, HttpStatus.OK);
	    
  }

  @ApiOperation(value = "Add/update user profile photo for an user", response = UserProfile.class)
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Updated user details"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/users/{userid}/profile", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addUserProfilePhoto(@PathVariable String userid, @RequestParam("file") MultipartFile file) {

    User user = userService.getById(userid);
    if (user == null) {
      System.out.println("user is null");
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    UserProfile userprofile = userProfileService.getByUserId(userid);
    if (file != null) {
      try {
        userprofile.setPhoto(file.getBytes());
        userprofile.setPhotouri(file.getOriginalFilename());
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    userprofile.setUser(user);


    userprofile = userProfileService.add(userprofile);
    return new ResponseEntity(new CustomStatusMessage("Photo has been uploaded"),
            HttpStatus.OK);
  }

  @ApiOperation(value = "Activate a particular user")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Updated user details"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/users/{userid}/activate", method = {RequestMethod.POST},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> activateUser(@PathVariable String userid) {

    User user = userService.getById(userid);
    if (user == null) {
      System.out.println("user is null");
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    user.setActive(true);
    user.setReasonForDeactivate(null);
    user = userService.updateUser(user, false);
    return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
  }


  @ApiOperation(value = "Deactivate a particular user")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Updated user details"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/usermanagement/users/{userid}/deactivate",
      method = {RequestMethod.PUT}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> deactivateUser(@PathVariable String userid, @RequestBody User user) {

    User tmpUser = userService.getById(userid);
    if (tmpUser == null) {
      System.out.println("user is null");
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    if (tmpUser.getUsername().trim().equalsIgnoreCase("systemadmin")) {
      System.out.println("systemadmin cannot be deactivated");
      return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }
    if (user.getReasonForDeactivate() != null) {
      tmpUser.setReasonForDeactivate(user.getReasonForDeactivate());
    }
    tmpUser.setActive(false);
    user = userService.updateUser(tmpUser, false);
    return new ResponseEntity<User>(HttpStatus.ACCEPTED);
  }

  
  
  
  
  
  
  
  @ApiOperation(value = "Forgot Password for an user", response = User.class)
  @ApiResponses(value = {
          @ApiResponse(code = 202, message = "Successfully send email link to reset password"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
  @RequestMapping(value = "/usermanagement/users/{username}/forgotpassword", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> forgotUserPassword(@PathVariable String username) {
      User user = userService.getUser(username);
      if (user == null) {
          System.out.println("user is null");
          return new ResponseEntity(HttpStatus.NOT_FOUND);
      }


      System.out
              .println("ServletUriComponentsBuilder.fromCurrentContextPath() : "
                      + ServletUriComponentsBuilder.fromCurrentContextPath()
                              .toUriString());
      String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/teamweb/api/v1/usermanagement/users/resetpassword")
              .toUriString();
      
  
    
      String resetPasswordLink = applicationConfigParams
              .getPasswordResetUrl()
              + "/"
              + userService.generatePasswordResetLink(user, uri);
      
      AppConfig appconfig = appConfigService.getAppConfig(SystemConfigGroup.APPLICATION.getCode(),
          "APPLICATION", "user.passwordreseturl");
      System.out.println("appc "+appconfig);
      System.out.println("reset link: "+resetPasswordLink);
      if(appconfig != null){
        resetPasswordLink = appconfig.getConfigValue()+ "/"
                + userService.generatePasswordResetLink(user, uri);;
      }

      
      /*    
      //Setting password reset url        
      AppConfig appconfig = appConfigService.getAppConfig(SystemConfigGroup.APPLICATION.getCode(),
              "APPLICATION", "user.passwordreseturl");
  
      TemplateData templateData = new TemplateData();
      String data = "{\"username\":\"" + username
              + "\",\"resetpasswordlink\":\"" + resetpasswordlink + "\"}";
      templateData.setData(data);
      templateData = templateConfigurationServiceImpl.getRenderedTemplate(
              emailtemplatename, templateData);

      EmailTransaction emailTransaction = new EmailTransaction();
      List<String> toAddress = new ArrayList<>();
      toAddress.add(user.getEmail());

      emailTransaction.setToaddress(toAddress);
      emailTransaction.setCcaddress(null);
      emailTransaction.setBccaddress(null);

      emailTransaction.setSubject("Reset Password");
      emailTransaction.setBody(templateData.getRenderedOutput());
      emailTransaction.setContenttype("text/html");

      emailTransaction = emailTransactionService.add(emailTransaction);
      if (emailTransaction == null)
          return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
  */
      return new ResponseEntity(HttpStatus.ACCEPTED);
  
  
  
  }

  
  @ApiOperation(value = "Reset Password", response = ApiResult.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Password reset successful"),
          @ApiResponse(code = 404, message = "User not found") })
  @RequestMapping(value = "/usermanagement/users/resetpassword", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResult<User> passwordResetUser(@RequestBody ResetPasswordPojo request) {
      logger.info("Attempting password reset for email: {}", request.getEmail());
      
      User userWithToken;
      
      try {
          // Call the service to process the password reset based on email and password
          userWithToken = userService.changePassword(request);
          logger.info("Password reset successful for user: {}", request.getEmail());
          
          // Return the result wrapped in ApiResult
          return new ApiResult<>(HttpStatus.OK.value(), "Password reset successful", userWithToken);
          
      } catch (CustomException e) {
          // Handle exception and log the error
          logger.error("Error during password reset for email: {}", request.getEmail(), e);
          
          // Optionally, customize the response in case of failure
          return new ApiResult<>(HttpStatus.BAD_REQUEST.value(), "Password reset failed: " + e.getMessage(), null);
      }
  }



  @RequestMapping(value = "/usermanagement/verifyGoogleIdToken", method = {RequestMethod.POST},
	      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> verifyGoogleIdToken(@RequestBody GoogleTokenInfo idToken) {
	  logger.info("verifyGoogleIdToken="+idToken);

	  if(idToken.getJti() != null && !idToken.getJti().isEmpty() && idToken.getEmail() != null) {
		  	logger.info("google jwt token valid="+idToken.getJti());
		    User user = new User();
		    user.setSocialId(idToken.getJti());
		    user.setEmail(idToken.getEmail());
		    user.setFirstName(idToken.getName());
		    user.setPhotoUrl(idToken.getPicture());
		    user.setUsername(idToken.getEmail());
		    user = userService.checkSocialUser(user);
		    return new ResponseEntity<User>(user, HttpStatus.OK);
	  }else {
		  logger.error("google jwt token invalid="+idToken.getJti()+"  "+idToken.getEmail());
		  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
	  }
	  
  }
  
  private final String GOOGLE_TOKENINFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo";
  @PostMapping("/usermanagement/verifyGoogleIdTokenDart")
  public ResponseEntity<?> verifyGoogleIdToken(@RequestBody Map<String, String> requestBody) {
	  logger.info("verifyGoogleIdTokenDart=");
      String idToken = requestBody.get("idToken");

      // Validate the Google ID token and retrieve user details
      Map<String, String> userDetails = validateAndExtractUserDetails(idToken);

      if (userDetails != null) {
          // Perform actions with user details
          return ResponseEntity.ok(userDetails);
      } else {
          // Handle invalid token
          return ResponseEntity.badRequest().body("Invalid token");
      }
  }

  private Map<String, String> validateAndExtractUserDetails(String idToken) {
      RestTemplate restTemplate = new RestTemplate();
      Map<String, String> params = new HashMap<>();
      params.put("id_token", idToken);

      // Make a request to Google's tokeninfo endpoint
      Map<String, ?> tokenInfo = restTemplate.getForObject(GOOGLE_TOKENINFO_URL + "?id_token={id_token}", Map.class, params);

      // Check if the token is valid based on the response
      if (tokenInfo != null && !tokenInfo.containsKey("error")) {
          // Extract user details from the tokenInfo map
          Map<String, String> userDetails = new HashMap<>();
          userDetails.put("email", (String) tokenInfo.get("email"));
          userDetails.put("name", (String) tokenInfo.get("name"));
          userDetails.put("picture", (String) tokenInfo.get("picture"));
          // You can add more user details as needed

          return userDetails;
      }

      return null;
  }
  
  
  
  
}
