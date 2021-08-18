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
public class TasteDTOResponse {
	
	@JsonProperty("tasteId")
	private int tasteId;
	
	@JsonProperty("tasteName")
	private String tasteName;
	
	@JsonProperty("goodsProtocol")
	private int goodsProtocol;
}
