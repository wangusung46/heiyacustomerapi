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
public class XenditOvoCreatePaymentDTORequest {
	
	/* Xendit request body */
	
	@JsonProperty("external_id")
	private String externalId; //this should be HW OrderNo
	
	@JsonProperty("amount")
	private BigDecimal amount;
	
	@JsonProperty("phone")
	private String phone;
	
	@JsonProperty("ewallet_type")
	private String ewalletType;
}
