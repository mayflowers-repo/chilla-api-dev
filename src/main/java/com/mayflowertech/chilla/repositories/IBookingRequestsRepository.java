package com.mayflowertech.chilla.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Customer;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.enums.BookingStatus;

public interface IBookingRequestsRepository extends JpaRepository<BookingRequest, Long> {
	List<BookingRequest> findByEnrolledByCustomer(Customer customer);
	List<BookingRequest> findByCreatedOnBetween(Date startDate, Date endDate);
	List<BookingRequest> findByStatus(BookingStatus status);
	
	 List<BookingRequest> findByAssignedStudentsContaining(Student student);
}
