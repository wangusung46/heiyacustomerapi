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
public class CustomerChangeDTORequest {
	
	@JsonProperty("phoneNo")
	private String phoneNo;
        
        @JsonProperty("orderId")
	private String orderId;
        
        @JsonProperty("friendNo")
	private String friendNo;
	
}
