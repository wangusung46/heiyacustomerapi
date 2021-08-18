package com.heiya.mobileapi.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heiya.mobileapi.dto.response.BaseResponse;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ListCheckNotificationDTOResponse extends BaseResponse {

    @JsonProperty("checkNotificationDTOResponses")
    private List<CheckNotificationDTOResponse> checkNotificationDTOResponses;

}
