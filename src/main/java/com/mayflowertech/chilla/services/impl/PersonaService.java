package com.mayflowertech.chilla.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.Constants;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.AuthUser;
import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.entities.RoleRequest;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.UserSignupPojo;
import com.mayflowertech.chilla.enums.MailOtpPurpose;
import com.mayflowertech.chilla.enums.RoleRequestStatus;
import com.mayflowertech.chilla.enums.SystemRoles;
import com.mayflowertech.chilla.repositories.ICustomerRepository;
import com.mayflowertech.chilla.repositories.IManagerRepository;
import com.mayflowertech.chilla.repositories.IPatientRepository;
import com.mayflowertech.chilla.repositories.IRoleRequestRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;
import com.mayflowertech.chilla.services.IAuthUserService;
import com.mayflowertech.chilla.services.IMailService;
import com.mayflowertech.chilla.services.IPersonaService;
import com.mayflowertech.chilla.services.IRoleService;
import com.mayflowertech.chilla.services.IUserService;

@Service
public class PersonaService implements IPersonaService{
	private static final Logger logger = LoggerFactory.getLogger(PersonaService.class);
    @Autowired
    private IStudentRepository studentRepository;
    
    @Autowired
    private ICustomerRepository customerRepository;
    
    @Autowired
    private IManagerRepository managerRepository;
	
    @Autowired
    private IPatientRepository patientRepository;
    
    
    @Autowired
    private IRoleRequestRepository roleRequestRepository;
    
	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IMailService mailService;
	
	@Autowired
	private IAuthUserService authUserService;
	
	@Override
	public List<Customer> listCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public List<Student> listStudents() {
		return studentRepository.findAll();
	}

	@Override
	public List<Manager> listManagers() {
		return managerRepository.findAll();
	}

	@Override
	public List<Patient> listAllPatients() {
		return patientRepository.findAll();
	}

	@Override
	public List<Patient> listPatientsEnrolledBy(Long customerId) {
		 return patientRepository.findByEnrolledByCustomerId(customerId);
	}

	@Override
	  public User signUpUser(UserSignupPojo persona) throws CustomException {
        User user = new User();
		try {
			if (persona.getEmail() == null || persona.getEmail().isEmpty()) {
			    throw new CustomException("Invalid email");
			}
			if (persona.getPersona() == null || persona.getPersona().isEmpty()) {
			    throw new CustomException("Invalid persona");
			}

			// Validate persona
			try {
			    SystemRoles.valueOf(persona.getPersona().toUpperCase());
			} catch (IllegalArgumentException e) {
			    throw new CustomException("Invalid persona: " + persona.getPersona());
			}

			user.setUsername(persona.getEmail());
			user.setEmail(persona.getEmail());
			user.setFirstName(persona.getFirstName());
			user.setLastName(persona.getLastName());
			user.setOtpWaiting(true);
			user.setMobile(persona.getMobile());
			user.setPassword(Constants.INITIAL_PASSWORD);
			
			// Set password based on persona
			if ("customer".equalsIgnoreCase(persona.getPersona())) {
			    User exisingUser = userService.getByEmail(persona.getEmail());
			    if(exisingUser != null) {
			    	//resend otp
			    	String otp = mailService.generateAndSendOtp(persona.getEmail(),  MailOtpPurpose.EMAIL_VERIFICATION.getCode());
			    	return exisingUser;
			    }else {
			    	if (persona.getMobile() != null) {
					    User userWithMobile = userService.getByMobile(persona.getMobile());
					    if (userWithMobile != null) {
					        throw new CustomException("User already exists with this mobile number");
					    }
					}
					user = userService.createUser(user);
			    }
			}else {
				// Check if mobile number already exists
				if (persona.getMobile() != null) {
				    User userWithMobile = userService.getByMobile(persona.getMobile());
				    if (userWithMobile != null) {
				        throw new CustomException("User already exists with this mobile number");
				    }
				}
				user = userService.createUser(user);
			}


			// Assign roles based on persona
			Role role = null;
			if (SystemRoles.CUSTOMER.getRoleCode().equalsIgnoreCase(persona.getPersona())) {
			    role = roleService.getRoleByName(SystemRoles.CUSTOMER.getRoleCode());
			    String otp = mailService.generateAndSendOtp(persona.getEmail(),  MailOtpPurpose.EMAIL_VERIFICATION.getCode());

			} else if (SystemRoles.STUDENT.getRoleCode().equalsIgnoreCase(persona.getPersona())) {
			    role = roleService.getRoleByName(SystemRoles.STUDENT.getRoleCode());
			   
			} else if (SystemRoles.MANAGER.getRoleCode().equalsIgnoreCase(persona.getPersona())) {
			    role = roleService.getRoleByName(SystemRoles.MANAGER.getRoleCode());           
			}

			if (role != null) {
				AuthUser authUser = new AuthUser();
				authUser.setId(user.getId());
				authUserService.addRoletoUser(authUser, role);
			}
			logger.info("created user and assigned role.");
			RoleRequest roleRequest = new RoleRequest();
			roleRequest.setStatus(RoleRequestStatus.PENDING.getCode());
			roleRequest.setRequestedByUser(user);
			roleRequest.setRequestDate(LocalDateTime.now());
			if (SystemRoles.STUDENT.getRoleCode().equalsIgnoreCase(persona.getPersona())) {
				 roleRequest.setRequestedRole(SystemRoles.STUDENT.getRoleCode());
			}  else if (SystemRoles.MANAGER.getRoleCode().equalsIgnoreCase(persona.getPersona())) {
				roleRequest.setRequestedRole(SystemRoles.MANAGER.getRoleCode());
			}
			if (! "customer".equalsIgnoreCase(persona.getPersona())) {
				 roleRequest = roleRequestRepository.save(roleRequest);
				 logger.info("created a role request");
			}
		}catch (Throwable e) {
			throw new CustomException(e.getMessage());
		}
       

        return user;
    }
}
