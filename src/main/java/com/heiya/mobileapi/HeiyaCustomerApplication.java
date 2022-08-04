package com.heiya.mobileapi;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import com.heiya.mobileapi.config.SchedulerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Import({SchedulerConfig.class})
@SpringBootApplication
public class HeiyaCustomerApplication extends SpringBootServletInitializer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HeiyaCustomerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HeiyaCustomerApplication.class, args);
        LOGGER.info(System.getProperty("java.home"));
//                System.out.println(System.getProperty("java.home"));
    }

    @PostConstruct
    void started() {
        /*
         *  This is to configure UTC to use (force) current system Timezone for both Java & Database.
         */
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
