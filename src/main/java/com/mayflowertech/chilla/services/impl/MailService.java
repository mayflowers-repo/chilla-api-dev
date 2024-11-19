package com.mayflowertech.chilla.services.impl;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.EmailOtpRequest;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.enums.MailOtpPurpose;
import com.mayflowertech.chilla.enums.OtpStatus;
import com.mayflowertech.chilla.repositories.IOtpRepository;
import com.mayflowertech.chilla.repositories.IUserRepository;
import com.mayflowertech.chilla.services.IMailService;

@Service
public class MailService implements IMailService{
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private static final String FROM_EMAIL = "ananniachilla@gmail.com";
    private static final String FROM_NAME = "Karuthal Team";

    private final MailjetClient mailjetClient;
    

    private static final int OTP_VALIDITY_DURATION_MINUTES = 10; 

    
	@Autowired
	private IOtpRepository otpRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
    public MailService() {
        mailjetClient = new MailjetClient("4a1eb0e49b7fa9af762fdca14b0d116b", "11a3def575afbd137fdbfc8fca4cd6ef", new ClientOptions("v3.1"));

    }

    @Override
    public String generateAndSendOtp(String email, String purpose) throws CustomException {
    	//String otp = String.valueOf(100000 + new Random().nextInt(900000));
    	String otp = "123456";
		try {
			

			User user = userRepository.findByEmail(email);
			if(user == null) {
				throw new CustomException("User does not exist with email "+email);
			}
			if(MailOtpPurpose.EMAIL_VERIFICATION.getCode().equalsIgnoreCase(purpose)){
				sendVerificationEmail(email, user.getFirstName()+" "+user.getLastName(), otp, null);
			}else if(MailOtpPurpose.PASSWORD_CHANGE.getCode().equalsIgnoreCase(purpose)){
				sendChangePasswordOTP(email, user.getFirstName()+" "+user.getLastName(), otp, null);
			}
			
			// Create an OTP request and save it using the OtpService
			EmailOtpRequest otpRequest = new EmailOtpRequest();
			otpRequest.setEmail(email);
			otpRequest.setOtp(otp);
			otpRequest.setCreatedAt(LocalDateTime.now());
			otpRequest.setStatus(OtpStatus.SENT.getCode());

			// Save OTP request through OtpService
			otpRequest = otpRepository.save(otpRequest);
		} catch (CustomException e) {
			EmailOtpRequest otpRequest = new EmailOtpRequest();
			otpRequest.setEmail(email);
			otpRequest.setOtp(otp);
			otpRequest.setCreatedAt(LocalDateTime.now());
			otpRequest.setStatus(OtpStatus.FAILED.getCode());
			otpRequest = otpRepository.save(otpRequest);
			throw new CustomException(e.getMessage());
		}
        
        return otp;
    }
    
    

    
    public void sendVerificationEmail(String toEmail, String toName, String otp, String verificationLink) throws CustomException {
    	logger.info(otp+" sendVerificationEmail: "+toEmail);
        try {
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", FROM_EMAIL)
                                            .put("Name", FROM_NAME))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", toEmail)
                                                    .put("Name", toName)))
                                    .put(Emailv31.Message.TEMPLATEID, 6352835)
                                    .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                    .put(Emailv31.Message.SUBJECT, "Verify Your Email with Anannia")
                                    .put(Emailv31.Message.VARIABLES, new JSONObject()
                                            .put("name", toName)
                                            .put("otp", otp)
                                            .put("verificationLink", verificationLink))));

            // Send the request
            MailjetResponse response = mailjetClient.post(request);

            if (response.getStatus() != 200) {
                throw new CustomException("Failed to send email: " + response.getStatus());
            }

            logger.info("Status: " + response.getStatus());
            logger.info("Data: " + response.getData());

        } catch (MailjetException | MailjetSocketTimeoutException e) {
            // Handle Mailjet-specific exceptions
            throw new CustomException("Error sending email: " + e.getMessage(), e);
        } catch (JSONException e) {
            // Handle JSON exceptions
            throw new CustomException("Error building email content: " + e.getMessage(), e);
        } catch (Exception e) {
            // Catch any other exceptions
            throw new CustomException("Unexpected error occurred: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean verifyOtp(String email, String otp) throws CustomException {
    	 Optional<EmailOtpRequest> otpRequestOpt = otpRepository.findTopByEmailOrderByCreatedAtDesc(email);
    	 
    	   email = email.toLowerCase();
    	   // Check if the OTP request is present
    	    EmailOtpRequest otpRequest = otpRequestOpt.orElseThrow(() -> 
    	        new CustomException("No OTP found for the provided email.")
    	    );

    	    // Check if the OTP matches and is not expired
    	    if (!otpRequest.getOtp().equalsIgnoreCase(otp)) {
    	        throw new CustomException("Invalid OTP.");
    	    }
    	    
    	    // Check if the OTP is expired
    	    if (otpRequest.isExpired(OTP_VALIDITY_DURATION_MINUTES)) {
    	        throw new CustomException("OTP has expired.");
    	    }

    	    // If OTP is valid, update the request and return true
    	    otpRequest.setVerifiedAt(LocalDateTime.now());
    	    otpRequest.setStatus(OtpStatus.VERIFIED.getCode());
    	    otpRepository.save(otpRequest);
    	    
    	    return true;
    }


    // Method to generate the verification link (can be JWT or UUID based)
    public String generateVerificationLink(String email) {
        // Generate a unique token (JWT or UUID)
        String token = "generated-unique-token"; // Replace with your JWT or UUID logic
        return "https://yourdomain.com/verify-email?token=" + token;
    }
    
    

	
	public void sendChangePasswordOTP(String toEmail, String toName, String otp, String link) throws CustomException {
		logger.info(otp+" sendChangePasswordOTP: "+toEmail);
        try {
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", FROM_EMAIL)
                                            .put("Name", FROM_NAME))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", toEmail)
                                                    .put("Name", toName)))
                                    .put(Emailv31.Message.TEMPLATEID, 6430875)
                                    .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                                    .put(Emailv31.Message.SUBJECT, "Verify Your Email with Anannia")
                                    .put(Emailv31.Message.VARIABLES, new JSONObject()
                                            .put("name", toName)
                                            .put("otp", otp)
                                            .put("verificationLink", link))));

            // Send the request
            MailjetResponse response = mailjetClient.post(request);

            if (response.getStatus() != 200) {
                throw new CustomException("Failed to send email: " + response.getStatus());
            }else {
            	logger.error("MailjetResponse  not 200 "+response.getStatus());
            }

            logger.info("Status: " + response.getStatus());
            logger.info("Data: " + response.getData());

        } catch (MailjetException | MailjetSocketTimeoutException e) {
            // Handle Mailjet-specific exceptions
            throw new CustomException("Error sending email: " + e.getMessage(), e);
        } catch (JSONException e) {
            // Handle JSON exceptions
            throw new CustomException("Error building email content: " + e.getMessage(), e);
        } catch (Exception e) {
            // Catch any other exceptions
            throw new CustomException("Unexpected error occurred: " + e.getMessage(), e);
        }
		
	}

	@Override
	public boolean checkIfPresentEmailAndStatus(String email, String status) {
		Optional<EmailOtpRequest> otpRequestOpt = otpRepository.findTopByEmailAndStatusOrderByCreatedAtDesc(email, status);
        return otpRequestOpt.isPresent();
	}
}
