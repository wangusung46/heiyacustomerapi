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
public class CustomerDetailDTOResponse extends BaseResponse {
	
	@JsonProperty("customerId")
	private Long customerId;
	
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("gender")
	private String gender;
	
	@JsonProperty("dob")
	private String dob;
	
	@JsonProperty("mobileNo")
	private String mobileNo;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("isActive")
	private String isActive;
	
	@JsonProperty("isLoggedIn")
	private String isLoggedIn;
}
