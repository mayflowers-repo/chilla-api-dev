package com.mayflowertech.chilla.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Address;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.repositories.IAddressRepository;
import com.mayflowertech.chilla.services.IAddressService;
import com.mayflowertech.chilla.services.IUserService;

@Service
public class AddressServiceImpl implements IAddressService {

	  @Autowired
	  private IAddressRepository addressRepository;

	  @Autowired
	  private IUserService userService;

	  
	@Override
	public Address createAddress(String userId) throws CustomException{
		  User user = userService.getById(userId);
		  if(user == null) {
			  throw new CustomException("No user with id "+userId);
		  }
		  Address address = user.getAddress();
		  if(address == null) {
			  address = new Address();
		  }
		  
	      return addressRepository.save(address);      
	}

}
