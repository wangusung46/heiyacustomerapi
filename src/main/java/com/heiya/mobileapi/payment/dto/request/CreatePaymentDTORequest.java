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
public class CreatePaymentDTORequest {
	
	@JsonProperty("external_id")
	private String externalId;
	
	@JsonProperty("amount")
	private BigDecimal amount;
	
	@JsonProperty("phone")
	private String phone; //ini sebagai ID customer
	
	@JsonProperty("ewallet_type")
	private String ewalletType;
	
	@JsonProperty("goodsId")
	private int goodsId;
	
	@JsonProperty("tasteId")
	private int tasteId;
	
	@JsonProperty("goodsProtocol")
	private int goodsProtocol;
	
	@JsonProperty("productName")
	private String productName;
	
	@JsonProperty("machineCode")
	private String machineCode;
}
