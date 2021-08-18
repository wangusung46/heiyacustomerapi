package com.heiya.mobileapi.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CheckNotificationDTOResponse {

    @JsonProperty("orderNo")
    private String orderNo;
    
    @JsonProperty("token")
    private List<TokenDTOResponse> token;
    
    @JsonProperty("notifMsg")
    private String notifMsg;
    
    @JsonProperty("customerName")
    private String customerName;

}
