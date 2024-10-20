package com.mayflowertech.chilla.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.PersonalizedService;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.StudentPojo;
import com.mayflowertech.chilla.repositories.ICollegeRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;
import com.mayflowertech.chilla.repositories.IpServiceRepository;
import com.mayflowertech.chilla.services.IStudentService;
import com.mayflowertech.chilla.services.IUserService;

@Service
public class StudentService implements IStudentService {
	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
	
	
    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private ICollegeRepository collegeRepository;
    
    @Autowired
    private IpServiceRepository serviceRepository;
    
    

    @Autowired
    private IUserService userService;
    
    
	@Override
	public Student createStudent(Student student) {
		if(! collegeRepository.existsById(student.getCollege().getId())) {
			collegeRepository.save(student.getCollege());
		}
		student = studentRepository.save(student);
		return student;
	}

	@Override
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Student updateStudentServices(Long studentId, List<PersonalizedService> services) throws CustomException {
		logger.info(" updateStudentServices "+services);
	    // Fetch the student by ID
	    Student student = studentRepository.findById(studentId)
	            .orElseThrow(() -> new CustomException("Student not found"));

	    // Extract the IDs from the provided services list
	    List<Long> serviceIds = services.stream()
	            .map(PersonalizedService::getId)
	            .collect(Collectors.toList());

	    // Fetch services from the repository using the IDs
	    List<PersonalizedService> existingServices = serviceRepository.findAllById(serviceIds);

	    // Check if the number of found services matches the provided service list
	    if (existingServices.size() != services.size()) {
	        // Identify the missing services
	        List<Long> foundIds = existingServices.stream()
	                .map(PersonalizedService::getId)
	                .collect(Collectors.toList());

	        List<Long> notFoundIds = serviceIds.stream()
	                .filter(id -> !foundIds.contains(id))
	                .collect(Collectors.toList());

	        throw new CustomException("Service(s) not found for IDs: " + notFoundIds);
	    }

	    // Update the student's offered services
	    student.setOfferedServices(existingServices);

	    // Log the update process
	    logger.info(studentId + ": updating student services with service IDs " + serviceIds);

	    // Save and return the updated student
	    return studentRepository.save(student);
	}

	

	@Override
	public Student getStudentDetails(Long studentId) {
		 return studentRepository.findById(studentId).orElse(null);
	}

	@Override
	public List<Patient> getPatientsAssignedToStudent(Long studentId) throws CustomException {
		logger.info("getPatientsAssignedToStudent  "+studentId);
		Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException("Student not found with ID: " + studentId));
		logger.info(student+"");
        // Assuming that each student has a collection of BookingRequests
        // and each BookingRequest has a list of patients
        List<Patient> assignedPatients = new ArrayList<>();
        for (BookingRequest request : student.getAssignedBookingRequests()) {
            assignedPatients.addAll(request.getRequestedFor());
        }
        return assignedPatients;
	}

	 @Override
	    public Student updateStudent(Long studentId, StudentPojo studentPojo) throws CustomException {
	        Student existingStudent = getStudentDetails(studentId);
	        if (existingStudent == null) {
	            throw new CustomException("Student not found");
	        }

	        // Update the fields from StudentPojo to Student
	        existingStudent.setAge(studentPojo.getAge());
	        existingStudent.setCourse(studentPojo.getCourse());
	        existingStudent.setCompletionYear(studentPojo.getCompletionYear());
	        existingStudent.setGender(studentPojo.getGender());
	       
	        
	        // Update registered user details
	        User registeredUser = existingStudent.getRegisteredUser();
	        registeredUser.setFirstName(studentPojo.getFirstName());
	        registeredUser.setLastName(studentPojo.getLastName());

	        // Save updated student
	        existingStudent = studentRepository.save(existingStudent);
	        logger.info("saved student details");
	        if(studentPojo.getServices() != null) {
	        	updateStudentServices(studentId, studentPojo.getServices());
	        }
	        
	        userService.markUserAsRegistered(registeredUser.getEmail());
	        
	        return existingStudent;
	    }

}
