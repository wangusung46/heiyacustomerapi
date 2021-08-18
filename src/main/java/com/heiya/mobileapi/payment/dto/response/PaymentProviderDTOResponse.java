package com.heiya.mobileapi.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PaymentProviderDTOResponse {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("providerName")
	private String providerName;
	
	@JsonProperty("apiEndpoint")
	private String apiEndpoint;
	
	@JsonProperty("isActive")
	private String isActive;
}
