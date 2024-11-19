package com.mayflowertech.chilla.services;

import com.mayflowertech.chilla.config.custom.CustomException;

public interface IMailService {
	 //public void sendVerificationEmail(String toEmail, String toName, String otp, String verificationLink) throws CustomException;
	 String generateAndSendOtp(String email, String purpose) throws CustomException; 
	 
	 boolean verifyOtp(String email, String otp) throws CustomException;
	 //public void sendChangePasswordOTP(String toEmail, String toName, String otp, String link) throws CustomException;
	 
	 boolean checkIfPresentEmailAndStatus(String email, String status);
}
