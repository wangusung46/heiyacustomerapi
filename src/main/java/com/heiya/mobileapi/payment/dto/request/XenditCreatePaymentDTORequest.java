package com.heiya.mobileapi.payment.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class XenditCreatePaymentDTORequest {
	
	/* Xendit request body */
	
	@JsonProperty("reference_id")
	private String referenceId;
	
	@JsonProperty("currency")
	private String currency;
	
	@JsonProperty("amount")
	private BigDecimal amount;
	
	@JsonProperty("checkout_method")
	private String checkoutMethod;
        
        @JsonProperty("channel_code")
	private String channelCode;
        
        @JsonProperty("channel_properties")
	private XenditChannelProperties channelProperties;
        
        @JsonProperty("metadata")
	private XenditMetadata metadata;
}
