package com.mayflowertech.chilla.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.RoleRequest;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.RoleRequestPojo;
import com.mayflowertech.chilla.enums.RoleRequestStatus;
import com.mayflowertech.chilla.enums.SystemRoles;
import com.mayflowertech.chilla.enums.UserStatus;
import com.mayflowertech.chilla.repositories.IManagerRepository;
import com.mayflowertech.chilla.repositories.IRoleRequestRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;
import com.mayflowertech.chilla.repositories.IUserRepository;
import com.mayflowertech.chilla.services.IRoleRequestService;
import com.mayflowertech.chilla.services.IUserService;

@Service
public class RoleRequestService implements IRoleRequestService{
    @Autowired
    private IRoleRequestRepository roleRequestRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(RoleRequestService.class);
    
    
    @Autowired
    private IUserService userService;
    

	@Autowired
	private IStudentRepository studentRepository;

	@Autowired
	private IManagerRepository managerRepository;
	
	@Autowired
	private IUserRepository userRepository;

	@Override
	public List<RoleRequest> getPendingRoleRequests() {
		return roleRequestRepository.findByStatusOrderByRequestDateAsc(RoleRequestStatus.PENDING.getCode());
	}

	@Override
	public RoleRequest updateRoleRequestStatus(Long requestId, String status, String comment) throws CustomException {
		User approvedBy = userService.getCurrentLoggedInUser();
		//logger.info("approvedBy :"+approvedBy);
	    RoleRequest roleRequest = roleRequestRepository.findById(requestId)
	            .orElseThrow(() -> new RuntimeException("Role request not found"));

	    User requestedBy = roleRequest.getRequestedByUser();
	    if(requestedBy == null) {
	    	throw new CustomException("Invalid requestedBy user");
	    }

	    // Update the fields of roleRequest
	    roleRequest.setStatus(status);
	    roleRequest.setComments(comment);
	    roleRequest.setApprovedBy(approvedBy); 
	    roleRequest.setApprovalDate(LocalDateTime.now()); 
	    
	    if(RoleRequestStatus.REJECTED.getCode().equalsIgnoreCase(status)) {
	    	logger.info("rejecting role request");	  
		    userService.updateUserStatus(roleRequest.getRequestedByUser().getId(), UserStatus.REJECTED.getCode());
	    	 return roleRequestRepository.save(roleRequest); 
	    }
	    
	    userService.updateUserStatus(roleRequest.getRequestedByUser().getId(), UserStatus.ACTIVE.getCode());
	    
	    if(SystemRoles.STUDENT.getRoleCode().equalsIgnoreCase(roleRequest.getRequestedRole())) {
		    Optional<Student> existingStudentOpt = studentRepository.findByRegisteredUser(requestedBy);
		    if (existingStudentOpt.isPresent()) {
		        throw new CustomException("Student already exists as this user");
		    }
		    Student newStudent = new Student();
		    newStudent.setRegisteredUser(requestedBy);
		    studentRepository.save(newStudent);
		    logger.info("registered a new student");	    	
	    } if(SystemRoles.MANAGER.getRoleCode().equalsIgnoreCase(roleRequest.getRequestedRole())) {
	    	Optional<Manager> existingManagerOpt = managerRepository.findByRegisteredUser(requestedBy);
	    	
		    if (existingManagerOpt.isPresent()) {
		        throw new CustomException("Manager already exists as this user");
		    }
		    Manager newManager = new Manager();
		    newManager.setRegisteredUser(requestedBy);
		    managerRepository.save(newManager);
		    logger.info("registered a new manager");
	    }
	   
	    return roleRequestRepository.save(roleRequest); 
	}

	@Override
	public List<RoleRequest> getAllRoleRequests() {
		return roleRequestRepository.findAll();
	}

}
