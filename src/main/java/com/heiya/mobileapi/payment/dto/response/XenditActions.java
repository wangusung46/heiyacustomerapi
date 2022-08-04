package com.heiya.mobileapi.payment.dto.response;

import com.heiya.mobileapi.payment.dto.request.*;
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
public class XenditActions {
	
	/* Xendit request body */
	
	@JsonProperty("desktop_web_checkout_url")
	private String desktopWebCheckoutUrl;
        
        @JsonProperty("mobile_web_checkout_url")
        private String mobileWebCheckoutUrl;
        
        @JsonProperty("mobile_deeplink_checkout_url")
        private String mobileDeeplinkCheckoutUrl;
        
        @JsonProperty("qr_checkout_string")
        private String qrCheckoutString;
	
}
