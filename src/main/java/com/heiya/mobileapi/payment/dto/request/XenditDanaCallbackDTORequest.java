package com.heiya.mobileapi.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Dian Krisnanjaya
 *
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class XenditDanaCallbackDTORequest {
	
	@JsonProperty("external_id")
	private String externalId;
	
	@JsonProperty("amount")
	private String amount;
	
	@JsonProperty("business_id")
	private String businessId;
	
	@JsonProperty("payment_status")
	private String paymentStatus;
	
	@JsonProperty("transaction_date")
	private String transactionDate;
	
	@JsonProperty("ewallet_type")
	private String ewalletType;
	
	@JsonProperty("callback_authentication_token")
	private String callbackAuthenticationToken;
}
