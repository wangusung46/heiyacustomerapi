package com.heiya.mobileapi.payment.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HWPickupOrderDTORequest {
	
	@JsonProperty("order_no")
	private String orderNo;
	
	@JsonProperty("order_type")
	private String orderType;
	
	@JsonProperty("operate_code")
	private String operateCode;
	
	@JsonProperty("payment_time")
	private String paymentTime;
	
	@JsonProperty("original_amount")
	private String originalAmount;
	
	@JsonProperty("deposit_amount")
	private String depositAmount;
	
	@JsonProperty("payment_amount")
	private String paymentAmount;
	
	@JsonProperty("machine_code")
	private String machineCode;
	
	@JsonProperty("order_source")
	private String orderSource;
	
	@JsonProperty("detail")
	private List<PickupOrderDetailRequest> detail;
}
