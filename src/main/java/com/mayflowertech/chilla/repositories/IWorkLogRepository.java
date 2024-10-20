package com.mayflowertech.chilla.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.WorkLog;

public interface IWorkLogRepository extends JpaRepository<WorkLog, Long> {
	 List<WorkLog> findByWorkStartTimeBetween(LocalDateTime start, LocalDateTime end);
	  List<WorkLog> findByStudent_StudentId(Long studentId);
	  WorkLog findFirstByStudent_StudentIdAndWorkEndTimeIsNullOrderByWorkStartTimeDesc(Long studentId);
}
