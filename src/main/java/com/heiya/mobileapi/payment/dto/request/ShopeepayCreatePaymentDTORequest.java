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
public class ShopeepayCreatePaymentDTORequest {
	
	/* Shopeepay request body */
	
	@JsonProperty("payment_type")
	private String paymentType;
	
	@JsonProperty("transaction_details")
	private TransactionDetails transactionDetails;
        
//        @JsonProperty("items")
//	private Items items;
//        
//        @JsonProperty("customer_detail")
//	private CustomerDetail customerDetail;
	
	@JsonProperty("shopeepay")
	private ShopeePay shopeepay;
}
