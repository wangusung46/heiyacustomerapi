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
public class MachineDetailDTOResponse {
	
	@JsonProperty("id")
	private long id;
	
	@JsonProperty("machineCode")
	private String machineCode;
	
	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("longitude")
	private Double longitude;
	
	@JsonProperty("location")
	private String location;
	
	@JsonProperty("operate1")
	private String operate1;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("modelId")
	private Integer modelId;
	
	@JsonProperty("onlineTime")
	private String onlineTime;
	
	@JsonProperty("online")
	private Integer online;
	
	@JsonProperty("status")
	private Integer status;
	
	@JsonProperty("userId")
	private Long userId;
	
	@JsonProperty("channelType")
	private String channelType;
}
