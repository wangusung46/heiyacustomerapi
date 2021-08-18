package com.heiya.mobileapi.firebase.service;

import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.firebase.dto.request.PushNotificationDTORequest;
import com.heiya.mobileapi.firebase.dto.request.PushOrderNotificationDTORequest;
import com.heiya.mobileapi.firebase.dto.response.PushNotificationDTOResponse;

public interface PushNotificationService {

//    public void sendPushNotification(PushNotificationDTORequest request) throws Exception;
//    public void sendPushNotificationCustomDataWithTopic(PushNotificationDTORequest request) throws Exception;
//    public void sendPushNotificationCustomDataWithTopicWithSpecificJson(PushNotificationDTORequest request) throws Exception;
//    public void sendPushNotificationWithoutData(PushNotificationDTORequest request) throws Exception;
//    public void sendPushNotificationToToken(PushNotificationDTORequest request) throws Exception;
    
//    public PushNotificationDTOResponse sendPushNotificationToAll(PushNotificationDTORequest request) throws Exception;
    public PushNotificationDTOResponse sendPushNotificationWithOrderNo(PushOrderNotificationDTORequest request) throws Exception;

}
