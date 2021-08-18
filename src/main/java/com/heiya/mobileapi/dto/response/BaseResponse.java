package com.heiya.mobileapi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BaseResponse {
	
	private boolean success;
	
	private String resultCode;
	
	private String resultMsg;
}
