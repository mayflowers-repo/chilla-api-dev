package com.mayflowertech.chilla.services;

import java.util.List;
import java.util.UUID;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Address;

public interface IAddressService {
	
	Address createAddressForUser(Address address, String userId)throws CustomException;
	Address createAddressForPatient(Address address, Long userId)throws CustomException;
	List<Address> getAddressesByUserId(UUID userId);
	List<Address> getAddressesByPatientId(Long patientId);
}
