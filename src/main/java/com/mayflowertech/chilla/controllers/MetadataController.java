package com.mayflowertech.chilla.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.Country;
import com.mayflowertech.chilla.entities.PersonalizedService;
import com.mayflowertech.chilla.entities.State;
import com.mayflowertech.chilla.enums.PatientRelation;
import com.mayflowertech.chilla.services.IMetadataService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/metadata")
public class MetadataController {
	private static final Logger logger = LoggerFactory.getLogger(MetadataController.class);

	@Autowired
	private IMetadataService metadataService;
	
	
	@RequestMapping(value = "/countries", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<String>> getCountries() {
	    try {
	        logger.info("GET countries");
	        
	        // Fetch the list of countries
	        List<Country> countries = metadataService.getCountries();

	        // Map the list of Country objects to a list of country names (String)
	        List<String> countryNames = countries.stream()
	                                             .map(Country::getName) // Assuming `getName` is the method to get country name
	                                             .collect(Collectors.toList());

	        // Return success response with country names
	        ApiResult<List<String>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved countries", countryNames);
	        return apiResult;

	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving countries", ex);
	        // Return error response for general server error
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}



	@RequestMapping(value = "/states", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<String>> getStates() {
	    try {
	        logger.info("GET states");
	        
	        // Get the list of states from metadataService
	        List<State> states = metadataService.getStates(null);
	        
	        // Map the list of State objects to a list of State names (String)
	        List<String> stateNames = states.stream()
	            .map(State::getName) // Assuming the State class has a getName() method
	            .collect(Collectors.toList());

	        // Return success response with the list of state names
	        ApiResult<List<String>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved states", stateNames);
	        return apiResult;

	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving states", ex);
	        // Return error response for general server error
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}

	

	@RequestMapping(value = "/districts", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<String>> getDistricts(
		    @RequestParam(value = "stateName", required = true) String stateName  // Adding query parameter for stateName
		) {
	    try {
	        logger.info("GET districts for "+stateName);
	        State state = new State();
	        if(stateName == null || stateName.isEmpty()) {
	        	stateName = "KERALA";
	        }
	        state.setName(stateName);
	        List<String> districts = metadataService.getDistricts(state);
	        
	        // Return success response with districts list
	        ApiResult<List<String>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved districts", districts);
	        return apiResult;

	    } catch (CustomException e) {
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving districts", ex);
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
		}
	}


	

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



	@RequestMapping(value = "/patientrelations", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<String>> getPatientRelations() {
	    try {
	        logger.info("GET relations");
	        List<String> relations = Arrays.stream(PatientRelation.values())
                    .map(PatientRelation::name) 
                    .collect(Collectors.toList());
	        
	        // Return success response with countries list
	        ApiResult<List<String>> apiResult = new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved relations", relations);
	        return apiResult;

	    } catch (Exception ex) {
	        logger.error("Error: An unexpected error occurred while retrieving relations", ex);
	        // Return error response for general server error
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}

	
	
}
