package com.heiya.mobileapi.firebase.service;

import com.heiya.mobileapi.firebase.dto.request.PushNotificationDTORequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public interface FCMPaymentSettlementService {

    public void sendMessageToToken(PushNotificationDTORequest request) throws Exception, InterruptedException, ExecutionException;

//    public void sendMessageCustomDataWithTopic(Map<String, String> data, PushNotificationDTORequest request) throws Exception, InterruptedException, ExecutionException;

}
