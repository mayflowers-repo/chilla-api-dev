package com.mayflowertech.chilla.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.Patient;
import com.mayflowertech.chilla.entities.PersonalizedService;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.enums.BookingStatus;
import com.mayflowertech.chilla.repositories.IBookingRequestsRepository;
import com.mayflowertech.chilla.repositories.ICustomerRepository;
import com.mayflowertech.chilla.repositories.IPatientRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;
import com.mayflowertech.chilla.repositories.IpServiceRepository;
import com.mayflowertech.chilla.services.IBookingRequestService;

@Service
public class BookingRequestService implements IBookingRequestService {

	@Autowired
	private IBookingRequestsRepository bookingRequestsRepository;

	@Autowired
	private ICustomerRepository customerRepository;
	
	@Autowired
	private IStudentRepository studentRepository;
	
	
	@Autowired
	private IPatientRepository patientRepository;
	
	@Autowired
	private IpServiceRepository serviceRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(BookingRequestService.class);
	
	@Override
	public BookingRequest createBookingRequest(BookingRequest bookingRequest) throws CustomException{
		bookingRequest.setStatus(BookingStatus.PENDING.getCode());
		
		Optional<Customer> enrolledByCustomerOptional = customerRepository.findById(bookingRequest.getEnrolledByCustomer().getCustomerId());
		if (enrolledByCustomerOptional.isPresent()) {
		    Customer enrolledByCustomer = enrolledByCustomerOptional.get();
			logger.info("found the parent customer : "+enrolledByCustomer.getCustomerId());
			bookingRequest.setEnrolledByCustomer(enrolledByCustomer);
		} else {
			logger.error("parent customer not found "+bookingRequest.getEnrolledByCustomer());
		    throw new CustomException("Customer not found with id " + bookingRequest.getEnrolledByCustomer().getCustomerId());
		}
		
		List<PersonalizedService> personalizedServices = bookingRequest.getRequestedServices();
		for(PersonalizedService ps : personalizedServices) {
			Optional<PersonalizedService> serviceOptional =  serviceRepository.findById(ps.getId());
			if(serviceOptional.isEmpty()) {
				throw new CustomException("Service with ID " + ps.getId() + " not found.");
			}
		}
		
		 List<Patient> patients = bookingRequest.getRequestedFor();
		 for(Patient patient: patients) {
				Optional<Patient> patientOptional = patientRepository.findById(patient.getPatientId());	
				  if (patientOptional.isEmpty()) {
				        throw new CustomException("Patient with ID " + patient.getPatientId() + " not found.");
				  }
		 }
		
		 
		 bookingRequest.setStatus(BookingStatus.ASSIGNED.getCode());
		 
		logger.info("saving booking request");
		return bookingRequestsRepository.save(bookingRequest);
	}

	@Override
	public BookingRequest updateBookingRequest(Long id, BookingRequest bookingRequestDetails) throws CustomException{
		 BookingRequest existingRequest = bookingRequestsRepository.findById(id)
		            .orElseThrow(() -> new CustomException("BookingRequest not found for this id :: " + id));
	        // Update fields
	        existingRequest.setRequestedServices(bookingRequestDetails.getRequestedServices());
	        existingRequest.setPreferredGender(bookingRequestDetails.getPreferredGender());
	        
	        //TODO this can be problematic
	        existingRequest.setRequestedFor(bookingRequestDetails.getRequestedFor());
	        existingRequest.setStatus(bookingRequestDetails.getStatus()); 	        
	        // Save updated BookingRequest
	        return bookingRequestsRepository.save(existingRequest);
	}

	@Override
	public List<BookingRequest> getAllBookingRequests() {
		return bookingRequestsRepository.findAll();
	}

	@Override
	public BookingRequest getBookingRequestById(Long id) throws CustomException {
		 return bookingRequestsRepository.findById(id)
	                .orElseThrow(() -> new CustomException("Booking Request not found for this id :: " + id));
	}

	@Override
	public List<BookingRequest> getBookingRequestsByStatus(BookingStatus status) throws CustomException {
		 return bookingRequestsRepository.findByStatus(status);
	}

	@Override
	public List<BookingRequest> getBookingRequestsCreatedBetween(Date startDate, Date endDate) throws CustomException {
		return bookingRequestsRepository.findByCreatedOnBetween(startDate, endDate);
	}

	@Override
	public List<BookingRequest> listBookingRequestsByStudent(Long studentId) throws CustomException {
	    // Fetch student by ID
	    Optional<Student> studentOptional = studentRepository.findById(studentId);
	    if (studentOptional.isEmpty()) {
	        throw new CustomException("Student not found with ID: " + studentId);
	    }
	    
	    List<BookingRequest> bookingRequests = bookingRequestsRepository.findByAssignedStudentsContaining(studentOptional.get());
	    if (bookingRequests.isEmpty()) {
	        throw new CustomException("No booking requests found for Student ID: " + studentId);
	    }
	    
	    return bookingRequests;
	}

	@Override
	public List<BookingRequest> listBookingRequestsByCustomer(Long customerId) throws CustomException {
	    // Fetch customer by ID
	    Optional<Customer> customerOptional = customerRepository.findById(customerId);
	    if (customerOptional.isEmpty()) {
	        throw new CustomException("Customer not found with ID: " + customerId);
	    }
	    
	    // Fetch booking requests for the customer
	    List<BookingRequest> bookingRequests = bookingRequestsRepository.findByEnrolledByCustomer(customerOptional.get());
	    if (bookingRequests.isEmpty()) {
	        throw new CustomException("No booking requests found for Customer ID: " + customerId);
	    }
	    
	    return bookingRequests;
	}

}
