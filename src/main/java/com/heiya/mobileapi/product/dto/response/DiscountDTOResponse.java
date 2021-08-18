package com.heiya.mobileapi.product.dto.response;

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
public class DiscountDTOResponse extends BaseResponse {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("goodsId")
	private Integer goodsId;
	
	@JsonProperty("discount")
	private Integer discount;
}
