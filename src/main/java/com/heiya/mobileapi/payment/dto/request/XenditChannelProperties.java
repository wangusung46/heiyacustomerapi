package com.heiya.mobileapi.payment.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class XenditChannelProperties {
	
	/* Xendit request body */
	
	@JsonProperty("success_redirect_url")
	private String successRedirectUrl;
        
        @JsonProperty("mobile_number")
        private String mobileNumber;
//        
        @JsonProperty("failure_redirect_url")
        private String failureRedirectUrl;
	
}
