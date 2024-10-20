package com.mayflowertech.chilla.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.ManagerPojo;
import com.mayflowertech.chilla.entities.pojo.StudentPojo;
import com.mayflowertech.chilla.enums.BookingStatus;
import com.mayflowertech.chilla.repositories.IBookingRequestsRepository;
import com.mayflowertech.chilla.repositories.IManagerRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;
import com.mayflowertech.chilla.services.IManagerService;

@Service
public class ManagerService implements IManagerService {
	private static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

	@Autowired
	private IBookingRequestsRepository bookingRequestRepository;

	@Autowired
	private IManagerRepository managerRepository;

	@Autowired
	private IStudentRepository studentRepository;

	@Override
	public BookingRequest assignStudentsToBookingRequest(Long managerId, Long bookingRequestId, List<Long> studentIds)
			throws CustomException {

		// Fetch the manager
		Optional<Manager> managerOpt = managerRepository.findById(managerId);
		if (!managerOpt.isPresent()) {
			throw new CustomException("Manager not found with ID: " + managerId);
		}
		Manager manager = managerOpt.get();

		// Fetch the booking request
		Optional<BookingRequest> bookingRequestOpt = bookingRequestRepository.findById(bookingRequestId);
		if (!bookingRequestOpt.isPresent()) {
			throw new CustomException("Booking Request not found with ID: " + bookingRequestId);
		}
		BookingRequest bookingRequest = bookingRequestOpt.get();

		// Fetch the students by their IDs
		List<Student> students = studentRepository.findAllById(studentIds);
		if (students.isEmpty() || students.size() != studentIds.size()) {
			throw new CustomException("Some students not found with provided IDs");
		}

		// Assign the students to the booking request
		bookingRequest.setAssignedStudents(students);

		// Assign the manager as the one who made the assignment
		bookingRequest.setAssignedByManager(manager);

		bookingRequest.setStatus(BookingStatus.ASSIGNED.getCode());

		// Save the updated booking request
		BookingRequest updatedBookingRequest = bookingRequestRepository.save(bookingRequest);

		return updatedBookingRequest;
	}

	@Override
	public Manager getManagerDetails(Long managerId) {
		return managerRepository.findById(managerId).orElse(null);
	}

	@Override
	public Manager updateManager(Long managerId, ManagerPojo managerPojo) throws CustomException {
		Manager existingManager = getManagerDetails(managerId);
		if (existingManager == null) {
			throw new CustomException("Manager not found");
		}
		User registeredUser = existingManager.getRegisteredUser();
		registeredUser.setFirstName(managerPojo.getFirstName());
		registeredUser.setLastName(managerPojo.getLastName());

		return managerRepository.save(existingManager);
	}

}
