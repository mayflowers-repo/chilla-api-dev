package com.mayflowertech.chilla.services;

import java.util.List;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.College;
import com.mayflowertech.chilla.entities.Country;
import com.mayflowertech.chilla.entities.PersonalizedService;
import com.mayflowertech.chilla.entities.State;


public interface IMetadataService {
	List<String> getDistricts(State state) throws CustomException;
	List<Country> getCountries();	
	List<College> getColleges();
	List<State> getStates(Country country);
	
	List<PersonalizedService> getServices(boolean valueAdded);
	
}
