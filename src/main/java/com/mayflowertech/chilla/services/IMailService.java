package com.mayflowertech.chilla.services;

import com.mayflowertech.chilla.config.custom.CustomException;

public interface IMailService {
	public void sendVerificationEmail(String toEmail, String toName, String otp, String verificationLink) throws CustomException;
	 String generateOtp(String email); 
	 
	 boolean verifyOtp(String email, String otp) throws CustomException;
	 
}
