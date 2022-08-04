package com.heiya.mobileapi.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heiya.mobileapi.dto.response.BaseResponse;
import javax.persistence.Column;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderDTOResponse {
	
	@JsonProperty("paymentDate")
	private String paymentDate;
	
	@JsonProperty("paymentStatus")
	private String paymentStatus;
	
	@JsonProperty("orderNo")
	private String orderNo;
	
	@JsonProperty("productName")
	private String productName;
	
	/*@JsonProperty("originalPrice")
	private String originalPrice; //get dari product table
	
	@JsonProperty("discount")
	private String discount;*/
	
	@JsonProperty("price")
	private String price;
	
	@JsonProperty("machineCode")
	private String machineCode;
	
	@JsonProperty("machineLocation")
	private String machineLocation;
	
	@JsonProperty("eWalletType")
	private String eWalletType;
	
	@JsonProperty("expiryTime")
	private String expiryTime;
        
        @JsonProperty("id")
	private Long id;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("mobileNo")
	private String mobileNo;
}
