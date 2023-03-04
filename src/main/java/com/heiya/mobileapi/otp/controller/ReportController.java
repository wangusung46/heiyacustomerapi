package com.heiya.mobileapi.otp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heiya.mobileapi.otp.service.ReportService;
import com.heiya.mobileapi.otp.service.SendEmailService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/send-email")
@Configuration
@EnableScheduling
public class ReportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private SendEmailService emailService;

    @Value("${spring.mail.username}")
    private String mailFrom;

    //@Value("${mail.to}")
    //private String mailTo;

    //@Value("${mail.subject}")
    //private String mailSubject;

    //@Scheduled(cron = "${cron.expression}")
    @ApiOperation("SEND EMAIL Undeliver")
    @PostMapping(value = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doReport(String orderNo) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ReportController.doReport with Order Number " + orderNo);
        try {
            String content = reportService.export(orderNo).toString();
            //String[] tos = mailTo.split(",");
            //for (String to : tos) {
                emailService.sendSimpleMessage(mailFrom, content);
                LOGGER.info("======== COMPLETED ReportController.doReport");
            //}
            LOGGER.info("======== COMPLETED ReportController.doReport");
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
