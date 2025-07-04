package com.heiya.mobileapi.payment.dto.request;

import java.util.List;

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
public class HeiyaGeneralCallbackDTORequest {
	
	/*OVO PART*/
//	@JsonProperty("oreder_no")
//	private String oreder_no;
//	
//	@JsonProperty("id")
//	private String id;
	
	@JsonProperty("external_id")
	private String externalId;
	
//	@JsonProperty("business_id")
//	private String businessId;
//	
//	@JsonProperty("phone")
//	private String phone;
//	
//	@JsonProperty("ewallet_type")
//	private String ewalletType;
//	
//	@JsonProperty("amount")
//	private String amount;
//	
	@JsonProperty("created")
	private String created;
//	
//	@JsonProperty("status")
//	private String status;
//	
//	@JsonProperty("failure_code")
//	private String failureCode;
//	
//	/*ADDITIONAL FIELDS FOR DANA*/
//	@JsonProperty("payment_status")
//	private String paymentStatus;
//	
//	@JsonProperty("transaction_date")
//	private String transactionDate;
//	
//	@JsonProperty("callback_authentication_token")
//	private String callbackAuthenticationToken;
//	
//	/*ADDITIONAL FIELDS FOR LINKAJA*/
//	@JsonProperty("items")
//	private List<Items> items;
}
