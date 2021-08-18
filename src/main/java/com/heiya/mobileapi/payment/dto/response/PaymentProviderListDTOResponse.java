package com.heiya.mobileapi.payment.dto.response;

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
public class PaymentProviderListDTOResponse extends BaseResponse {
	
	@JsonProperty("paymentProviderList")
	private List<PaymentProviderDTOResponse> paymentProviderList;
}
