package com.heiya.mobileapi.product.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDTOResponsev2 {
	
	@JsonProperty("productId")
	private Long productId;
	
	@JsonProperty("goodsId")
	private int goodsId;
	
	@JsonProperty("productName")
	private String productName;
	
	@JsonProperty("productDesc")
	private String productDesc;
	
	@JsonProperty("priceMin")
	private BigDecimal priceMin;
        
        @JsonProperty("priceMax")
	private BigDecimal priceMax;
	
	@JsonProperty("discount")
	private String discount;
	
	@JsonProperty("isActive")
	private String isActive;
	
	@JsonProperty("productImagePath")
	private String productImagePath;
        
        @JsonProperty("modelId")
	private String modelId;
}
