package com.heiya.mobileapi.payment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BaseHWResponse {
	
	private int success;
	
	private String resultCode;
	
	private String resultMsg;
	
	private String result;
	
	private String extInfo;
}
