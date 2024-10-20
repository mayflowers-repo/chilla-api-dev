package com.mayflowertech.chilla.services;

import java.util.List;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.PersonalizedService;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.pojo.StudentPojo;

public interface IStudentService {
	Student createStudent(Student student);
	List<Student> getAllStudents();
	
	Student updateStudentServices(Long studentId, List<PersonalizedService> services) throws CustomException;
	
	 
	 Student getStudentDetails(Long studentId);
	 List<Patient> getPatientsAssignedToStudent(Long studentId) throws CustomException;
	 
	 Student updateStudent(Long studentId, StudentPojo studentPojo) throws CustomException;
}
