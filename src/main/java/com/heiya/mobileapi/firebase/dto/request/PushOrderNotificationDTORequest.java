package com.heiya.mobileapi.firebase.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PushOrderNotificationDTORequest {

//    @JsonProperty("title")
//    private String title;
//    
//    @JsonProperty("message")
//    private String message;
//
    @JsonProperty("topic")
    private String topic;
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("orderNo")
    private String orderNo;
    
    @JsonProperty("customerName")
    private String customerName;
    
}
