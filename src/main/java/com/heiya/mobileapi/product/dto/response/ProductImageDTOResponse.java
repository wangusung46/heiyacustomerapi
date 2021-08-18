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
public class ProductImageDTOResponse {
	
	@JsonProperty("productId")
	private Long productId;
	
	@JsonProperty("imagePath")
	private String imagePath;
	
	@JsonProperty("isMain")
	private String isMain;
}
