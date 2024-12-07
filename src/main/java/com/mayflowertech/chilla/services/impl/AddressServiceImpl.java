package com.mayflowertech.chilla.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Address;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.repositories.IAddressRepository;
import com.mayflowertech.chilla.repositories.IPatientRepository;
import com.mayflowertech.chilla.repositories.IUserRepository;
import com.mayflowertech.chilla.services.IAddressService;
import com.mayflowertech.chilla.services.IUserService;

@Service
public class AddressServiceImpl implements IAddressService {
	
	  @Autowired
	  private IAddressRepository addressRepository;

	  @Autowired
	  private IUserService userService;
	
	  @Autowired
	  private IUserRepository userRepository;
	  
	  @Autowired
	  private IPatientRepository patientRepository;
	  
	  
	@Override
	public Address createAddressForUser(Address address, String userId) throws CustomException {
	    User user = userService.getById(userId);
	    if (user == null) {
	        throw new CustomException("User not found with ID: " + userId);
	    }

	    // Save the new address
	    Address savedAddress = addressRepository.save(address);

	    // Associate the address with the user
	    user.getAddresses().add(savedAddress);
	    userRepository.save(user); // Save the updated user

	    return savedAddress;
	}


	@Override
	public Address createAddressForPatient(Address address, Long patientId) throws CustomException {
	    // Retrieve the user by ID
	    Optional<Patient> patientOptional = patientRepository.findById(patientId);
	    if (patientOptional.isEmpty()) {
	        throw new CustomException("Patient not found with ID: " + patientId);
	    }

	    Patient patient = patientOptional.get();
	    Address savedAddress = addressRepository.save(address);
	    patient.getAddresses().add(savedAddress);
	    patientRepository.save(patient);
		return savedAddress;
	}

	@Override
	public List<Address> getAddressesByUserId(UUID userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Address> getAddressesByPatientId(Long patientId) {
		// TODO Auto-generated method stub
		return null;
	}



}
