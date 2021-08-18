package com.heiya.mobileapi.product.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HWProductDTOResponse {
	
	@JsonProperty("coffee")
	private List<CoffeeDTOResponse> coffee;
	
	@JsonProperty("machineInfo")
	private List<MachineInfoDTOResponse> machineInfo;
	
}
