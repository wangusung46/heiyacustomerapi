package com.heiya.mobileapi.communication.service;

import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	
	@Override
	public void sendMessage(String to, String subject, String message) throws GeneralSecurityException {
		 SimpleMailMessage simpleMailMessage = new SimpleMailMessage(); 
		 simpleMailMessage.setTo(to); 
		 simpleMailMessage.setSubject(subject); 
		 simpleMailMessage.setText(message);

		 LOGGER.info("\n======== START EmailServiceImpl.sendOtpMessage() SUBJECT : " + subject);
		 LOGGER.info("======== START EmailServiceImpl.sendOtpMessage() TO : " + to);
		 LOGGER.info("======== START EmailServiceImpl.sendOtpMessage() MESSAGE : " + message);

		 //Uncomment to send mail
		 javaMailSender.send(simpleMailMessage);
	}
}
