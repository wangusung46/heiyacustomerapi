package com.heiya.mobileapi.product.dto.response;

import java.util.List;

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
public class MachineDetailListDTOResponse extends BaseResponse {
	
	@JsonProperty("machineList")
	private List<MachineDetailDTOResponse> machineList;
}
