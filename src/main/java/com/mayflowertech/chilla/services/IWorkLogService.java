package com.mayflowertech.chilla.services;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.WorkLog;

public interface IWorkLogService {
	public WorkLog startWorkLog(WorkLog workLog) throws CustomException;
	public WorkLog getLatestUnfinishedWorkLog(Long studentId) throws CustomException;
	public WorkLog createWorkLog(WorkLog workLog) throws CustomException;
	public WorkLog completeWork(Long logId) throws CustomException;
	public Duration getWorkDuration(Long logId) throws CustomException;
	public WorkLog getWorkLog(Long logId) throws CustomException;
	public List<WorkLog> getWorkLogsByDate(LocalDate date) throws CustomException;
	public List<WorkLog> getWorkLogsByStudentId(Long studentId) throws CustomException;
}
