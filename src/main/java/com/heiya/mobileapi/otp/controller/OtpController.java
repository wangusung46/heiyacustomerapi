/**
 * 
 */
package com.heiya.mobileapi.otp.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heiya.mobileapi.communication.dto.response.SendSmsDTOResponse;
import com.heiya.mobileapi.communication.service.EmailService;
import com.heiya.mobileapi.communication.service.SmsService;
import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.database.service.CRUDService;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.otp.service.OtpService;

import io.swagger.annotations.ApiOperation;

/**
 * @author Dian Krisnanjaya
 *
 */

@RestController
@RequestMapping("/v1/otp")
public class OtpController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OtpController.class);

	@Autowired
	public OtpService otpService;

	@Autowired
	public EmailService myEmailService;
	
	@Autowired
	public SmsService mySmsService;
	
	@Autowired
	private CRUDService crudService;
	
	
	@ApiOperation("OTP API - Generate OTP and send it through SMS / Email")
	@GetMapping(value = "/generateOtp/{phoneNo}/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> generateOtp(@RequestParam("phoneNo") String phoneNo, @RequestParam("email") String email) throws Exception {
		LOGGER.info("\n\n======== START OtpController.generateOtp to recipient : "+ phoneNo + " or " + email);
		ResponseEntity<?> response = null;
		
		String defaultGateway = crudService.getGlobalConfigParamByKey(GlobalConstants.OTP_DEFAULT_GW);
		
		if (defaultGateway.contentEquals("sms")) {
			response = this.generateSmsOtp(phoneNo);
		}
		else if (defaultGateway.contentEquals("email")) {
			response = this.generateEmailOtp(email);
		}
	
		return response;
	}
	
	
	@ApiOperation("OTP API - Generate OTP and send it through SMS")
	@GetMapping(value = "/generateSmsOtp/{phoneNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> generateSmsOtp(@RequestParam("phoneNo") String phoneNo) throws Exception {
		LOGGER.info("\n\n======== START OtpController.generateSmsOtp to phone no : "+ phoneNo);
		SendSmsDTOResponse response = new SendSmsDTOResponse();
		
		if (phoneNo != null && !phoneNo.isEmpty()) {
			//Validator
			String regexPhoneNo = "^(\\d){10,15}$";
			Pattern regexPattern = Pattern.compile(regexPhoneNo);
			Matcher matcher = regexPattern.matcher(phoneNo);
			
			if (!matcher.matches()) {
				//Response
				response.setSuccess(false);
				response.setResultCode("412");
				response.setResultMsg("Invalid phone no format or length");
				LOGGER.info("\n======== COMPLETED OtpController.generateSmsOtp - Phone no ("+phoneNo+") is invalid");
				return ResponseEntity.ok(response);
			}
			
			if (phoneNo.trim().startsWith("0")) {
				phoneNo = phoneNo.substring(1);
				phoneNo = "62".concat(phoneNo); //Mitracomm requires country code
			}
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
			String username = auth.getName();
		
			int otp = otpService.generateEmailOTP(username);
		
			LOGGER.info("\n======== OTP : "+otp);
			
			String message = "Your Heiya Registration OTP code is "+otp+". Please keep it confidential.";
		
			response = mySmsService.sendSMS(phoneNo, message);
			
			LOGGER.info("\n======== COMPLETED OtpController.generateSmsOtp - OTP sent");
		}
		else {
			//Response
			response.setSuccess(false);
			response.setResultCode("400");
			response.setResultMsg("Phone no cannot be empty");
			LOGGER.info("\n======== COMPLETED OtpController.generateSmsOtp - Phone no ("+phoneNo+") is empty/null");
		}
	
		return ResponseEntity.ok(response);
	}
	
	
	@ApiOperation("OTP API - Generate OTP and send it through email")
	@GetMapping(value = "/generateEmailOtp/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> generateEmailOtp(@RequestParam("email") String email) throws Exception {
		BaseResponse response = new BaseResponse();
		EmailValidator emailValidator = EmailValidator.getInstance();
		
		if (email != null && emailValidator.isValid(email)) {
			LOGGER.info("\n\n======== START OtpController.generateEmailOtp to email : "+email);
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
			String username = auth.getName();
		
			int otp = otpService.generateEmailOTP(username);
		
			LOGGER.info("\n======== OTP : "+otp);
		
			//Generate The Template to send OTP 
			/*EmailTemplate template = new EmailTemplate("SendOtp.html");
		
			Map<String,String> replacements = new HashMap<String,String>();
			replacements.put("user", username);
			replacements.put("otpnum", String.valueOf(otp));
		
			String message = template.getTemplate(replacements);*/
			String message = "Dear Customer,\n\nYour OTP code is "+otp+"\n\nThank You,\nHeiya";
		
			myEmailService.sendMessage(email, "Your Heiya Registration OTP", message);
			
			LOGGER.info("\n======== COMPLETED OtpController.generateEmailOtp - OTP sent");
			
			//Response
			response.setSuccess(true);
			response.setResultCode("200");
			response.setResultMsg("Email OTP sent successfully");
		}
		else {
			//Response
			response.setSuccess(false);
			response.setResultCode("400");
			response.setResultMsg("Invalid email address");
			LOGGER.info("\n======== COMPLETED OtpController.generateEmailOtp - Recipient email ("+email+") is invalid");
		}
	
		return ResponseEntity.ok(response);
	}
	
	
	@ApiOperation("OTP API - Validate the OTP entered by user")
	@RequestMapping(value ="/validateOtp/{otpnum}", method = RequestMethod.GET)
	public ResponseEntity<?> validateOtp(@RequestParam("otpnum") int otpnum) throws Exception{
		LOGGER.info("\n\n======== START OtpController.validateOtp");
		BaseResponse response = new BaseResponse();
		
		final String SUCCESS = "Entered Otp is valid";
	
		final String FAIL = "Entered Otp is NOT valid. Please Retry!";
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		String username = auth.getName();
	
		LOGGER.info(" Otp Number : "+otpnum);
	
		//Validate the OTP
		if(otpnum >= 0) {
		int serverOtp = otpService.getEmailOtp(username);
			if(serverOtp > 0){
				if(otpnum == serverOtp) {
					otpService.clearEmailOtp(username);
					//Response
					response.setSuccess(true);
					response.setResultCode("200");
					response.setResultMsg(SUCCESS);
					LOGGER.info("\n======== COMPLETED OtpController.validateOtp - Entered Otp is valid");
					return ResponseEntity.ok(response);
				}
				else {
					//Response
					response.setSuccess(false);
					response.setResultCode("400");
					response.setResultMsg(FAIL);
					return ResponseEntity.ok(response);
				}
			}
			else {
				//Response
				response.setSuccess(false);
				response.setResultCode("400");
				response.setResultMsg(FAIL);
				return ResponseEntity.ok(response);
			}
		}
		else {
			//Response
			response.setSuccess(false);
			response.setResultCode("400");
			response.setResultMsg(FAIL);
			return ResponseEntity.ok(response);
		}
	}
}
