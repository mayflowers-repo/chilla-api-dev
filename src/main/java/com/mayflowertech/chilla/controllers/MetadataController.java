package com.mayflowertech.chilla.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.Country;
import com.mayflowertech.chilla.entities.District;
import com.mayflowertech.chilla.entities.PersonalizedService;
import com.mayflowertech.chilla.entities.State;
import com.mayflowertech.chilla.services.IMetadataService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/metadata")
public class MetadataController {
	private static final Logger logger = LoggerFactory.getLogger(MetadataController.class);

	@Autowired
	private IMetadataService metadataService;

	@ApiOperation(value = "View a list of all countries")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = "/countries", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<Country>> getCountries() {
	    try {
	        logger.info("GET countries");
	        List<Country> countries = metadataService.getCountries();
	        
	        // Return success response with countries list
	        ApiResult<List<Country>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved countries", countries);
	        return apiResult;

	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving countries", ex);
	        // Return error response for general server error
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}


	
	@ApiOperation(value = "View a list of all states")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = "/states", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<State>> getStates() {
	    try {
	        logger.info("GET states");
	        List<State> states = metadataService.getStates(null);
	        
	        // Return success response with states list
	        ApiResult<List<State>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved states", states);
	        return apiResult;

	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving states", ex);
	        // Return error response for general server error
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}


	@ApiOperation(value = "View a list of all districts")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = "/districts", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<District>> getDistricts() {
	    try {
	        logger.info("GET districts");
	        List<District> districts = metadataService.getDistricts(null);
	        
	        // Return success response with districts list
	        ApiResult<List<District>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved districts", districts);
	        return apiResult;

	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving districts", ex);
	        // Return error response for general server error
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}


	
	@ApiOperation(value = "View a list of all personalized services")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = "/services", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<PersonalizedService>> getPersonServices() {
	    try {
	        logger.info("GET personalized services");
	        List<PersonalizedService> services = metadataService.getServices(false);
	        
	        // Return success response with services list
	        ApiResult<List<PersonalizedService>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved personalized services", services);
	        return apiResult;

	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving personalized services", ex);
	        // Return error response for general server error
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}

	@ApiOperation(value = "View a list of all value-added services")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
	    @ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = "/vaservices", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<PersonalizedService>> getValueAddedServices() {
	    try {
	        logger.info("GET value-added services");
	        List<PersonalizedService> services = metadataService.getServices(true);

	        // Return success response with the list of value-added services
	        return new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved value-added services", services);

	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving value-added services", ex);
	        // Return error response for general server error
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}


}
