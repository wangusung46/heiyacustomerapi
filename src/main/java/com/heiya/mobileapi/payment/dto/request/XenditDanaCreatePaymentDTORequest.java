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
public class XenditDanaCreatePaymentDTORequest {
	
	/* Xendit request body */
	
	@JsonProperty("external_id")
	private String externalId; //this should be HW OrderNo
	
	@JsonProperty("amount")
	private BigDecimal amount;
	
	@JsonProperty("expiration_date")
	private String expirationDate;
	
	@JsonProperty("callback_url")
	private String callbackUrl;
	
	@JsonProperty("redirect_url")
	private String redirectUrl;
	
	@JsonProperty("ewallet_type")
	private String ewalletType;
}
