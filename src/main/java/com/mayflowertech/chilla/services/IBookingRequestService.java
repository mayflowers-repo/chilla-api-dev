package com.mayflowertech.chilla.services;

import java.util.Date;
import java.util.List;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.enums.BookingStatus;

public interface IBookingRequestService {
	BookingRequest createBookingRequest(BookingRequest bookingRequest) throws CustomException;
	BookingRequest updateBookingRequest(Long id, BookingRequest bookingRequest) throws CustomException;	
	List<BookingRequest> getAllBookingRequests();
	public List<BookingRequest> listBookingRequestsByStudent(Long studentId) throws CustomException;
	public List<BookingRequest> listBookingRequestsByCustomer(Long customerId) throws CustomException;
	
	BookingRequest getBookingRequestById(Long id) throws CustomException;	
    List<BookingRequest> getBookingRequestsByStatus(String status) throws CustomException;
    List<BookingRequest> getBookingRequestsCreatedBetween(Date startDate, Date endDate) throws CustomException;
}
