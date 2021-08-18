package com.heiya.mobileapi.payment.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class XenditLinkAjaCallbackDTORequest {
	
	@JsonProperty("external_id")
	private String externalId; //this should be HW OrderNo
	
	@JsonProperty("amount")
	private BigDecimal amount;
	
	@JsonProperty("items")
	private List<Items> items;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("ewallet_type")
	private String ewalletType;
	
	@JsonProperty("callback_authentication_token")
	private String callbackAuthenticationToken;
}
