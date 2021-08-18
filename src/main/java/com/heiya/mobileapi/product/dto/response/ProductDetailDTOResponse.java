package com.heiya.mobileapi.product.dto.response;

import java.math.BigDecimal;
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
public class ProductDetailDTOResponse extends BaseResponse {
	
	@JsonProperty("productId")
	private Long productId;
	
	@JsonProperty("goodsId")
	private int goodsId;
	
	@JsonProperty("productName")
	private String productName;
	
	@JsonProperty("productDesc")
	private String productDesc;
	
	@JsonProperty("price")
	private BigDecimal price;
	
	@JsonProperty("discount")
	private BigDecimal discount;
	
	@JsonProperty("isActive")
	private String isActive;
	
	@JsonProperty("productImageList")
	private List<ProductImageDTOResponse> productImageList;
}
