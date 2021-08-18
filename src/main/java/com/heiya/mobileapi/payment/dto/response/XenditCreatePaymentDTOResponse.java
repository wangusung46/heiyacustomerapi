package com.heiya.mobileapi.payment.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heiya.mobileapi.dto.response.BaseResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class XenditCreatePaymentDTOResponse extends BaseResponse {
	
	@JsonProperty("amount")
	private BigDecimal amount;
	
	@JsonProperty("business_id")
	private String businessId;
	
	@JsonProperty("created")
	private String created;
	
	@JsonProperty("ewallet_type")
	private String ewalletType;
	
	@JsonProperty("external_id")
	private String externalId;
	
	@JsonProperty("phone")
	private String phone;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("checkout_url")
	private String checkoutUrl; //only for DANA
	
	@JsonProperty("error_code")
	private String errorCode;
	
	@JsonProperty("message")
	private String message;
}
