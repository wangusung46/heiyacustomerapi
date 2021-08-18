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
public class TransactionDetails {
	
	@JsonProperty("order_id")
	private String orderId;
	
	@JsonProperty("gross_amount")
	private String grossAmount;
}
