package com.mayflowertech.chilla.controllers;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.JacksonFilterConfig;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.WorkLog;
import com.mayflowertech.chilla.entities.pojo.WorkLogCriteriaPojo;
import com.mayflowertech.chilla.entities.pojo.WorkLogPojo;
import com.mayflowertech.chilla.services.IWorkLogService;
import com.mayflowertech.chilla.utils.CommonUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/worklog")
public class WorkLogController {
	
	private static final Logger logger = LoggerFactory.getLogger(WorkLogController.class);
	
	@Autowired
	private  IWorkLogService workLogService;
	
	  @Autowired
	  private JacksonFilterConfig jacksonFilterConfig;
	  
	  
	  
    @ApiOperation(value = "Create a new work log")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully created work log"),
        @ApiResponse(code = 500, message = "An unexpected error occurred")
    })
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<WorkLogPojo> createWorkLog(@RequestBody WorkLog workLog) {
        try {
        	logger.info("create a work log");
            WorkLog createdWorkLog = workLogService.createWorkLog(workLog);
            WorkLogPojo createdWorkLogPojo = convertToPojo(createdWorkLog);
            return new ApiResult<WorkLogPojo>(HttpStatus.OK.value(), "Work log created successfully", createdWorkLogPojo);
        } catch (CustomException e) {
            return new ApiResult<WorkLogPojo>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResult<WorkLogPojo>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        }
    }
    
    
    @ApiOperation(value = "Complete a work log")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully completed work log"),
        @ApiResponse(code = 404, message = "Work log not found"),
        @ApiResponse(code = 500, message = "An unexpected error occurred")
    })
    @RequestMapping(value = "/complete/{logId}", method = RequestMethod.PUT)
    public ApiResult<WorkLogPojo> completeWorkLog(@PathVariable("logId") Long logId) {
        try {
        	logger.info("complete work log "+logId);
            WorkLog completedWorkLog = workLogService.completeWork(logId);
            WorkLogPojo completedWorkLogPojo = convertToPojo(completedWorkLog);
            return new ApiResult<>(HttpStatus.OK.value(), "Work log completed successfully", completedWorkLogPojo);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        }
    }
    
    
    
    @ApiOperation(value = "Get work logs for a particular day")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved work logs"),
        @ApiResponse(code = 404, message = "No work logs found for the specified date"),
        @ApiResponse(code = 500, message = "An unexpected error occurred")
    })
    @RequestMapping(value = "/date/{date}", method = RequestMethod.GET)
    public ApiResult<List<WorkLogPojo>> getWorkLogsByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
        	logger.info("get work logs for a day "+date);
            List<WorkLog> workLogs = workLogService.getWorkLogsByDate(date);
            List<WorkLogPojo> workLogPojos = workLogs.stream()
                .map(this::convertToPojo)
                .collect(Collectors.toList());
            
            return new ApiResult<>(HttpStatus.OK.value(), "Work logs retrieved successfully", workLogPojos);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        }
    }
    
    @ApiOperation(value = "Get the latest unfinished work log for a student")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved work log"),
        @ApiResponse(code = 404, message = "Work log not found"),
        @ApiResponse(code = 500, message = "An unexpected error occurred")
    })
    @RequestMapping(value = "/unfinished/student/{studentId}", method = RequestMethod.GET)
    public ApiResult<WorkLogPojo> getLatestUnfinishedWorkLog(@PathVariable("studentId") Long studentId) {
        try {
        	logger.info("getLatestUnfinishedWorkLog "+studentId);
            WorkLog workLog = workLogService.getLatestUnfinishedWorkLog(studentId);
            WorkLogPojo workLogPojo = convertToPojo(workLog);
            jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "firstName", "lastName");
            jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
            jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "username", "email", "description");
			jacksonFilterConfig.applyFilters("PatientFilter", "patientId", "registeredUser", "age", "gender", "firstName", "lastName", "healthDescription");
            return new ApiResult<>(HttpStatus.OK.value(), "Work log retrieved successfully", workLogPojo);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        }finally {
        	jacksonFilterConfig.clearFilters();
        }
    }
    
    @ApiOperation(value = "Get a work log by ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved work log"),
        @ApiResponse(code = 404, message = "Work log not found"),
        @ApiResponse(code = 500, message = "An unexpected error occurred")
    })
    @RequestMapping(value = "/{logId}", method = RequestMethod.GET)
    public ApiResult<WorkLogPojo> getWorkLog(@PathVariable("logId") Long logId) {
        try {
        	logger.info("get a work log for "+logId);
            WorkLog workLog = workLogService.getWorkLog(logId);
            WorkLogPojo workLogPojo = convertToPojo(workLog);
            return new ApiResult<>(HttpStatus.OK.value(), "Work log retrieved successfully", workLogPojo);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        }
    }
    
    private WorkLogPojo convertToPojo(WorkLog workLog) {
        if (workLog == null) {
            return null; // Handle null case
        }

        WorkLogPojo workLogPojo = new WorkLogPojo();
        workLogPojo.setLogId(workLog.getLogId());
        
        // Set the student directly
        workLogPojo.setStudent(workLog.getStudent());
        workLogPojo.setBookingRequest(workLog.getBookingRequest());
        workLogPojo.setWorkDescription(workLog.getWorkDescription());
        workLogPojo.setWorkStartTime(workLog.getWorkStartTime());
        workLogPojo.setWorkEndTime(workLog.getWorkEndTime());
        String duration = getFormattedDuration(workLog);
        workLogPojo.setDuration(duration);

        return workLogPojo;
    }

    private Duration calculateDuration(WorkLog workLog) {
	    if (workLog.getWorkStartTime() != null && workLog.getWorkEndTime() != null) {
	        return Duration.between(workLog.getWorkStartTime(), workLog.getWorkEndTime());
	    }
	    return Duration.ZERO; // Return zero if duration cannot be calculated
   }
    
    public String getFormattedDuration(WorkLog workLog) {
        Duration duration = calculateDuration(workLog);
        if (duration == null) {
            return "N/A";
        }
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();  // Calculate remaining seconds

        return String.format("%d hours %d minutes %d seconds", hours, minutes, seconds);  // Include seconds
    }


    
    @ApiOperation(value = "Get all work logs for a student by student ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved work logs"),
        @ApiResponse(code = 404, message = "No work logs found for the student"),
        @ApiResponse(code = 500, message = "An unexpected error occurred")
    })
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET)
    public ApiResult<List<WorkLogPojo>> getWorkLogsByStudentId(@PathVariable("studentId") Long studentId) {
        try {
            logger.info("Retrieving work logs for studentId: " + studentId);
            List<WorkLog> workLogs = workLogService.getWorkLogsByStudentId(studentId);
            if (workLogs.isEmpty()) {
                return new ApiResult<>(HttpStatus.NOT_FOUND.value(), "No work logs found for the student", null);
            }
            // Convert WorkLog entities to WorkLogPojo objects
            List<WorkLogPojo> workLogPojos = workLogs.stream()
                .map(this::convertToPojo)
                .toList();
            
            jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email");
            jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
            jacksonFilterConfig.applyFilters("BookingRequestFilter", "id", "username", "email");

            return new ApiResult<>(HttpStatus.OK.value(), "Work logs retrieved successfully", workLogPojos);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error while retrieving work logs for studentId: " + studentId, e);
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        }finally {
        	jacksonFilterConfig.clearFilters();
        }
    }
    
    
    @ApiOperation(value = "Get work logs based on criteria")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved work logs based on criteria"),
        @ApiResponse(code = 404, message = "No work logs found for the given criteria"),
        @ApiResponse(code = 500, message = "An unexpected error occurred")
    })
    @RequestMapping(value = "/worklogsbycriteria", method = RequestMethod.POST)
    public ApiResult<List<WorkLogPojo>> getWorkLogsByCriteria(@RequestBody WorkLogCriteriaPojo workLogCriteria) {
        try {
            logger.info("Retrieving work logs based on criteria: " );
            
            // Validate the WorkLogCriteria object
            validateCriteria(workLogCriteria);
            
            // Call service method to get work logs based on criteria
            List<WorkLog> workLogs = workLogService.getWorkLogsByCriteria(workLogCriteria);
            
            if (workLogs.isEmpty()) {
                return new ApiResult<>(HttpStatus.NOT_FOUND.value(), "No work logs found for the given criteria", null);
            }
            
            // Convert WorkLog entities to WorkLogPojo objects
            List<WorkLogPojo> workLogPojos = workLogs.stream()
                .map(this::convertToPojo)
                .toList();

            // Apply filters for Jackson
            jacksonFilterConfig.applyFilters("UserFilter", "id", "firstName", "lastName");
            jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
            jacksonFilterConfig.applyFilters("BookingRequestFilter", "id");

            return new ApiResult<>(HttpStatus.OK.value(), "Work logs retrieved successfully", workLogPojos);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error while retrieving work logs based on criteria: " + workLogCriteria, e);
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        } finally {
            jacksonFilterConfig.clearFilters();
        }
    }

    private void validateCriteria(WorkLogCriteriaPojo criteria) throws CustomException {
        if (criteria == null) {
            throw new CustomException("Criteria cannot be null.");
        }

        // Validate dates
        if (criteria.getFromDate() == null || criteria.getFromDate().isEmpty()) {
            throw new CustomException("Start date (fromDate) is mandatory.");
        }
        if (criteria.getToDate() == null || criteria.getToDate().isEmpty()) {
            throw new CustomException("End date (toDate) is mandatory.");
        }

        LocalDate fromDate = CommonUtils.parseDate(criteria.getFromDate());
        LocalDate toDate = CommonUtils.parseDate(criteria.getToDate());

        if (fromDate.isAfter(toDate)) {
            throw new CustomException("Start date (fromDate) cannot be after end date (toDate).");
        }

        // Validate email
        if (criteria.getStudentEmail() != null && !criteria.getStudentEmail().isEmpty()) {
            if (! CommonUtils.isValidEmail(criteria.getStudentEmail())) {
                throw new CustomException("Invalid email format for studentEmail.");
            }
        }
    }
    
}
