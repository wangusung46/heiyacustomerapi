package com.heiya.mobileapi.firebase.dto.response;

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
public class PushNotificationWithOrderNoDTOResponse extends BaseResponse {

    @JsonProperty("orderNo")
    private String orderNo;

}
