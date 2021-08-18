package com.heiya.mobileapi.firebase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.firebase.dto.request.PushNotificationDTORequest;
import com.heiya.mobileapi.firebase.dto.request.PushOrderNotificationDTORequest;
import com.heiya.mobileapi.firebase.dto.response.PushNotificationDTOResponse;
import com.heiya.mobileapi.firebase.service.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/firebase")
public class PushNotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationController.class);

    @Autowired
    private PushNotificationService pushNotificationService;

//    @PostMapping(value = "/notification/topic", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> doSendendNotification(@RequestBody PushNotificationDTORequest request) throws JsonProcessingException {
//        LOGGER.info("\n\n======== START PushNotificationController.doSendendNotification");
//        PushNotificationDTOResponse response = null;
//        try {
//            response = pushNotificationService.sendPushNotificationToAll(request);
//            LOGGER.info("======== COMPLETED PushNotificationController.doSendendNotification");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }

    @PostMapping(value = "/notification/payment/checkpayment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doSendPaymentTokenNotification(@RequestBody PushOrderNotificationDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PushNotificationController.doSendTokenCheckExpirePickup");
        PushNotificationDTOResponse response = null;
        try {
            response = pushNotificationService.sendPushNotificationWithOrderNo(request);
            LOGGER.info("======== COMPLETED PushNotificationController.doSendTokenCheckExpirePickup");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    
//    @PostMapping(value = "/notification/settlement/expire", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> doSendTokenExpirePickup(@RequestBody PushOrderNotificationDTORequest request) throws JsonProcessingException {
//        LOGGER.info("\n\n======== START PushNotificationController.doSendTokenExpirePickup");
//        PushNotificationDTOResponse response = null;
//        try {
//            response = pushNotificationService.sendPushNotificationWithOrderNo(request);
//            LOGGER.info("======== COMPLETED PushNotificationController.doSendTokenExpirePickup");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }

//    @PostMapping(value = "/notification/data", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> doSendDataNotification(@RequestBody PushNotificationDTORequest request) throws JsonProcessingException {
//        LOGGER.info("\n\n======== START PushNotificationController.doSendDataNotification");
//        PushNotificationDTOResponse response = null;
//        try {
//            pushNotificationService.sendPushNotification(request);
//            LOGGER.info("======== COMPLETED PushNotificationController.doSendDataNotification");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }
//
//    @PostMapping(value = "/notification/data/customdatawithtopic", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity doSendDataNotificationCustom(@RequestBody PushNotificationDTORequest request) {
//        LOGGER.info("\n\n======== START PushNotificationController.doSendDataNotificationCustom");
//        PushNotificationDTOResponse response = null;
//        try {
//            pushNotificationService.sendPushNotificationCustomDataWithTopic(request);
//            LOGGER.info("======== COMPLETED PushNotificationController.doSendDataNotificationCustom");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }
//
//    @PostMapping(value = "/notification/data/customdatawithtopicjson", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity doSendDataNotificationCustomWithSpecificJson(@RequestBody PushNotificationDTORequest request) {
//        LOGGER.info("\n\n======== START PushNotificationController.doSendDataNotificationCustomWithSpecificJson");
//        PushNotificationDTOResponse response = null;
//        try {
//            pushNotificationService.sendPushNotificationCustomDataWithTopicWithSpecificJson(request);
//            LOGGER.info("======== COMPLETED PushNotificationController.doSendDataNotificationCustomWithSpecificJson");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }
//
//    public void sendAutomaticNotification() {
//        PushNotificationDTORequest request = new PushNotificationDTORequest();
//        try {
//            request.setTopic("global");
//            pushNotificationService.sendPushNotificationCustomDataWithTopicWithSpecificJson(request);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
//    }
}
