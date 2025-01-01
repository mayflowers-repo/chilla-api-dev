package com.mayflowertech.chilla.services;

import java.util.List;
import java.util.UUID;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.CustomerPojo;
import com.mayflowertech.chilla.entities.pojo.ManagerPojo;
import com.mayflowertech.chilla.entities.pojo.PatientPojo;
import com.mayflowertech.chilla.entities.pojo.ResetPasswordPojo;
import com.mayflowertech.chilla.entities.pojo.StudentPojo;

public interface IUserService {
	User createUser(User user) throws Exception, Throwable;
	User updateUser(User user, boolean changepassword);
	boolean validateUser(User user);
	User getUser(String username);
	User getById(String id);
	User getByEmail(String email);
	User getByMobile(String mobile);
	boolean isExist(User user);	
	public User addRoletoUser(User user, Role role);
	boolean hasRole(User user, Role role);	
	public List<User> getAllActiveUsers();
	public List<User> getAllUsers();
	public User getCurrentLoggedInUser();
	User removeRolefromUser(User user, Role role);
	String generatePasswordResetLink(User user, String uri);
	User getUserFromSignature(String signature);
    User checkSocialUser(User user) throws CustomException;
    
    public Customer registerCustomer(CustomerPojo customer) throws Throwable;
    public Student registerStudent(StudentPojo student) throws Throwable;
    public Patient enrollPatient(PatientPojo patient) throws Throwable;
    public Manager registerManager(ManagerPojo manager) throws Throwable;
    public Long getManagerId(User user) throws CustomException;
    public Long getCustomerId(User user) throws CustomException;
    public Long getStudentId(User user) throws CustomException;
    public User updateUserStatus(UUID id, String newStatus) throws CustomException;
    User changePassword(ResetPasswordPojo pojo) throws CustomException;
    User changePasswordInitial(ResetPasswordPojo pojo) throws CustomException;
    User markUserAsRegistered(String email) throws CustomException;
}
