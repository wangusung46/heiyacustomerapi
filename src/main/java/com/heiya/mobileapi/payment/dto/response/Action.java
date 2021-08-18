package com.heiya.mobileapi.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Action {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("method")
	private String method;
	
	@JsonProperty("url")
	private String url;
}
