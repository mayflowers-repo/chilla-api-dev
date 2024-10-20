package com.mayflowertech.chilla.services.impl;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.services.IMailService;

@Service
public class MailService implements IMailService{
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    private static final String FROM_EMAIL = "ananniachilla@gmail.com";
    private static final String FROM_NAME = "Karuthal Team";

    private final MailjetClient mailjetClient;
    
    private Map<String, OtpDetails> otpStorage = new ConcurrentHashMap<>();

    private static final int OTP_VALIDITY_DURATION_MINUTES = 10; 

    public MailService() {
        mailjetClient = new MailjetClient("4a1eb0e49b7fa9af762fdca14b0d116b", "11a3def575afbd137fdbfc8fca4cd6ef", new ClientOptions("v3.1"));

    }

    @Override
    public String generateOtp(String email) {
    	logger.info("generating OTP for "+email);
        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpStorage.put(email, new OtpDetails(otp, LocalDateTime.now()));
        logger.info(otpStorage+"");
        return otp;
    }

    @Override
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
        OtpDetails otpDetails = otpStorage.get(email);

        if (otpDetails == null) {
            throw new CustomException("No OTP found for the provided email.");
        }

        // Check if the OTP has expired
        if (otpDetails.getTimestamp().plusMinutes(OTP_VALIDITY_DURATION_MINUTES).isBefore(LocalDateTime.now())) {
            otpStorage.remove(email);  // Remove expired OTP
            throw new CustomException("OTP has expired.");
        }

        // Check if the OTP matches
        if (!otpDetails.getOtp().equals(otp)) {
            throw new CustomException("Invalid OTP.");
        }

        // If OTP is valid, remove it from storage and return true
        otpStorage.remove(email);
        return true;
    }


    // Method to generate the verification link (can be JWT or UUID based)
    public String generateVerificationLink(String email) {
        // Generate a unique token (JWT or UUID)
        String token = "generated-unique-token"; // Replace with your JWT or UUID logic
        return "https://yourdomain.com/verify-email?token=" + token;
    }
    
    private class OtpDetails {
        private String otp;
        private LocalDateTime timestamp;

        public OtpDetails(String otp, LocalDateTime timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}
