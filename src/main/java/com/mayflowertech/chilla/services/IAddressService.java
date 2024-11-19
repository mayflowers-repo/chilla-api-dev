package com.mayflowertech.chilla.services;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Address;

public interface IAddressService {
	
	Address createAddress(String userId)throws CustomException;
}
