package com.heiya.mobileapi.customer.dto.response;

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
public class CustomerInquiryChangeDTOResponse extends BaseResponse {

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("customerId")
    private long customerId;

}
