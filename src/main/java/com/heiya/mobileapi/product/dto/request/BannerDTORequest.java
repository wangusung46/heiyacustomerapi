package com.heiya.mobileapi.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BannerDTORequest {
	
	@JsonProperty("bannerName")
	private String bannerName;
	
	@JsonProperty("bannerPath")
	private String bannerPath;
}
