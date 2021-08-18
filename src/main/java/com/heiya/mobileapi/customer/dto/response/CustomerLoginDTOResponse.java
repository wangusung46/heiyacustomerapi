package com.heiya.mobileapi.customer.dto.response;

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
public class CustomerLoginDTOResponse extends BaseResponse {
	
	@JsonProperty("isLoggedIn")
	private String isLoggedIn;
	
	@JsonProperty("customerId")
	private long customerId;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("mobileNo")
	private String mobileNo;
}
