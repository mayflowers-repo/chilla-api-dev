package com.mayflowertech.chilla.services;

import java.util.List;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Manager;
import com.mayflowertech.chilla.entities.pojo.ManagerPojo;

public interface IManagerService {
	 /**
     * Assigns a list of students to a booking request by a manager.
     * 
     * @param managerId The ID of the manager making the assignment.
     * @param bookingRequestId The ID of the booking request.
     * @param studentIds The list of student IDs to assign.
     * @return The updated BookingRequest entity.
     * @throws CustomException If the booking request, manager, or students are not found.
     */
    BookingRequest assignStudentsToBookingRequest(Long managerId, Long bookingRequestId, List<Long> studentIds) throws CustomException;

    
    Manager getManagerDetails(Long managerId);
    
    Manager updateManager(Long managerId, ManagerPojo managerPojo) throws CustomException;
}
