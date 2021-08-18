package com.heiya.mobileapi.product.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MachineInfoDTOResponse {
	
	@JsonProperty("machine_code")
	private String machineCode;
	
	@JsonProperty("lat")
	private double latitude;
	
	@JsonProperty("long")
	private double longitude;
	
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("operate1")
	private String operate1;
	
	@JsonProperty("model_id")
	private int modelId;
	
	@JsonProperty("onlineTime")
	private String onlineTime;
	
	@JsonProperty("online")
	private int online;
	
	@JsonProperty("status")
	private int status;
}
