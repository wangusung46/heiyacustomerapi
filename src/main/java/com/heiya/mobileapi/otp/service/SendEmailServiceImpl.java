package com.heiya.mobileapi.otp.service;

import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.database.service.CRUDService;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SendEmailServiceImpl implements SendEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailServiceImpl.class);

    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private CRUDService crudService;

    @Override
    public void sendSimpleMessage(String from, String text) throws MessagingException {
        LOGGER.info("======== START EmailServiceImpl.sendSimpleMessage");
        String mailTo = crudService.getGlobalConfigParamByKey(GlobalConstants.EMAIL_TO);
        String subject = crudService.getGlobalConfigParamByKey(GlobalConstants.SUBJECT);
        String[] tos = mailTo.split(",");
        for (String to : tos) {
            LOGGER.info("======== START EmailServiceImpl.sendSimpleMessage From     : " + from);
            LOGGER.info("======== START EmailServiceImpl.sendSimpleMessage To       : " + to);
            LOGGER.info("======== START EmailServiceImpl.sendSimpleMessage Subject  : " + subject);
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(text, true);
            emailSender.send(message.getMimeMessage());
            LOGGER.info("======== COMPLETE EmailServiceImpl.sendSimpleMessage");
        }
    }
}
