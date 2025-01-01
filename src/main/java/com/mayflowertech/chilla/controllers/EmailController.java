package com.mayflowertech.chilla.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.pojo.EmailVerifyPojo;
import com.mayflowertech.chilla.enums.MailOtpPurpose;
import com.mayflowertech.chilla.services.IMailService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/email")
public class EmailController {
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
	
	@Autowired
	private IMailService mailService;

	
	@Deprecated
	@ApiOperation(value = "Send a verification email")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully sent verification email"),
	    @ApiResponse(code = 500, message = "An unexpected error occurred")
	})
	@RequestMapping(value = "/sendotp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<String> sendVerificationEmail(@RequestBody EmailVerifyPojo emailRequest) {
	    try {
	        // Generate OTP
	        String otp = mailService.generateAndSendOtp(emailRequest.getEmail(), MailOtpPurpose.EMAIL_VERIFICATION.getCode());
	        
	        // Log and return success message
	        logger.info("Verification email sent to " + emailRequest.getEmail());
	        return new ApiResult<>(HttpStatus.OK.value(), "Verification email sent successfully", "Email sent");

	    } catch (CustomException e) {
	        // Handle custom exceptions
	        logger.error("Error sending email to " + emailRequest.getEmail(), e);
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error sending verification email: " + e.getMessage(), null);
	    } catch (Exception e) {
	        // Catch all other exceptions
	        logger.error("Unexpected error while sending email", e);
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}

	
	@ApiOperation(value = "Send OTP for password change")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully sent OTP to email"),
	    @ApiResponse(code = 500, message = "An unexpected error occurred")
	})
	@RequestMapping(value = "/sendpasswordchangeotp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<String> sendOtpEmailForPasswordChange(@RequestBody EmailVerifyPojo emailRequest) {
	    try {
	        // Generate OTP
	        String otp = mailService.generateAndSendOtp(emailRequest.getEmail(), MailOtpPurpose.PASSWORD_CHANGE.getCode());
	        
	        // Log and return success message
	        logger.info("password change otp email sent to " + emailRequest.getEmail());
	        return new ApiResult<>(HttpStatus.OK.value(), "Change password OTP  sent successfully", "Email sent");

	    } catch (CustomException e) {
	        // Handle custom exceptions
	        logger.error("Error sending email to " + emailRequest.getEmail(), e);
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error sending OTP email: " + e.getMessage(), null);
	    } catch (Exception e) {
	        // Catch all other exceptions
	        logger.error("Unexpected error while sending email", e);
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}
	
	
	@ApiOperation(value = "Verify OTP for email")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OTP verified successfully"),
	    @ApiResponse(code = 400, message = "Invalid or expired OTP"),
	    @ApiResponse(code = 500, message = "An unexpected error occurred")
	})
	@RequestMapping(value = "/verify-otp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<String> verifyOtp(@RequestBody EmailVerifyPojo otpVerificationRequest) {
	    try {
	    	logger.info("verify-otp controller "+otpVerificationRequest.getEmail());
	        boolean isValid = mailService.verifyOtp(otpVerificationRequest.getEmail(), otpVerificationRequest.getOtp(), 
	        		otpVerificationRequest.getPurpose());
	        if (isValid) {
	            return new ApiResult<>(HttpStatus.OK.value(), "OTP verified successfully", "Verification successful");
	        } else {
	            return new ApiResult<>(HttpStatus.BAD_REQUEST.value(), "Invalid or expired OTP", null);
	        }
	    } catch (CustomException e) {
	        // Handle custom exceptions with user-friendly messages
	        logger.error("Custom error occurred while verifying OTP for email: " + otpVerificationRequest.getEmail(), e);
	        return new ApiResult<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
	    } catch (Exception e) {
	        // Handle any unexpected exceptions
	        logger.error("Unexpected error verifying OTP for email: " + otpVerificationRequest.getEmail(), e);
	        return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
	    }
	}

    
}
