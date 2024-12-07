package com.mayflowertech.chilla.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.entities.Location;
import com.mayflowertech.chilla.repositories.ILocationRepository;

@Service
public class LocationService implements ILocationService {
	@Autowired
	private ILocationRepository locationRepository;

	@Override
	public List<Location> findByPincode(String pincode) {
		return locationRepository.findByPincode(pincode);
	}

	@Override
	public List<String> findAllDistrictsInState(String stateName) {
		 return locationRepository.findDistinctDistrictsByStateName(stateName.toUpperCase());
	}

	@Override
	public List<String> findAllStates() {
		return locationRepository.findAllStates();
	}

}
