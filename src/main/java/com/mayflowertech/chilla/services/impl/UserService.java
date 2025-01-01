package com.mayflowertech.chilla.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.AuthorizationConstants;
import com.mayflowertech.chilla.config.Constants;
import com.mayflowertech.chilla.config.JwtAuthorizationProvider;
import com.mayflowertech.chilla.config.RSAEncryptionConfigUtil;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Address;
import com.mayflowertech.chilla.entities.Country;
import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.AddressPojo;
import com.mayflowertech.chilla.entities.pojo.CustomerPojo;
import com.mayflowertech.chilla.entities.pojo.ManagerPojo;
import com.mayflowertech.chilla.entities.pojo.PatientPojo;
import com.mayflowertech.chilla.entities.pojo.ResetPasswordPojo;
import com.mayflowertech.chilla.entities.pojo.StudentPojo;
import com.mayflowertech.chilla.enums.SystemRoles;
import com.mayflowertech.chilla.enums.UserStatus;
import com.mayflowertech.chilla.repositories.IAddressRepository;
import com.mayflowertech.chilla.repositories.ICountryRepository;
import com.mayflowertech.chilla.repositories.ICustomerRepository;
import com.mayflowertech.chilla.repositories.IManagerRepository;
import com.mayflowertech.chilla.repositories.IPatientRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;
import com.mayflowertech.chilla.repositories.IUserRepository;
import com.mayflowertech.chilla.services.IMailService;
import com.mayflowertech.chilla.services.IRoleService;
import com.mayflowertech.chilla.services.IUserService;
import com.mayflowertech.chilla.utils.CommonUtils;
import com.mayflowertech.chilla.utils.PasswordUtils;

@Service
public class UserService implements  IUserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private ICustomerRepository customerRepository;
	
	@Autowired
	private IStudentRepository studentRepository;

	@Autowired
	private IManagerRepository managerRepository;

	@Autowired
	private IPatientRepository patientRepository;
	
	@Autowired
	private JwtAuthorizationProvider jwtTokenUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IMailService mailService;
	
	@Autowired
	private ICountryRepository countryRepository;

	@Autowired
	private IAddressRepository addressRepository;
	


	@Override
	public User createUser(User user) throws Throwable {
		User existingUser = getByEmail(user.getEmail());
		if (existingUser != null) {
			throw new CustomException("Email already in use");
		}
		if (isExist(user)) {
			return updateUser(user, false);
		}
		user.setPassword(PasswordUtils.generateSecurePassword(user.getPassword(), Constants.PASSWORD_SALT));

		user.setStatus(UserStatus.INACTIVE.getCode());
		user = userRepository.save(user);
		logger.info("user updated in db: " + user);
		return user;
	}



	@Override
	public User updateUser(User user, boolean changepassword) {
		logger.debug("updateUser  " + user);

		User ret = userRepository.findById(user.getId());
		if (ret != null) {
			if (user.getReasonForDeactivate() != null && !user.getReasonForDeactivate().isEmpty())
				ret.setReasonForDeactivate(user.getReasonForDeactivate());
			if (user.getReasonForDelete() != null && !user.getReasonForDelete().isEmpty())
				ret.setReasonForDelete(user.getReasonForDelete());
			if (user.getUsername() != null && !user.getUsername().isEmpty())
				ret.setUsername(user.getUsername());
			if (user.getEmail() != null && !user.getEmail().isEmpty())
				ret.setEmail(user.getEmail());
			if (user.getFirstName() != null && !user.getFirstName().isEmpty())
				ret.setFirstName(user.getFirstName());
			if (user.getLastName() != null && !user.getLastName().isEmpty())
				ret.setLastName(user.getLastName());
			if (user.getProvider() != null && !user.getProvider().isEmpty())
				ret.setProvider(user.getProvider());
			if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty())
				ret.setPhotoUrl(user.getPhotoUrl());

			if (changepassword)
				ret.setPassword(PasswordUtils.generateSecurePassword(user.getPassword(), Constants.PASSWORD_SALT));
			ret.setActive(user.isActive());
			return userRepository.save(ret);
		}
		return null;
	}

	@Override
	public User getUser(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User getById(String id) {
		return userRepository.findById(UUID.fromString(id));
	}

	

	@Override
	public boolean isExist(User user) {
		User ret = userRepository.findByUsername(user.getUsername());
		if (ret == null && user.getId() != null) {
			ret = userRepository.findById(user.getId());
		}
		if (ret == null)
			return false;
		else
			return true;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> getAllActiveUsers() {
		// TODO Auto-generated method stub
		return userRepository.findByActiveOrderByUsernameAsc(true);
	}

	
	public String generatePasswordResetLink(User user, String uri) {
		String ret = "";

		if (user == null)
			return ret;
		if (user.getUsername() == null)
			return ret;
		if (user.getUsername().trim().isEmpty())
			return ret;

		long ut1 = Instant.now().getEpochSecond();
		String data = uri + "##" + ut1 + "##" + user.getUsername();
		ret = CommonUtils.encrypt(data, AuthorizationConstants.AES_SIGNINGSALT, AuthorizationConstants.AES_SIGNINGSALT);

		return ret.replace("/", "**").replace("\n", "").replace("\r", "");
	}

	
	

	@Override
	public User checkSocialUser(User user) throws CustomException{

		if (isExist(user)) {
			user = userRepository.findByUsername(user.getUsername());
			Optional<Customer> customerOptional =  customerRepository.findByRegisteredUser(user);
			if(customerOptional.isPresent()) {
				long customerId = customerOptional.get().getCustomerId();
				user.setCustomerId(customerId);
			}else {
				throw new CustomException("The user yet to register as customer");
			}
		} else {
			user.setPassword(PasswordUtils.generateSecurePassword(user.getSocialId(), Constants.PASSWORD_SALT));
			Role role = roleService.getRoleByName(SystemRoles.CUSTOMER.getRoleCode());
			user = userRepository.save(user);
			//addRoletoUser(user, role);
		}

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getSocialId()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtTokenUtil.generateToken(authentication);
		user.setAuthtoken(token);
		return user;
	}

	@Override
	public User getByEmail(String email) {
	    User user = userRepository.findByEmail(email);
	    if (user != null) {
	        // Ensures the addresses are loaded
	        user.getAddresses().size(); // Forces initialization of the addresses collection
	    }
	    return user;
	}

	@Transactional
	public Customer registerCustomer(CustomerPojo customerPojo) throws Throwable {
	    // Check if the user already exists in the `users` table
	    User existingUser = userRepository.findByEmail(customerPojo.getEmail());
	    if (existingUser == null) {
	        throw new CustomException("User does not exist");
	    }

	    existingUser.setRegistered(true);
	    // Check if the Customer entry already exists for the user
	    Optional<Customer> existingCustomerOpt = customerRepository.findByRegisteredUser(existingUser);
	    if (existingCustomerOpt.isPresent()) {
	        throw new CustomException("Customer already exists for this user");
	    }

	    Country country = countryRepository.findByName(customerPojo.getCountry());
	    if(country == null) {
	    	 throw new CustomException("Invalid country");
	    }
	    
	    // Create a new Customer entity and set the fields from CustomerPojo
	    Customer newCustomer = new Customer();
	    newCustomer.setJob(customerPojo.getJob());
	    newCustomer.setCity(customerPojo.getCity());
	    newCustomer.setCountry(country);
	    
	    // Set the registeredUser to the existing user
	    newCustomer.setRegisteredUser(existingUser);
	    newCustomer = customerRepository.save(newCustomer);
	  
	    existingUser.setFirstName(customerPojo.getFirstName());
	    existingUser.setLastName(customerPojo.getLastName());
	    logger.info(customerPojo.getAddress()+"");
	    Address address = AddressPojo.convertToAddress(customerPojo.getAddress());
	    address = addressRepository.save(address);
	    existingUser.addAddress(address);
	    userRepository.save(existingUser);
	    
	    return newCustomer;
	}

	@Override
	public Student registerStudent(StudentPojo student) throws Throwable {
	    User existingUser = userRepository.findByEmail(student.getEmail());
	    if (existingUser == null) {
	        throw new CustomException("User does not exist");
	    }

	    Optional<Student> existingManagerOpt = studentRepository.findByRegisteredUser(existingUser);
	    if (existingManagerOpt.isPresent()) {
	        throw new CustomException("Student already exists as this user");
	    }
	    
	    if(student.getFirstName() != null || student.getLastName() != null) {
	    	existingUser.setFirstName(student.getFirstName());
	    	existingUser.setLastName(student.getLastName());
	    	userRepository.save(existingUser);
	    }

	    Student newStudent = new Student();
	    newStudent.setAge(student.getAge());
	    newStudent.setCollege(student.getCollege());
	    newStudent.setCompletionYear(student.getCompletionYear());
	    newStudent.setCourse(student.getCourse());
	    newStudent.setRegisteredUser(existingUser);
	    return studentRepository.save(newStudent);
	}

	@Override
	public Patient enrollPatient(PatientPojo pojo) throws Throwable {
	    logger.info("service : enrollPatient ");
	    
	    if (pojo.getMobile() != null && !pojo.getMobile().isEmpty()) {
	        Optional<Patient> optionalPatient = patientRepository.findByMobile(pojo.getMobile());
	        if (optionalPatient.isPresent()) {
	            throw new CustomException("Patient already exists with mobile: " + pojo.getMobile());
	        }
	    }

	    Patient patient = new Patient();
	    patient.setAge(pojo.getAge());
	    patient.setActive(true);
	    patient.setEnrolledBy(pojo.getEnrolledBy());
	    patient.setFirstName(pojo.getFirstName());
	    patient.setLastName(pojo.getLastName());
	    patient.setHealthDescription(pojo.getHealthDescription());
	    patient.setGender(pojo.getGender());
	    patient.setRelationWithPatient(pojo.getRelationWithPatient());
	    patient.setEmail(pojo.getEmail());
	    patient.setMobile(pojo.getMobile());

	    // Save patient to generate ID
	    patient = patientRepository.save(patient);

	    if (pojo.getAddress() != null) {
	        Address address = new Address();
	        address.setFirst(pojo.getAddress().getFlatDetails());
	        address.setSecond(pojo.getAddress().getApartmentDetails());
	        address.setPincode(pojo.getAddress().getPincode());
	        address.setLandmark(pojo.getAddress().getLandmark());
	        address.setDistrict(pojo.getAddress().getDistrict());
	        address.setState(pojo.getAddress().getState());
	        address.setMap(pojo.getAddress().getMap());
	        // Save address
	        Address savedAddress = addressRepository.save(address);

	        // Add address to patient
	        patient.addAddress(savedAddress);

	        // Save patient again to update the join table
	        patient = patientRepository.save(patient);
	        logger.info("Patient updated with address association");
	    }

	    return patient;
	}


	

	@Override
	public Manager registerManager(ManagerPojo manager) throws Throwable {
	    User existingUser = userRepository.findByEmail(manager.getEmail());
	    if (existingUser == null) {
	        throw new CustomException("User does not exist");
	    }

	    Optional<Manager> existingManagerOpt = managerRepository.findByRegisteredUser(existingUser);
	    if (existingManagerOpt.isPresent()) {
	        throw new CustomException("Manager already exists as this user");
	    }

	    Manager newManager = new Manager();
	    newManager.setRegisteredUser(existingUser);
	    return managerRepository.save(newManager);
	}

	@Override
	public Long getManagerId(User user) throws CustomException {
	    Optional<Manager> managerOptional = managerRepository.findByRegisteredUser(user);
	    if (managerOptional.isPresent()) {
	        return managerOptional.get().getManagerId();  
	    } else {
	        // Throw CustomException if manager is not found
	        throw new CustomException("The user dont have Manager role.");
	    }
	}


	@Override
	public Long getStudentId(User user) throws CustomException {
		Optional<Student> studentOptional =  studentRepository.findByRegisteredUser(user);
		if(studentOptional.isPresent()) {
			return studentOptional.get().getStudentId();
		}else {
			throw new CustomException("The user yet to register as student");
		}
	}

	@Override
	public User getByMobile(String mobile) {
		return userRepository.findByMobile(mobile);
	}

	@Override
	public User updateUserStatus(UUID userId, String newStatus) throws CustomException {
        // Fetch the user from the repository
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new CustomException("User not found");
        }
        logger.info("Updating the status to "+newStatus+"  for user "+user);
        user.setStatus(newStatus);

        return userRepository.save(user);
    }

	@Override
	public User changePassword(ResetPasswordPojo pojo) throws CustomException {
	    if (pojo.getEmail() == null || pojo.getPassword() == null) {
	        throw new CustomException("Email or password cannot be null.");
	    }

	    User user = userRepository.findByEmail(pojo.getEmail());
	    if (user == null) {
	        throw new CustomException("User not found with email: " + pojo.getEmail());
	    }
		try {
			RSAEncryptionConfigUtil rsaEncryptionUtil = new RSAEncryptionConfigUtil();
			
			String newPass = rsaEncryptionUtil.decrypt(pojo.getPassword());
			 
			    user.setPassword(PasswordUtils.generateSecurePassword(newPass, Constants.PASSWORD_SALT));
			    user.setOtpWaiting(false);
			    user.setStatus(UserStatus.ACTIVE.getCode());
			    user = userRepository.save(user);
			    logger.info("password changed successfully for user "+user);
		} catch (Exception e) {
			 logger.error("Error updating password "+user);
			e.printStackTrace();
		} catch (CustomException e) {
			 logger.error("Error updating password "+user);
			e.printStackTrace();
		}
	   
	    
	    return user;
	}

	
	@Override
	public User changePasswordInitial(ResetPasswordPojo pojo) throws CustomException {
	    if (pojo.getEmail() == null || pojo.getPassword() == null) {
	        throw new CustomException("Email or password cannot be null.");
	    }

	    User user = userRepository.findByEmail(pojo.getEmail());
	    if (user == null) {
	        throw new CustomException("User not found with email: " + pojo.getEmail());
	    }
	    
	    if(! user.isOtpWaiting()) {
	    	throw new CustomException("User not expecting OTP.");
	    }
	    
	    if (pojo.getPassword().length() < 8) {
	        throw new CustomException("Password must be at least 8 characters long.");
	    }

	  
	    
	    user.setPassword(PasswordUtils.generateSecurePassword(pojo.getPassword(), Constants.PASSWORD_SALT));
	    user.setOtpWaiting(false);
	    user.setStatus(UserStatus.ACTIVE.getCode());
	    user = userRepository.save(user);
	    logger.info("password changed successfully for user "+user);
	    
	    
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), pojo.getPassword())
            );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        user.setAuthtoken(token);

	    return user;
	}
	
	@Override
	public User markUserAsRegistered(String email) throws CustomException {
		User existingUser = userRepository.findByEmail(email);
	    if (existingUser == null) {
	        throw new CustomException("User does not exist "+email);
	    }
	    existingUser.setRegistered(true);
	    existingUser = userRepository.save(existingUser);
	    return existingUser;
	}

	@Override
	public Long getCustomerId(User user) throws CustomException {
		Optional<Customer> customerOptional =  customerRepository.findByRegisteredUser(user);
		if(customerOptional.isPresent()) {
			return customerOptional.get().getCustomerId();
		}else {
			throw new CustomException("The user yet to register as customer");
		}
	}




}
