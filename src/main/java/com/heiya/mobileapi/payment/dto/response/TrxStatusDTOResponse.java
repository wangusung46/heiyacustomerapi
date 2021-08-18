package com.heiya.mobileapi.payment.dto.response;

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
public class TrxStatusDTOResponse extends BaseResponse {
	
	@JsonProperty("amount")
	private String amount;
	
	@JsonProperty("business_id")
	private String businessId;
	
	@JsonProperty("ewallet_type")
	private String ewalletType;
	
	@JsonProperty("external_id")
	private String externalId;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("transaction_date")
	private String transactionDate; //only OVO & GOPAY
	
	@JsonProperty("payment_timestamp")
	private String paymentTimestamp; //only LINKAJA
	
	@JsonProperty("expiration_date")
	private String expirationDate; //only DANA
	
	@JsonProperty("checkout_url")
	private String checkoutUrl; //only DANA
	
	private String reason;
}
