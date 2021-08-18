package com.heiya.mobileapi.firebase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.database.service.CRUDService;
import com.heiya.mobileapi.firebase.dto.request.PushNotificationDTORequest;
import com.heiya.mobileapi.firebase.dto.request.PushOrderNotificationDTORequest;
import com.heiya.mobileapi.firebase.dto.response.PushNotificationDTOResponse;
import com.heiya.mobileapi.payment.model.Transaction;
import com.heiya.mobileapi.payment.repository.TransactionRepository;
import com.heiya.mobileapi.product.model.Product;
import com.heiya.mobileapi.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {

    private final Logger LOGGER = LoggerFactory.getLogger(PushNotificationServiceImpl.class);

    private final FCMPaymentSettlementService fcmpss;

    @Autowired
    private TransactionRepository paymentRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CRUDService crudService;

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    public PushNotificationServiceImpl(FCMPaymentSettlementService fcmpss) {
        this.fcmpss = fcmpss;
    }

//    @Override
//    public void sendPushNotification(PushNotificationDTORequest request) throws Exception {
//        LOGGER.info("======== START PushNotificationServiceImpl.sendPushNotification() with request : " + mapper.writeValueAsString(request));
//        try {
//            fcmService.sendMessage(getSamplePayloadData(), request);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//    }
//
//    @Override
//    public void sendPushNotificationCustomDataWithTopic(PushNotificationDTORequest request) throws Exception {
//        LOGGER.info("======== START PushNotificationServiceImpl.sendPushNotificationCustomDataWithTopic() with request : " + mapper.writeValueAsString(request));
//        try {
//            fcmService.sendMessageCustomDataWithTopic(getSamplePayloadDataCustom(), request);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//    }
//    @Override
//    public void sendPushNotificationCustomDataWithTopicWithSpecificJson(PushNotificationDTORequest request) throws Exception {
//        LOGGER.info("======== START PushNotificationServiceImpl.sendPushNotificationCustomDataWithTopicWithSpecificJson() with request : " + mapper.writeValueAsString(request));
//        try {
//            fcmService.sendMessageCustomDataWithTopic(getSamplePayloadDataWithSpecificJsonFormat(), request);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//    }
//
//    @Override
//    public void sendPushNotificationWithoutData(PushNotificationDTORequest request) throws Exception {
//        LOGGER.info("======== START PushNotificationServiceImpl.sendPushNotificationWithoutData() with request : " + mapper.writeValueAsString(request));
//        try {
//            fcmService.sendMessageWithoutData(request);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//    }
//
//    @Override
//    public void sendPushNotificationToToken(PushNotificationDTORequest request) throws Exception {
//        LOGGER.info("======== START PushNotificationServiceImpl.sendPushNotificationToToken() with request : " + mapper.writeValueAsString(request));
//        try {
//            fcmService.sendMessageToToken(request);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//    }
//
//    private Map<String, String> getSamplePayloadData() {
//        Map<String, String> pushData = new HashMap<>();
//        Map<String, String> data = new HashMap<>();
//        Map<String, String> payload = new HashMap<>();
//        Map<String, String> article_data = new HashMap<>();
//
//        pushData.put("title", "Notification for pending work");
//        pushData.put("message", "pls complete your pending task immediately");
//        pushData.put("image", "https://raw.githubusercontent.com/Firoz-Hasan/SpringBootPushNotification/master/pushnotificationconcept.png");
//        pushData.put("timestamp", "2020-07-11 19:23:21");
//        pushData.put("article_data", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//        // pushData.put("article_data","vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
//        // payload.put("article_data", String.valueOf(article_data));
//        // pushData.put("payload", String.valueOf(payload));
//
//        //   data.put("data", String.valueOf(pushData));
//        return pushData;
//    }
//
//    private Map<String, String> getSamplePayloadDataCustom() {
//        Map<String, String> pushData = new HashMap<>();
//
//        pushData.put("title", "Notification for pending work-custom");
//        pushData.put("message", "pls complete your pending task immediately-custom");
//        pushData.put("image", "https://raw.githubusercontent.com/Firoz-Hasan/SpringBootPushNotification/master/pushnotificationconcept.png");
//        pushData.put("timestamp", String.valueOf(new Date()));
//        pushData.put("article_data", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//        // pushData.put("article_data","vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
//        return pushData;
//    }
//
//    private Map<String, String> getSamplePayloadDataWithSpecificJsonFormat() {
//        Map<String, String> pushData = new HashMap<>();
//        Map<String, String> data = new HashMap<>();
//        ArrayList<Map<String, String>> payload = new ArrayList<>();
//        Map<String, String> article_data = new HashMap<>();
//
//        pushData.put("title", "jsonformat");
//        pushData.put("message", "itsworkingkudussssssssssssssssssssssssssssssssssss");
//        pushData.put("image", "qqq");
//        pushData.put("timestamp", "fefe");
//        article_data.put("article_data", "ffff");
//        payload.add(article_data);
//        pushData.put("payload", String.valueOf(payload));
//        data.put("data", String.valueOf(pushData));
//        return data;
//
//        /*getPreconfiguredMessageBuilderCustomDataWithTopic will get some issue to generate notification as
//        * data.get("title") wont give us title as its mapped inside data
//        * */
//    }
//
//    @Override
//    public PushNotificationDTOResponse sendPushNotificationToAll(PushNotificationDTORequest request) throws Exception {
//        PushNotificationDTOResponse response = new PushNotificationDTOResponse();
//        LOGGER.info("======== START PushNotificationServiceImpl.sendPushNotificationToAll() with request : " + mapper.writeValueAsString(request));
//        try {
//            fcmpss.sendMessageWithoutData(request);
//            response.setSuccess(true);
//            response.setResultCode("200");
//            response.setResultMsg("Send Notification To All Successfully");
//            LOGGER.info("======== END PaymentServiceImpl.sendPushNotificationToAll() - Send Notification To All Successfully with response : " + mapper.writeValueAsString(response));
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//        return response;
//    }
    @Override
    public PushNotificationDTOResponse sendPushNotificationWithOrderNo(PushOrderNotificationDTORequest request) throws Exception {
        PushNotificationDTOResponse response = new PushNotificationDTOResponse();
        PushNotificationDTORequest dTORequest = new PushNotificationDTORequest();
        LOGGER.info("======== START PushNotificationServiceImpl.sendPushNotificationWithOrderNo() with request : " + mapper.writeValueAsString(request));
        try {
            Transaction transaction = paymentRepo.findPaymentByOrderNo(request.getOrderNo());

            Date trxDate = transaction.getChannelTransactionTime();
            Calendar calendarPayment = Calendar.getInstance();

            calendarPayment.setTime(trxDate);
            calendarPayment.add(Calendar.HOUR_OF_DAY, 1); //relative notification
            Date datePayment = calendarPayment.getTime();

            calendarPayment.setTime(trxDate);
            calendarPayment.add(Calendar.HOUR_OF_DAY, 24); //relative notification
            Date datePickup = calendarPayment.getTime();

            if (request.getTopic().equals(GlobalConstants.NOTIFICATION_FIREBASE_CHECK_PAYMENT)) {
                dTORequest.setTitle("Please pay your order");
                dTORequest.setMessage("Hallo " + request.getCustomerName() + " Your order " + transaction.getGoodsName() + " will expire on " + datePayment.toLocaleString());
                dTORequest.setTopic(request.getTopic());
                dTORequest.setToken(request.getToken());

                fcmpss.sendMessageToToken(dTORequest);

                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Send Notification To " + transaction.getCustomerId() + " Successfully");

                LOGGER.info("======== END PushNotificationServiceImpl.sendPushNotificationWithOrderNo() - Send Notification Successfully with response : " + mapper.writeValueAsString(response));
            } else if (request.getTopic().equals(GlobalConstants.NOTIFICATION_FIREBASE_EXPIRE_PAYMENT)) {
                dTORequest.setTitle("Your payment has been expired");
                dTORequest.setMessage("Hallo " + request.getCustomerName() + " Your order " + transaction.getGoodsName() + " expire on " + datePayment.toLocaleString());
                dTORequest.setTopic(request.getTopic());
                dTORequest.setToken(request.getToken());

                fcmpss.sendMessageToToken(dTORequest);

                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Send Notification To " + transaction.getCustomerId() + " Successfully");

                LOGGER.info("======== END PushNotificationServiceImpl.sendPushNotificationWithOrderNo() - Send Notification Successfully with response : " + mapper.writeValueAsString(response));
            } else if (request.getTopic().equals(GlobalConstants.NOTIFICATION_FIREBASE_SECCESS_PAYMENT)) {
                dTORequest.setTitle("Your payment has been completed");
                dTORequest.setMessage("Hallo " + request.getCustomerName() + " Your order " + transaction.getGoodsName() + " completed");
                dTORequest.setTopic(request.getTopic());
                dTORequest.setToken(request.getToken());

                fcmpss.sendMessageToToken(dTORequest);

                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Send Notification To " + transaction.getCustomerId() + " Successfully");

                LOGGER.info("======== END PushNotificationServiceImpl.sendPushNotificationWithOrderNo() - Send Notification Successfully with response : " + mapper.writeValueAsString(response));
            } else if (request.getTopic().equals(GlobalConstants.NOTIFICATION_FIREBASE_CHECK_EXPIRE)) {
                dTORequest.setTitle("Please take your order");
                dTORequest.setMessage("Hallo " + request.getCustomerName() + " Your order " + transaction.getGoodsName() + " expire on " + datePickup.toLocaleString());
                dTORequest.setTopic(request.getTopic());
                dTORequest.setToken(request.getToken());

                fcmpss.sendMessageToToken(dTORequest);

                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Send Notification To " + transaction.getCustomerId() + " Successfully");

                LOGGER.info("======== END PushNotificationServiceImpl.sendPushNotificationWithOrderNo() - Send Notification Successfully with response : " + mapper.writeValueAsString(response));
            } else if (request.getTopic().equals(GlobalConstants.NOTIFICATION_FIREBASE_EXPIRE_PICKUP)) {
                dTORequest.setTitle("Your order has been expired");
                dTORequest.setMessage("Hallo " + request.getCustomerName() + " Your order " + transaction.getGoodsName() + " expire on " + datePickup.toLocaleString());
                dTORequest.setTopic(request.getTopic());
                dTORequest.setToken(request.getToken());

                fcmpss.sendMessageToToken(dTORequest);

                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Send Notification To " + transaction.getCustomerId() + " Successfully");

                LOGGER.info("======== END PushNotificationServiceImpl.sendPushNotificationWithOrderNo() - Send Notification Successfully with response : " + mapper.writeValueAsString(response));
            } else if (request.getTopic().equals(GlobalConstants.NOTIFICATION_FIREBASE_SECCESS_PICKUP)) {
                dTORequest.setTitle("Your order has been taken");
                dTORequest.setMessage("Hallo " + request.getCustomerName() + " Your order " + transaction.getGoodsName() + " completed");
                dTORequest.setTopic(request.getTopic());
                dTORequest.setToken(request.getToken());

                fcmpss.sendMessageToToken(dTORequest);

                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Send Notification To " + transaction.getCustomerId() + " Successfully");

                LOGGER.info("======== END PushNotificationServiceImpl.sendPushNotificationWithOrderNo() - Send Notification Successfully with response : " + mapper.writeValueAsString(response));
            } else {
                response.setSuccess(false);
                response.setResultCode("404");
                response.setResultMsg("Aplication Error");

                LOGGER.info("======== END PushNotificationServiceImpl.sendPushNotificationWithOrderNo() - Aplication Error");
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return response;
    }

    private Map<String, String> getDetailOrderData(String orderNo) {
        Map<String, String> pushData = new HashMap<>();
        Transaction transaction = paymentRepo.findPaymentByOrderNo(orderNo);

        Date trxDate = transaction.getChannelTransactionTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trxDate);
        calendar.add(Calendar.HOUR_OF_DAY, 24); //relative notification
        Date date = calendar.getTime();

        pushData.put("title", "Please take your order : " + transaction.getGoodsName());
        pushData.put("message", "Your order will expire on " + date.toLocaleString());
        pushData.put("topic", "notification_expire_payment");
//        pushData.put("image", product.getGoodsUrl());
//        pushData.put("timestamp", "2020-07-11 19:23:21");
//        pushData.put("article_data", transaction.getChannelDeeplink());
        return pushData;
    }

    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        Map<String, String> payload = new HashMap<>();
        Map<String, String> article_data = new HashMap<>();

        pushData.put("title", "Notification for pending work");
        pushData.put("message", "pls complete your pending task immediately");
        pushData.put("image", "https://raw.githubusercontent.com/Firoz-Hasan/SpringBootPushNotification/master/pushnotificationconcept.png");
        pushData.put("timestamp", "2020-07-11 19:23:21");
        pushData.put("article_data", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        // pushData.put("article_data","vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        // payload.put("article_data", String.valueOf(article_data));
        // pushData.put("payload", String.valueOf(payload));

        //   data.put("data", String.valueOf(pushData));
        return pushData;
    }
}
