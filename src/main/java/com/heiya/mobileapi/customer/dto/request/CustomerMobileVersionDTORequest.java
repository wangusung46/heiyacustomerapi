package com.heiya.mobileapi.customer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.mobile.device.Device;

/**
 * @author Dian Krisnanjaya
 *
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomerMobileVersionDTORequest {
	
	@JsonProperty("device")
	private Device device;
	
	@JsonProperty("version")
	private String version;
	
}
