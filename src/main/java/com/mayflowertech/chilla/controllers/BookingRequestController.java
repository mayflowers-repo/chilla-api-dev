package com.mayflowertech.chilla.controllers;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.JacksonFilterConfig;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.enums.BookingStatus;
import com.mayflowertech.chilla.services.IBookingRequestService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/bookingrequest")
public class BookingRequestController {
    @Autowired
    private IBookingRequestService bookingRequestService;
    
    @Autowired
    private JacksonFilterConfig jacksonFilterConfig;
    
    private static final Logger logger = LoggerFactory.getLogger(BookingRequestController.class);
    
    // 1. Create a new booking request
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN", "ROLE_CUSTOMER"})
	@ApiOperation(value = "Create a booking request", response = BookingRequest.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Booking Request added successfully"),
			@ApiResponse(code = 409, message = "Booking Request already exists"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@RequestMapping(value = "/create", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<BookingRequest> createBookingRequest(@RequestBody BookingRequest bookingRequest) {
	    try {
	    	logger.info("creating a new booking request");
	        BookingRequest createdBooking = bookingRequestService.createBookingRequest(bookingRequest);
			jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
			jacksonFilterConfig.applyFilters("CustomerFilter", "customerId", "registeredUser");
			jacksonFilterConfig.applyFilters("PatientFilter", "patientId", "registeredUser");
			jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
			jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "enrolledByCustomer", "requestedServices", 
					"preferredGender", "description", "requestedFor", "status", "assignedStudents", "assignedByManager");
	        return new ApiResult<>(HttpStatus.CREATED.value(), "Booking request created successfully.", createdBooking);
	    } catch (CustomException e) {
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
	    } catch (IllegalArgumentException e) {
	        return new ApiResult<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
	    } catch (Exception e) {
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while creating the booking request.", null);
	    }finally {
	    	  jacksonFilterConfig.clearFilters();
	    }
	}

	
	 // 2. Update an existing booking request
	@Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN", "ROLE_CUSTOMER"})
    @ApiOperation(value = "Update a booking request", response = BookingRequest.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Booking Request updated successfully"),
        @ApiResponse(code = 404, message = "Booking Request not found"),
        @ApiResponse(code = 400, message = "Invalid input")
    })
    @PutMapping("/{id}")
    public ApiResult<BookingRequest> updateBookingRequest(
            @PathVariable Long id, 
            @RequestBody BookingRequest bookingRequest) {
    	 try {
    		    logger.info("updating a new booking request");
    	        BookingRequest updatedBooking = bookingRequestService.updateBookingRequest(id, bookingRequest);
    	        return new ApiResult<>(HttpStatus.OK.value(), "Booking request updated successfully.", updatedBooking);
    	    } catch (EntityNotFoundException e) {
    	        return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
    	    } catch (CustomException e) {
    	    	return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
    	    } catch (IllegalArgumentException e) {
    	        return new ApiResult<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    	    } catch (Exception e) {
    	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while updating the booking request.", null);
			}
    }
    
    
    // 3. Fetch all booking requests
	@Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN"})
    @ApiOperation(value = "Retrieve all booking requests", response = BookingRequest.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved booking requests"),
        @ApiResponse(code = 404, message = "Booking Requests not found")
    })
    @GetMapping
    public ApiResult<List<BookingRequest>> getAllBookingRequests() {
        try {
			List<BookingRequest> bookingRequests = bookingRequestService.getAllBookingRequests();
			jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
			jacksonFilterConfig.applyFilters("CustomerFilter", "customerId", "registeredUser");
			jacksonFilterConfig.applyFilters("PatientFilter", "patientId", "registeredUser");
			jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
			jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "enrolledByCustomer", "requestedServices", 
					"preferredGender", "description", "requestedFor", "status", "assignedStudents", "assignedByManager");
			return new ApiResult<>(HttpStatus.OK.value(), "Booking requests fetched successfully.", bookingRequests);
		} catch (Exception e) {
			 return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
		}finally {
	    	  jacksonFilterConfig.clearFilters();
	    }
    }
    
	@Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN", "ROLE_STUDENT"})
    @ApiOperation(value = "List booking requests by student ID", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved booking requests"),
        @ApiResponse(code = 404, message = "No booking requests found for the provided student ID")
    })
    @GetMapping("/student/{studentId}")
    public ApiResult<List<BookingRequest>> listBookingRequestsByStudent(@PathVariable Long studentId) {
        try {
            logger.info("Fetching booking requests for student ID: " + studentId);
            
            List<BookingRequest> bookingRequests = bookingRequestService.listBookingRequestsByStudent(studentId);
			jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
			jacksonFilterConfig.applyFilters("CustomerFilter", "customerId", "registeredUser");
			jacksonFilterConfig.applyFilters("PatientFilter", "patientId", "registeredUser", "age", "gender", "firstName", "lastName", "healthDescription");
			jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
			jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "enrolledByCustomer", "requestedServices", 
					"preferredGender", "description", "requestedFor", "status", "assignedStudents", "assignedByManager");
            return new ApiResult<>(HttpStatus.OK.value(), "Booking requests retrieved successfully.", bookingRequests);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred.", null);
        }finally {
	    	  jacksonFilterConfig.clearFilters();
	    }
    }
    
    
    @ApiOperation(value = "List booking requests by customer ID", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved booking requests"),
        @ApiResponse(code = 404, message = "No booking requests found for the provided customer ID")
    })
    @GetMapping("/customer/{customerId}")
    public ApiResult<List<BookingRequest>> listBookingRequestsByCustomer(@PathVariable Long customerId) {
        try {
            logger.info("Fetching booking requests for customer ID: " + customerId);
            List<BookingRequest> bookingRequests = bookingRequestService.listBookingRequestsByCustomer(customerId);
			jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
			jacksonFilterConfig.applyFilters("CustomerFilter", "customerId", "registeredUser");
			jacksonFilterConfig.applyFilters("PatientFilter", "patientId", "registeredUser");
			jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
			jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "enrolledByCustomer", "requestedServices", 
					"preferredGender", "description", "requestedFor", "status", "assignedStudents", "assignedByManager");
            return new ApiResult<>(HttpStatus.OK.value(), "Booking requests retrieved successfully.", bookingRequests);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred.", null);
        }finally {
	    	  jacksonFilterConfig.clearFilters();
	    }
    }
    
    
    // 4. Fetch a booking request by its ID
    @ApiOperation(value = "Get a booking request by ID", response = BookingRequest.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved booking request"),
        @ApiResponse(code = 404, message = "Booking Request not found")
    })
    @GetMapping("/{id}")
    public ApiResult<BookingRequest> getBookingRequestById(@PathVariable Long id) {
    	 try {
    		 logger.info("fetch a booking request "+id);
             BookingRequest bookingRequest = bookingRequestService.getBookingRequestById(id);
             jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "enrolledByCustomer", "requestedServices", 
   					"preferredGender", "description", "requestedFor", "status", "assignedStudents", "assignedByManager");
             jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
             jacksonFilterConfig.applyFilters("PatientFilter", "patientId", "firstName", "lastName", "age", "gender", "healthDescription");
             jacksonFilterConfig.applyFilters("CustomerFilter", "customerId", "registeredUser");
             return new ApiResult<>(HttpStatus.OK.value(), "Booking request retrieved successfully.", bookingRequest);
         } catch (CustomException e) {
             return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
         } catch (Throwable e) {
             return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred.", null);
         }finally {
	    	  jacksonFilterConfig.clearFilters();
	    }
    }
    
    // 5. Fetch booking requests by status
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN"})
    @ApiOperation(value = "Get booking requests by status", response = BookingRequest.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved booking requests"),
        @ApiResponse(code = 404, message = "Booking Requests not found for the given status")
    })
    @GetMapping("/status/{status}")
    public ApiResult<List<BookingRequest>> getBookingRequestsByStatus(@PathVariable String status) {
    	  try {
            List<BookingRequest> bookingRequests = bookingRequestService.getBookingRequestsByStatus(status);
  			jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
  			jacksonFilterConfig.applyFilters("CustomerFilter", "customerId", "registeredUser");
  			jacksonFilterConfig.applyFilters("PatientFilter", "patientId", "registeredUser");
  			jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
  			jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "enrolledByCustomer", "requestedServices", 
  					"preferredGender", "description", "requestedFor", "status", "assignedStudents", "assignedByManager");
              return new ApiResult<>(HttpStatus.OK.value(), "Booking requests retrieved successfully.", bookingRequests);
          } catch (CustomException e) {
			e.printStackTrace();
			  return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
          } catch (Exception e) {
              return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred.", null);
    	}finally {
	    	  jacksonFilterConfig.clearFilters();
	    }
    }
    
    
   // 6. Fetch booking requests created between two dates
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN"})
    @ApiOperation(value = "Get booking requests created between two dates", response = BookingRequest.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved booking requests"),
        @ApiResponse(code = 404, message = "No Booking Requests found between the specified dates")
    })
    @GetMapping("/created-between")
    public ApiResult<List<BookingRequest>> getBookingRequestsCreatedBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
    	  try {
              List<BookingRequest> bookingRequests = bookingRequestService.getBookingRequestsCreatedBetween(startDate, endDate);
  			jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
  			jacksonFilterConfig.applyFilters("CustomerFilter", "customerId", "registeredUser");
  			jacksonFilterConfig.applyFilters("PatientFilter", "patientId", "registeredUser");
  			jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
  			jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "enrolledByCustomer", "requestedServices", 
  					"preferredGender", "description", "requestedFor", "status", "assignedStudents", "assignedByManager");
              return new ApiResult<>(HttpStatus.OK.value(), "Booking requests retrieved successfully.", bookingRequests);
          } catch (Exception | CustomException e) {
              return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
          }finally {
	    	  jacksonFilterConfig.clearFilters();
	    }
    }
    
    
    
}


