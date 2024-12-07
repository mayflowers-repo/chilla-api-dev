package com.mayflowertech.chilla.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.College;
import com.mayflowertech.chilla.entities.Country;
import com.mayflowertech.chilla.entities.PersonalizedService;
import com.mayflowertech.chilla.entities.State;
import com.mayflowertech.chilla.repositories.ICollegeRepository;
import com.mayflowertech.chilla.repositories.ICountryRepository;
import com.mayflowertech.chilla.repositories.ILocationRepository;
import com.mayflowertech.chilla.repositories.IStateRepository;
import com.mayflowertech.chilla.repositories.IpServiceRepository;
import com.mayflowertech.chilla.services.IMetadataService;

@Service
public class MetadataService implements IMetadataService{
	private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);
	
    @Autowired
    private ICollegeRepository collegeRepository;
    

    
    @Autowired
    private ICountryRepository countryRepository;
   
    @Autowired
    private IpServiceRepository serviceRepository;
    
    @Autowired
    private IStateRepository stateRepository;
    
	@Autowired
	private ILocationRepository locationRepository;
    
	@Override
	public List<String> getDistricts(State state) throws CustomException {
		 if (state == null || state.getName() == null) {
		        throw new CustomException("State cannot be null or have a null name.");
		 }
		 return locationRepository.findDistinctDistrictsByStateName(state.getName().toUpperCase());
	}
   
	
	@Override
	public List<PersonalizedService> getServices(boolean valueAdded) {
		return serviceRepository.findByValueAdded(valueAdded);
	}

	@Override
	public List<Country> getCountries() {
		return countryRepository.findAll();
	}

	@Override
	public List<College> getColleges() {
		return collegeRepository.findAll();
	}

	@Override
	public List<State> getStates(Country country) {
		return stateRepository.findAll();
	}

}
