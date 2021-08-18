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
public class XenditCallbackDTORequest {
	
	@JsonProperty("event")
	private String event;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("external_id")
	private String externalId;
	
	@JsonProperty("business_id")
	private String businessId;
	
	@JsonProperty("phone")
	private String phone;
	
	@JsonProperty("ewallet_type")
	private String ewalletType;
	
	@JsonProperty("amount")
	private String amount;
	
	@JsonProperty("created")
	private String created;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("failure_code")
	private String failureCode;
}
