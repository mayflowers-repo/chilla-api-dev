package com.mayflowertech.chilla.services;

import java.util.List;

import com.mayflowertech.chilla.entities.Location;

public interface ILocationService {
		List<Location> findByPincode(String pincode);
	    
	    List<String> findAllDistrictsInState(String stateName);
	    
	    List<String> findAllStates();    
}
