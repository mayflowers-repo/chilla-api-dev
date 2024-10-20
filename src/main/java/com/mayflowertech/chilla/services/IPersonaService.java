package com.mayflowertech.chilla.services;

import java.util.List;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.UserSignupPojo;

public interface IPersonaService {

	 List<Customer> listCustomers();
	 List<Student> listStudents();
	 List<Manager> listManagers();
	 List<Patient> listAllPatients();
	 
	 List<Patient> listPatientsEnrolledBy(Long customerId);
	 User signUpUser(UserSignupPojo persona) throws CustomException;
}
