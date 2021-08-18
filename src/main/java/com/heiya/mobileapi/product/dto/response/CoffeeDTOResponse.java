package com.heiya.mobileapi.product.dto.response;

import java.math.BigDecimal;
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
public class CoffeeDTOResponse {
	
	@JsonProperty("operator")
	private String operatorId;
	
	@JsonProperty("model_id")
	private int modelId;
	
	@JsonProperty("goods_id")
	private int goodsId;
	
	@JsonProperty("goods_name")
	private String goodsName;
	
	@JsonProperty("goods_url")
	private String goodsURL;
	
	@JsonProperty("price")
	private BigDecimal price;
	
	@JsonProperty("taste_id")
	private int tasteId;
	
	@JsonProperty("taste_name")
	private String tasteName;
	
	@JsonProperty("goodsProtocol")
	private int goodsProtocol;
	
	@JsonProperty("taste_url")
	private String tasteURL;
	
	@JsonProperty("remark")
	private String remark;
}
