package com.heiya.mobileapi.firebase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heiya.mobileapi.firebase.dto.request.PushNotificationDTORequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMPaymentSettlementServiceImpl implements FCMPaymentSettlementService{

    private final Logger LOGGER = LoggerFactory.getLogger(FCMPaymentSettlementServiceImpl.class);
    
    private final ObjectMapper mapper = new ObjectMapper();

//    @Override
//    public void sendMessage(Map<String, String> data, PushNotificationDTORequest request) throws Exception, InterruptedException, ExecutionException {
//        LOGGER.info("======== START FCMService.sendMessage() with request : " + mapper.writeValueAsString(request));
//        Message message = getPreconfiguredMessageWithData(data, request);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String jsonOutput = gson.toJson(message);
//        String response = sendAndGetResponse(message);
//        LOGGER.info("======== END FCMService.sendMessage() - Sent message with data. Topic: " + request.getTopic() + ", " + response + " msg " + jsonOutput);
//    }

//    public void sendMessageCustomDataWithTopic(Map<String, String> data, PushNotificationDTORequest request) throws Exception, InterruptedException, ExecutionException {
//        LOGGER.info("======== START FCMService.sendMessageCustomDataWithTopic() with request : " + mapper.writeValueAsString(request) + " and data : " + mapper.writeValueAsString(data));
//        Message message = getPreconfiguredMessageWithDataCustomWithTopic(data, request);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String jsonOutput = gson.toJson(message);
//        String response = sendAndGetResponse(message);
//        LOGGER.info("======== END FCMService.sendMessageCustomDataWithTopic() - Sent message with data. Topic: " + data.get("topic") + ", " + response + " msg " + jsonOutput);
//    }

//    public void sendMessageWithoutData(PushNotificationDTORequest request) throws Exception, InterruptedException, ExecutionException {
//        LOGGER.info("======== START FCMService.sendMessageWithoutData() with request : " + mapper.writeValueAsString(request));
//        Message message = getPreconfiguredMessageWithoutData(request);
//        String response = sendAndGetResponse(message);
//        LOGGER.info("======== END FCMService.sendMessageWithoutData() - Sent message without data. Topic: " + request.getTopic() + ", " + response);
//    }
//
    @Override
    public void sendMessageToToken(PushNotificationDTORequest request) throws Exception, InterruptedException, ExecutionException {
        LOGGER.info("======== START FCMService.sendMessageToToken() with request : " + mapper.writeValueAsString(request));
        Message message = getPreconfiguredMessageToToken(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        LOGGER.info("======== END FCMService.sendMessageToToken() - Sent message to token. Device token: " + request.getToken() + ", " + response + " message " + jsonOutput);
//        LOGGER.info("Sent message to token. Device token: " + request.getToken() + ", " + response + " msg " + jsonOutput);
    }
//    
//    // HEIYA
//    public void sendMessageToAll(PushNotificationDTORequest request) throws Exception, InterruptedException, ExecutionException {
//        LOGGER.info("======== START FCMService.sendMessageToAll() with request : " + mapper.writeValueAsString(request));
//        Message message = getPreconfiguredMessageWithoutData(request);
//        String response = sendAndGetResponse(message);
//        LOGGER.info("======== END FCMService.sendMessageToAll() - Sent message without data. Topic: " + request.getTopic() + ", " + response);
//    }
//
//    public void sendMessageWithOrder(PushOrderNotificationDTORequest request) throws Exception, InterruptedException, ExecutionException {
//        LOGGER.info("======== START FCMService.sendMessageWithOrder() with request : " + mapper.writeValueAsString(request));
//        Message message = getPreconfiguredMessageToToken(request);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String jsonOutput = gson.toJson(message);
//        String response = sendAndGetResponse(message);
//        LOGGER.info("======== END FCMService.sendMessageWithOrder() - Sent message to token. Device token: " + request.getToken() + ", " + response + " msg " + jsonOutput);
//    }
//    // END HEIYA
//
    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
                        .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private Message getPreconfiguredMessageToToken(PushNotificationDTORequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private Message getPreconfiguredMessageWithoutData(PushNotificationDTORequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
                .build();
    }

    private Message getPreconfiguredMessageWithData(Map<String, String> data, PushNotificationDTORequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.getToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationDTORequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                        new Notification(request.getTitle(), request.getMessage()));
    }


    private Message getPreconfiguredMessageWithDataCustomWithTopic(Map<String, String> data, PushNotificationDTORequest request) {
        return getPreconfiguredMessageBuilderCustomDataWithTopic(data, request).putAllData(data).setTopic(request.getTopic())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilderCustomDataWithTopic(Map<String, String> data, PushNotificationDTORequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(data.get(request.getTopic()));
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                        new Notification(data.get("title"), data.toString()));
    }

}
