package com.mayflowertech.chilla.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.JacksonFilterConfig;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.BookingRequestAssignPojo;
import com.mayflowertech.chilla.entities.pojo.EmailVerifyPojo;
import com.mayflowertech.chilla.entities.pojo.ManagerPojo;
import com.mayflowertech.chilla.entities.pojo.PatientPojo;
import com.mayflowertech.chilla.entities.pojo.StudentPojo;
import com.mayflowertech.chilla.enums.MailOtpPurpose;
import com.mayflowertech.chilla.services.IMailService;
import com.mayflowertech.chilla.services.IManagerService;
import com.mayflowertech.chilla.services.IPersonaService;
import com.mayflowertech.chilla.services.IStudentService;
import com.mayflowertech.chilla.services.IUserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/staff")
public class StaffController {
	private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

	@Autowired
	private JacksonFilterConfig jacksonFilterConfig;

	@Autowired
	private IPersonaService personaService;

	@Autowired
	private IStudentService studentService;

	@Autowired
	private IManagerService managerService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IMailService mailService;

	@Secured({ "ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN" })
	@RequestMapping(value = "/students", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<Student>> getStudents(Model model) {
		try {
			logger.info("GET students list");
			List<Student> students = personaService.listStudents();

			jacksonFilterConfig.applyFilters("UserFilter", "username", "email");
			jacksonFilterConfig.applyFilters("StudentFilter", "studentId", "registeredUser");
			// Return success response with the list of students
			return new ApiResult<>(HttpStatus.OK.value(), "Successfully retrieved students list", students);

		} catch (Exception ex) {
			logger.error("Error: An unexpected error occurred while retrieving students list", ex);
			// Return error response for internal server error
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
		} finally {
			jacksonFilterConfig.clearFilters();
		}
	}

	@RequestMapping(value = "/updatestudentservices", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<StudentPojo> updateStudentServices(@RequestBody StudentPojo studentPojo) {
		logger.info("updateStudentServices");
		try {
			// Call the service layer to update the student's offered services
			Student updatedStudent = studentService.updateStudentServices(studentPojo.getStudentId(),
					studentPojo.getServices());

			return new ApiResult<>(HttpStatus.OK.value(), "Student services updated successfully", studentPojo);

		} catch (CustomException e) {
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), studentPojo);

		} catch (Exception e) {
			e.printStackTrace();
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred",
					studentPojo);
		}
	}

	@RequestMapping(value = "students/{studentId}", method = RequestMethod.GET, produces = "application/json")
	public ApiResult<StudentPojo> getStudentById(@PathVariable Long studentId) {
		logger.info("fetching details for student " + studentId);
		try {
			jacksonFilterConfig.applyFilters("UserFilter", "id", "username", "email", "photoUrl");
			Student student = studentService.getStudentDetails(studentId);
			StudentPojo result = new StudentPojo();
			if (student == null) {
				return new ApiResult<>(HttpStatus.NOT_FOUND.value(), "Student not found", null);
			}

			result.setStudentId(student.getStudentId());
			result.setRegisteredUser(student.getRegisteredUser());
			result.setAge(student.getAge());
			result.setCourse(student.getCourse());
			result.setCompletionYear(student.getCompletionYear());
			result.setGender(student.getGender());
			result.setCollege(student.getCollege());
			result.setServices(student.getOfferedServices());
			result.setEmail(student.getRegisteredUser().getEmail());
			result.setFirstName(student.getRegisteredUser().getFirstName());
			result.setLastName(student.getRegisteredUser().getLastName());
			return new ApiResult<StudentPojo>(HttpStatus.OK.value(), "Student details retrieved successfully", result);

		} catch (Exception e) {
			// Handle unexpected exceptions
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
		} finally {
			jacksonFilterConfig.clearFilters();
		}
	}

	@RequestMapping(value = "students/{studentId}", method = RequestMethod.PUT, produces = "application/json")
	public ApiResult<StudentPojo> updateStudentById(@PathVariable Long studentId,
			@RequestBody StudentPojo studentPojo) {
		logger.info("Updating details for student " + studentId);
		try {
			// Call the service method to update the student
			Student updatedStudentEntity = studentService.updateStudent(studentId, studentPojo);

			if (updatedStudentEntity == null) {
				return new ApiResult<>(HttpStatus.NOT_FOUND.value(), "Student not found", null);
			}

			// Prepare the response by converting the updated Student entity to StudentPojo
			StudentPojo result = new StudentPojo();
			result.setStudentId(updatedStudentEntity.getStudentId());
			result.setRegisteredUser(updatedStudentEntity.getRegisteredUser());
			result.setAge(updatedStudentEntity.getAge());
			result.setCourse(updatedStudentEntity.getCourse());
			result.setCompletionYear(updatedStudentEntity.getCompletionYear());
			result.setGender(updatedStudentEntity.getGender());
			result.setCollege(updatedStudentEntity.getCollege());
			result.setServices(updatedStudentEntity.getOfferedServices());
			result.setEmail(updatedStudentEntity.getRegisteredUser().getEmail());
			result.setFirstName(updatedStudentEntity.getRegisteredUser().getFirstName());
			result.setLastName(updatedStudentEntity.getRegisteredUser().getLastName());

			return new ApiResult<>(HttpStatus.OK.value(), "Student details updated successfully", result);

		} catch (CustomException e) {
			// Handle custom exception cases (e.g., student not found)
			logger.error("Error updating student: " + e.getMessage());
			return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
		} catch (Exception e) {
			// Handle any other unexpected exceptions
			logger.error("An error occurred while updating student", e);
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
		}
	}

	@Secured({ "ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN" })
	@RequestMapping(value = "managers/{managerId}", method = RequestMethod.PUT, produces = "application/json")
	public ApiResult<ManagerPojo> updateManagerById(@PathVariable Long managerId,
			@RequestBody ManagerPojo managerPojo) {
		logger.info("Updating details for manager " + managerId);
		try {
			// Call the service method to update the manager
			Manager updatedManagerEntity = managerService.updateManager(managerId, managerPojo);

			if (updatedManagerEntity == null) {
				return new ApiResult<>(HttpStatus.NOT_FOUND.value(), "Manager not found", null);
			}

			// Prepare the response by converting the updated Manager entity to ManagerPojo
			ManagerPojo result = new ManagerPojo();
			result.setManagerId(updatedManagerEntity.getManagerId());
			result.setRegisteredUser(updatedManagerEntity.getRegisteredUser());
			result.setFirstName(updatedManagerEntity.getRegisteredUser().getFirstName());
			result.setLastName(updatedManagerEntity.getRegisteredUser().getLastName());
			result.setEmail(updatedManagerEntity.getRegisteredUser().getEmail());
			return new ApiResult<>(HttpStatus.OK.value(), "Manager details updated successfully", result);

		} catch (CustomException e) {
			// Handle custom exception cases (e.g., manager not found)
			logger.error("Error updating manager: " + e.getMessage());
			return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
		} catch (Exception e) {
			// Handle any other unexpected exceptions
			logger.error("An error occurred while updating manager", e);
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
		}
	}

	@Secured({ "ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN" })
	@RequestMapping(value = "managers/{managerId}", method = RequestMethod.GET, produces = "application/json")
	public ApiResult<ManagerPojo> getManagerById(@PathVariable Long managerId) {
		logger.info("fetching details for manager " + managerId);
		try {
			Manager manager = managerService.getManagerDetails(managerId);
			ManagerPojo result = new ManagerPojo();
			if (manager == null) {
				return new ApiResult<>(HttpStatus.NOT_FOUND.value(), "Manager not found", null);
			}

			result.setManagerId(manager.getManagerId());
			result.setRegisteredUser(manager.getRegisteredUser());
			result.setEmail(manager.getRegisteredUser().getEmail());
			result.setFirstName(manager.getRegisteredUser().getFirstName());
			result.setLastName(manager.getRegisteredUser().getLastName());
			return new ApiResult<ManagerPojo>(HttpStatus.OK.value(), "Manager details retrieved successfully", result);

		} catch (Exception e) {
			// Handle unexpected exceptions
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
		}
	}

	@Secured({ "ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SYSTEMADMIN" })
	@RequestMapping(value = "/assignstudents", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<BookingRequestAssignPojo> assignStudentsToBookingRequest(
			@RequestBody BookingRequestAssignPojo bookingRequestPojo) {
		logger.info("assignstudents " + bookingRequestPojo);
		try {
			// Call the service layer to assign the students to the booking request
			BookingRequest updatedBookingRequest = managerService.assignStudentsToBookingRequest(
					bookingRequestPojo.getManagerId(), bookingRequestPojo.getBookingRequestId(),
					bookingRequestPojo.getStudentIds());

			return new ApiResult<>(HttpStatus.OK.value(), "Students assigned to booking request successfully",
					bookingRequestPojo);

		} catch (CustomException e) {
			return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), bookingRequestPojo);

		} catch (Exception e) {
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred",
					bookingRequestPojo);
		}
	}

	@RequestMapping(value = "/patientsforstudent/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<PatientPojo>> getPatientsAssignedToStudent(@PathVariable Long studentId) {

		try {
			// Call the service to get the list of patients assigned to the student
			List<Patient> patients = studentService.getPatientsAssignedToStudent(studentId);
			// Convert entities to Pojo or DTO
			List<PatientPojo> patientPojos = patients.stream().map(patient -> new PatientPojo(patient))
					.collect(Collectors.toList());

			return new ApiResult<>(HttpStatus.OK.value(), "Patients fetched successfully", patientPojos);

		} catch (CustomException e) {
			return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);

		} catch (Exception e) {
			e.printStackTrace();
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
		}
	}

	@RequestMapping(value = "/initiatechangepassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<String> initiateChangePassword(@RequestBody EmailVerifyPojo emailVerifyPojo) {
		logger.info("Initiating change password process for email: {}", emailVerifyPojo.getEmail());

		try {
			// Check if the user exists for the provided email
			User user = userService.getByEmail(emailVerifyPojo.getEmail());
			if (user == null) {
				return new ApiResult<>(HttpStatus.NOT_FOUND.value(), "Email not found", null);
			}

			// Generate and send OTP
			String otp = mailService.generateAndSendOtp(emailVerifyPojo.getEmail(),
					MailOtpPurpose.PASSWORD_CHANGE.getCode());

			logger.info("OTP sent successfully to email: {}", emailVerifyPojo.getEmail());
			return new ApiResult<>(HttpStatus.OK.value(), "OTP sent successfully", null);
		} catch (CustomException e) {
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
		} catch (Exception e) {
			logger.error("Error initiating change password for email: {}", emailVerifyPojo.getEmail(), e);
			return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
		}
	}

}
