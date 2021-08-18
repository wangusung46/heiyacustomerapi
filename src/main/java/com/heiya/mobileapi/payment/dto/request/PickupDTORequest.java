package com.heiya.mobileapi.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PickupDTORequest {
	
	@JsonProperty("orderNo")
	private String orderNo;
	
	@JsonProperty("machineCode")
	private String machineCode;
}
