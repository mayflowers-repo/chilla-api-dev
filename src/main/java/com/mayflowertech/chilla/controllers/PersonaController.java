package com.mayflowertech.chilla.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.JacksonFilterConfig;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.CustomerPojo;
import com.mayflowertech.chilla.entities.pojo.PatientPojo;
import com.mayflowertech.chilla.entities.pojo.UserSignupPojo;
import com.mayflowertech.chilla.services.IPersonaService;
import com.mayflowertech.chilla.services.IUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/persona")
public class PersonaController {

	@Autowired
	private IUserService userService;



	@Autowired
	private IPersonaService personaService;
	
	  @Autowired
	  private JacksonFilterConfig jacksonFilterConfig;
	
	private static final Logger logger = LoggerFactory.getLogger(PersonaController.class);

	@ApiOperation(value = "Sign up a new persona", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "User added/updated successfully"),
			@ApiResponse(code = 409, message = "User already exists"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@RequestMapping(value = "/signup", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/signup")
	public ApiResult<User> addUser(@RequestBody UserSignupPojo persona) {
	    logger.info("signup API: " + persona);
	    try {
	        User user = personaService.signUpUser(persona);
	        return new ApiResult<>(HttpStatus.OK.value(), "User saved successfully.", user);
	    } catch (CustomException e) {
	        return new ApiResult<>(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(), null);
	    } catch (Throwable e) {
	        e.printStackTrace();
	        return new ApiResult<>(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(), null);
	    }
	}


	
	  @ApiOperation(value = "Register new customer", response = Customer.class)
	  @ApiResponses(value = {@ApiResponse(code = 201, message = "Customer added/updated successfully"),
	      @ApiResponse(code = 409, message = "Customer already exists"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
	  @RequestMapping(value = "/regcustomer", method = {RequestMethod.POST},
	      produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<CustomerPojo> registerCustomer(@RequestBody CustomerPojo customer) {

	    try {      
	      logger.info("registering customer "+customer);
	      Customer newCustomer = userService.registerCustomer(customer);
	    } catch (Throwable e) {
	      e.printStackTrace();
	      return new ApiResult<CustomerPojo>(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(), customer);  
	    }
	    return new ApiResult<CustomerPojo>(HttpStatus.OK.value(), "User saved successfully.", customer);
	    
	  }
	  
	 


	  @ApiOperation(value = "Enroll a new patient", response = Patient.class)
	  @ApiResponses(value = {@ApiResponse(code = 201, message = "Patient added/updated successfully"),
	      @ApiResponse(code = 409, message = "Patient already exists"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
	  @RequestMapping(value = "/enrollpatient", method = {RequestMethod.POST},
	      produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<PatientPojo> enrollPatient(@RequestBody PatientPojo patient) {
		  Patient newPatient = null;
	    try {      
	      logger.info("enrolling patient "+patient.getEmail());
	      newPatient = userService.enrollPatient(patient);
	    } catch (Throwable e) {
	      e.printStackTrace();
	      return new ApiResult<PatientPojo>(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(), patient);  
	    }
	    return new ApiResult<PatientPojo>(HttpStatus.OK.value(), "Patient enrolled successfully.", patient);
	    
	  }


	  
	  
	  @ApiOperation(value = "View a list of all users")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully retrieved list"),
	      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	      @ApiResponse(code = 500, message = "Internal server error")
	  })
	  @RequestMapping(value = "/users", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<List<User>> getUsers(Model model) {
	      try {
	          List<User> users = userService.getAllUsers();
	          logger.info("persona GET users");

	          // Return success response with users list
	          ApiResult<List<User>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved users", users);
	          jacksonFilterConfig.printExistingFilters();
	          return apiResult;

	      } catch (Exception ex) {
	          logger.error("Error: An unexpected error occurred", ex);
	          // Return error response for general server error
	          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	      }finally {
	    	  jacksonFilterConfig.clearFilters(); 
	      }
	  }

	  
	  @ApiOperation(value = "View a list of all customers")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully retrieved list"),
	      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	      @ApiResponse(code = 500, message = "Internal server error")
	  })
	  @RequestMapping(value = "/customers", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<List<Customer>> getCustomers(Model model) {
	      try {
	          List<Customer> customers = personaService.listCustomers();
	          logger.info("GET customers");

	          // Return success response with customers list
	          ApiResult<List<Customer>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved customers", customers);
	          jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email");
	          jacksonFilterConfig.applyFilters("CustomerFilter", "id", "registeredUser");
	          return apiResult;

	      } catch (Exception ex) {
	          logger.error("Error: An unexpected error occurred", ex);
	          // Return error response for general server error
	          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	      }finally {
	    	  jacksonFilterConfig.clearFilters(); 
	      }
	  }


	  
	  @ApiOperation(value = "View a list of all managers")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully retrieved list"),
	      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	      @ApiResponse(code = 500, message = "Internal server error")
	  })
	  @RequestMapping(value = "/managers", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<List<Manager>> getManagers(Model model) {
	      try {
	          List<Manager> managers = personaService.listManagers();
	          logger.info("GET managers");

	          jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email");
	          jacksonFilterConfig.applyFilters("ManagerFilter", "id", "registeredUser");
	          // Return success response with managers list
	          ApiResult<List<Manager>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved managers", managers);
	          return apiResult;

	      } catch (Exception ex) {
	          logger.error("Error: An unexpected error occurred", ex);
	          // Return error response for general server error
	          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	      }finally {
	    	  jacksonFilterConfig.clearFilters();
	      }
	  }

	  
	  
	  @ApiOperation(value = "View a list of all students")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully retrieved list"),
	      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	      @ApiResponse(code = 500, message = "Internal server error")
	  })
	  @RequestMapping(value = "/students", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<List<Student>> getStudents(Model model) {
	      try {
	          List<Student> students = personaService.listStudents();
	          logger.info("GET students");

	          // Return success response with students list
	          ApiResult<List<Student>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved students", students);
	          jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email");
	          jacksonFilterConfig.applyFilters("StudentFilter", "id", "registeredUser");
	          return apiResult;

	      } catch (Exception ex) {
	          logger.error("Error: An unexpected error occurred", ex);
	          // Return error response for general server error
	          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	      }finally {
	    	  jacksonFilterConfig.clearFilters(); 
	      }
	  }

	  
	  @ApiOperation(value = "View a list of all patients")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully retrieved list"),
	      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	      @ApiResponse(code = 500, message = "Internal server error")
	  })
	  @RequestMapping(value = "/patients", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<List<Patient>> getPatients(Model model) {
	      try {
	          List<Patient> patients = personaService.listAllPatients();
	          logger.info("GET patients");

	          // Return success response with patients list
	          ApiResult<List<Patient>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved patients", patients);
	          jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email");
	          jacksonFilterConfig.applyFilters("PatientFilter", "id", "registeredUser");
	          return apiResult;

	      } catch (Exception ex) {
	          logger.error("Error: An unexpected error occurred", ex);
	          // Return error response for general server error
	          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	      }finally {
	    	  jacksonFilterConfig.clearFilters(); 
	      }
	  }

	  
	  
	  @ApiOperation(value = "View a list of patients enrolled by a specific customer")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully retrieved list"),
	      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	      @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	      @ApiResponse(code = 500, message = "Internal server error")
	  })
	  @RequestMapping(value = "/patientsby/{customerId}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	  public ApiResult<List<Patient>> getPatientsByCustomerId(@PathVariable Long customerId) {
	      try {
	          List<Patient> patients = personaService.listPatientsEnrolledBy(customerId);
	          logger.info("GET patients enrolled by customer ID: {}", customerId);

	          // Return success response with patients list
	          ApiResult<List<Patient>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved patients", patients);
	          return apiResult;

	      } catch (Exception ex) {
	          logger.error("Error: An unexpected error occurred while retrieving patients for customer ID: {}", customerId, ex);
	          // Return error response for general server error
	          return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	      }
	  }

}
