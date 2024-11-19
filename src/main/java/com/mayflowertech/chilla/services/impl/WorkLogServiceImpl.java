package com.mayflowertech.chilla.services.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.BookingRequest;
import com.mayflowertech.chilla.entities.Student;
import com.mayflowertech.chilla.entities.WorkLog;
import com.mayflowertech.chilla.repositories.IPatientRepository;
import com.mayflowertech.chilla.repositories.IStudentRepository;
import com.mayflowertech.chilla.repositories.IWorkLogRepository;
import com.mayflowertech.chilla.services.IBookingRequestService;
import com.mayflowertech.chilla.services.IWorkLogService;

@Service
public class WorkLogServiceImpl implements IWorkLogService {
	private static final Logger logger = LoggerFactory.getLogger(WorkLogServiceImpl.class);
	
    @Autowired
    private IWorkLogRepository workLogRepository;

    @Autowired
    private IStudentRepository studentRepository;
    
    @Autowired
    private IPatientRepository patientRepository;
    
    
    @Autowired
    private IBookingRequestService bookingRequestService;
  
    
	@Override
	public WorkLog createWorkLog(WorkLog workLog) throws CustomException{
		logger.info("service: createWorkLog");
		if (workLog.getStudent() == null || workLog.getStudent().getStudentId() == null) {
	        throw new CustomException("Student information is missing or invalid");
	    }
	    Optional<Student> studentOptional = studentRepository.findById(workLog.getStudent().getStudentId());
	    if (!studentOptional.isPresent()) {
	        throw new CustomException("Student not found for id: " + workLog.getStudent().getStudentId());
	    }
	    workLog.setStudent(studentOptional.get());
	    
	    if (workLog.getBookingRequest() == null ) {
	        throw new CustomException("Booking Request information is mandatory");
	    }else {
	    	Long bookingRequestId = workLog.getBookingRequest().getId();
	    	BookingRequest br = bookingRequestService.getBookingRequestById(bookingRequestId);
	    	if(br == null) {
	    		throw new CustomException("Booking Request is invalid");
	    	}
	    	workLog.setBookingRequest(br);
	    }
	    
	    workLog.setWorkStartTime(LocalDateTime.now());
	    /*
	    if (workLog.getWorkStartTime() == null) {
	    	workLog.setWorkStartTime(LocalDateTime.now());
	    }else {
		    if (workLog.getWorkStartTime().isAfter(LocalDateTime.now())) {
		        throw new CustomException("Work start time cannot be in the future");
		    }	    	
	    }
	    */
	    
	    
	    
	    if (workLog.getWorkEndTime() != null) {
	        // Ensure workEndTime is not in the future
	        if (workLog.getWorkEndTime().isAfter(LocalDateTime.now())) {
	            throw new CustomException("Work end time cannot be in the future");
	        }

	        // Ensure workStartTime is before workEndTime
	        if (workLog.getWorkStartTime().isAfter(workLog.getWorkEndTime())) {
	            throw new CustomException("Work start time cannot be later than work end time");
	        }

	        // Calculate duration if both start and end times are valid
	        calculateDuration(workLog);
	    }
	    
        return workLogRepository.save(workLog);
	}
	

	@Override
	public WorkLog startWorkLog(WorkLog workLog) throws CustomException{
	    workLog.setWorkStartTime(LocalDateTime.now());
	    workLog.setWorkEndTime(null);	    
        return workLogRepository.save(workLog);
	}


	@Override
	public WorkLog completeWork(Long logId) throws CustomException {
		logger.info("service: completeWork");
        Optional<WorkLog> workLogOptional = workLogRepository.findById(logId);

        if (workLogOptional.isPresent()) {
            WorkLog workLog = workLogOptional.get();
            
            if(workLog.isWorkCompleted()) {
                throw new CustomException("Work log already marked completed for id: " + logId);            	
            }
            // Set the workEndTime and calculate duration
            workLog.setWorkEndTime(LocalDateTime.now());
            calculateDuration(workLog);  // Calculate the duration based on start and end time

            // Save the updated work log with workEndTime
            return workLogRepository.save(workLog);
        } else {
            throw new CustomException("Work log not found for id: " + logId);
        }
	}

	@Override
	public Duration getWorkDuration(Long logId) throws CustomException{
        Optional<WorkLog> workLogOptional = workLogRepository.findById(logId);

        if (workLogOptional.isPresent()) {
            WorkLog workLog = workLogOptional.get();

            if (workLog.getWorkStartTime() != null && workLog.getWorkEndTime() != null) {
                return Duration.between(workLog.getWorkStartTime(), workLog.getWorkEndTime());
            } else {
                throw new CustomException("Work log does not have both start and end times.");
            }
        } else {
            throw new CustomException("Work log not found for id: " + logId);
        }
    }

	@Override
    public WorkLog getWorkLog(Long logId) throws CustomException{
        return workLogRepository.findById(logId)
            .orElseThrow(() -> new CustomException("Work log not found for id: " + logId));
    }
	
	private Duration calculateDuration(WorkLog workLog) {
	    if (workLog.getWorkStartTime() != null && workLog.getWorkEndTime() != null) {
	        return Duration.between(workLog.getWorkStartTime(), workLog.getWorkEndTime());
	    }
	    return Duration.ZERO; // Return zero if duration cannot be calculated
   }

	
	@Override
	public List<WorkLog> getWorkLogsByDate(LocalDate date) throws CustomException {
		logger.info("service: getWorkLogsByDate");
	    // Assuming you have a WorkLogRepository to fetch data from the database
	    return workLogRepository.findByWorkStartTimeBetween(
	        date.atStartOfDay(), date.plusDays(1).atStartOfDay()
	    );
	}

	@Override
	public List<WorkLog> getWorkLogsByStudentId(Long studentId) throws CustomException {
		try {
            return workLogRepository.findByStudent_StudentId(studentId);
        } catch (Exception e) {
            throw new CustomException("Error fetching work logs for studentId: " + studentId, e);
        }
	}


	@Override
	public WorkLog getLatestUnfinishedWorkLog(Long studentId) throws CustomException {
        if (!studentRepository.existsById(studentId)) {
            throw new CustomException("Student with ID " + studentId + " does not exist.");
        }

        try {
            // Fetch the latest work log where the workEndTime is null (indicating it is unfinished)
            WorkLog unfinishedWorkLog = workLogRepository.findFirstByStudent_StudentIdAndWorkEndTimeIsNullOrderByWorkStartTimeDesc(studentId);
            if (unfinishedWorkLog == null) {
                throw new CustomException("No unfinished work log found for student ID: " + studentId);
            }
            return unfinishedWorkLog; // Return the found unfinished work log
        } catch (Exception e) {
            throw new CustomException("Error fetching latest unfinished work log for student ID: " + studentId, e);
        }
    }
	
	
}
