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
public class PickupOrderDetailRequest {
	
	@JsonProperty("tasteid")
	private String tasteid;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("goodid")
	private String goodid;
	
	@JsonProperty("orderGoodsNo")
	private String orderGoodsNo;
	
	@JsonProperty("brewingCode")
	private String brewingCode;
	
	@JsonProperty("orderPrice")
	private String orderPrice;
}
