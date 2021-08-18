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
public class GopayTrxStatusDTOResponse {
	
	@JsonProperty("status_code")
	private String statusCode;
	
	@JsonProperty("status_message")
	private String statusMessage;
	
	@JsonProperty("transaction_id")
	private String transactionId;
	
	@JsonProperty("order_id")
	private String orderId;
	
	@JsonProperty("gross_amount")
	private String grossAmount;
	
	@JsonProperty("currency")
	private String currency;
	
	@JsonProperty("payment_type")
	private String paymentType;
	
	@JsonProperty("transaction_time")
	private String transactionTime;
	
	@JsonProperty("transaction_status")
	private String transactionStatus;
	
	@JsonProperty("settlement_time")
	private String settlementTime;
	
	@JsonProperty("merchant_id")
	private String merchantId;
	
	@JsonProperty("signature_key")
	private String signatureKey;
	
	@JsonProperty("fraud_status")
	private String fraudStatus;
}
