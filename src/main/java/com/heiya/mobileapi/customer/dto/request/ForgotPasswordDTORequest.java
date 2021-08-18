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
public class ForgotPasswordDTORequest {
	
	@JsonProperty("phoneNo")
	private String phoneNo;
	
	@JsonProperty("newPassword")
	private String newPassword;
	
	@JsonProperty("confirmNewPassword")
	private String confirmNewPassword;
}
