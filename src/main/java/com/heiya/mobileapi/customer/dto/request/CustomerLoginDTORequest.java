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
public class CustomerLoginDTORequest {
	
	@JsonProperty("mobileNo")
	private String mobileNo;
	
	@JsonProperty("password")
	private String password;
        
        @JsonProperty("id_token")
	private String idToken;
}
