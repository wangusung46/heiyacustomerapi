package com.heiya.mobileapi.customer.dto.request;

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
public class CustomerRegistrationDTORequest {
	
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
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("isActive")
	private String isActive;
	
	@JsonProperty("isLoggedIn")
	private String isLoggedIn;
}
