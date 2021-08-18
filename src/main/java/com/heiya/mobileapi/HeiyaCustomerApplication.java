package com.heiya.mobileapi;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import com.heiya.mobileapi.config.SchedulerConfig;

@Import({SchedulerConfig.class})
@SpringBootApplication
public class HeiyaCustomerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HeiyaCustomerApplication.class, args);
    }

    @PostConstruct
    void started() {
        /*
         *  This is to configure UTC to use (force) current system Timezone for both Java & Database.
         */
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
