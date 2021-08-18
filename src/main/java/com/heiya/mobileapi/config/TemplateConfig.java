package com.heiya.mobileapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TemplateConfig {

	@Bean("paymentRestTemplate")
	protected  RestTemplate restTemplate() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
		
	}
}
