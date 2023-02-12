package com.heiya.mobileapi.ap2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heiya.mobileapi.ap2.dto.request.Ap2GetTransactionRequest;
import com.heiya.mobileapi.ap2.dto.request.Ap2ListStoreRequest;
import com.heiya.mobileapi.ap2.dto.request.Ap2LoginRequest;
import com.heiya.mobileapi.ap2.dto.request.Ap2StoreRequest;
import com.heiya.mobileapi.ap2.dto.request.Ap2TokenRequest;
import com.heiya.mobileapi.ap2.dto.request.Ap2TransactionRequest;
import com.heiya.mobileapi.ap2.dto.response.Ap2GetTransactionResponse;
import com.heiya.mobileapi.ap2.dto.response.Ap2LoginResponse;
import com.heiya.mobileapi.ap2.dto.response.Ap2TransactionResponse;
import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.database.service.CRUDService;
import com.heiya.mobileapi.payment.model.Transaction;
import com.heiya.mobileapi.payment.repository.TransactionRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AP2ServiceImpl implements AP2Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AP2ServiceImpl.class);

    @Autowired
    private CRUDService crudService;

    @Autowired
    private TransactionRepository transactionRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    @Qualifier("paymentRestTemplate")
    private RestTemplate restTemplate;

    /**
     * @return the transactionRepository
     */
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    /**
     * @param transactionRepository the transactionRepository to set
     */
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    private Ap2LoginResponse hitLoginAp2() {
        Ap2LoginRequest request = new Ap2LoginRequest();
        Ap2LoginResponse response = new Ap2LoginResponse();

        request.setUsername(crudService.getGlobalConfigParamByKey(GlobalConstants.AP2_USERNAME));
        request.setPassword(crudService.getGlobalConfigParamByKey(GlobalConstants.AP2_PASSWORD));

        String urlAp2 = crudService.getGlobalConfigParamByKey(GlobalConstants.AP2_URL);
        String url = urlAp2.concat("/api/auth/login");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<Ap2LoginRequest> entity = new HttpEntity<>(request, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        try {
            ResponseEntity<Ap2LoginResponse> responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, Ap2LoginResponse.class);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                LOGGER.info("======== RESPONSE AP2ServiceImpl.getToken() - Status code response from AP2: " + responseEntity.getStatusCode());
            } else {
                response = responseEntity.getBody();
                if (response != null) {
                    Ap2TokenRequest.setToken(response.getToken());
                    Ap2TokenRequest.setStoreId(response.getAp2UserRequest().getAp2StoreRequests().get(0).getStoreId());
                }
            }
        } catch (final HttpClientErrorException httpClientErrorException) {
            LOGGER.info(httpClientErrorException.getMessage());
            if (response.getToken() == null) {
                hitLoginAp2();
            }
        } catch (HttpServerErrorException httpServerErrorException) {
            LOGGER.info(httpServerErrorException.getMessage());
            if (response.getToken() == null) {
                hitLoginAp2();
            }
        } catch (RestClientException exception) {
            LOGGER.info(exception.getMessage());
            if (response.getToken() == null) {
                hitLoginAp2();
            }
        }
        LOGGER.info("======== RESPONSE AP2ServiceImpl.getToken() - Ap2LoginResponse: " + response);

        return response;
    }

    @Override
    @Scheduled(fixedRateString = "${spring.scheduled.fixedRate.ap2}")
    public Ap2LoginResponse loginRequest() throws Exception {
//        LOGGER.info("" + Ap2TokenRequest.getLoginTime());
//        LOGGER.info("" + Ap2TokenRequest.getToken());
//        LOGGER.info("" + Ap2TokenRequest.getStoreId());

        Ap2LoginResponse response = new Ap2LoginResponse();

        String ap2 = crudService.getGlobalConfigParamByKey(GlobalConstants.AP2_ACTIVE);
        if (ap2.equals("1")) {

            // Declare Time to login AP2
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.HOUR, -12);
//            cal.add(Calendar.SECOND, -10);
            Date hourBack = cal.getTime();

            if (Ap2TokenRequest.getLoginTime() == null) {
                Ap2TokenRequest.setLoginTime(new Date());

                response = hitLoginAp2();
            }

//            LOGGER.info("" + Ap2TokenRequest.getLoginTime());
//            LOGGER.info("" + hourBack);
            if (Ap2TokenRequest.getLoginTime().before(hourBack)) {
                Ap2TokenRequest.setLoginTime(new Date());

                response = hitLoginAp2();
            } else {
                transactionCheck();
            }
        }
        return response;
    }

    private void transactionCheck() throws Exception {
        Ap2ListStoreRequest request = new Ap2ListStoreRequest();
        Ap2StoreRequest storeRequest = new Ap2StoreRequest();
        List<Ap2StoreRequest> storeRequests = new ArrayList<>();
        List<Ap2TransactionRequest> transactionRequests = new ArrayList<>();

        // Declare Time to login AP2
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 232
        cal.add(Calendar.HOUR, -Integer.parseInt(crudService.getGlobalConfigParamByKey(GlobalConstants.AP2_GET_TRANSACTION)));
//        cal.add(Calendar.HOUR, -24);
        Date hourBack = cal.getTime();

        Ap2TransactionResponse response = new Ap2TransactionResponse();
        List<Transaction> completePayments = getTransactionRepository().findPaymentListByStatusAndAP2(GlobalConstants.TRX_STATUS_COMPLETED, hourBack, Boolean.FALSE);
        if (!completePayments.isEmpty()) {
            for (Transaction payment : completePayments) {
                Ap2TransactionRequest transactionRequest = new Ap2TransactionRequest();
                transactionRequest.setInvoiceNo(invoice() + new Random().nextInt(900) + 100);
                transactionRequest.setTransDate(timeStampToDateString(payment.getChannelTransactionTime()));
                transactionRequest.setTransTime(timeStampToTimeStampString(payment.getChannelTransactionTime()));
                transactionRequest.setSequenceUnique(payment.getOrderNo());
                transactionRequest.setItemName(payment.getGoodsName());
                transactionRequest.setItemCode(payment.getGoodsCode());
                transactionRequest.setItemQty("1");
                transactionRequest.setItemPricePerUnit(String.valueOf(payment.getTotalFee().intValue() / 1.11));
                transactionRequest.setItemPriceAmount(String.valueOf(payment.getTotalFee().intValue() / 1.11));
                transactionRequest.setItemVat(String.valueOf((int) (payment.getTotalFee().intValue() - (payment.getTotalFee().intValue() / 1.11))));
                transactionRequest.setItemTotalPriceAmount(String.valueOf(payment.getTotalFee().intValue() / 1.11));
                transactionRequest.setTransactionAmount(payment.getTotalFee().toString());
                transactionRequests.add(transactionRequest);
            }

            storeRequest.setStoreId(Ap2TokenRequest.getStoreId());
            storeRequest.setTransactionRequest(transactionRequests);
            storeRequests.add(storeRequest);
            request.setStoreRequests(storeRequests);

            LOGGER.info("======== REQUEST AP2ServiceImpl.transactionCheck() - Ap2ListStoreRequest: " + mapper.writeValueAsString(request));

            String urlAp2 = crudService.getGlobalConfigParamByKey(GlobalConstants.AP2_URL);
            String url = urlAp2.concat("/api/v1/transaction/");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", Ap2TokenRequest.getToken());

            HttpEntity<Ap2ListStoreRequest> entity = new HttpEntity<>(request, headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
            try {
                ResponseEntity<Ap2TransactionResponse> responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, Ap2TransactionResponse.class);
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    LOGGER.info("======== AP2ServiceImpl.transactionCheck() - Status code response from AP2: " + responseEntity.getStatusCode());
                } else {
                    response = responseEntity.getBody();
                    for (Transaction payment : completePayments) {
                        payment.setAp2(Boolean.TRUE);
                        getTransactionRepository().save(payment);
                    }
                }
            } catch (final HttpClientErrorException httpClientErrorException) {
                LOGGER.info(httpClientErrorException.getMessage());
                hitLoginAp2();
            } catch (HttpServerErrorException httpServerErrorException) {
                LOGGER.info(httpServerErrorException.getMessage());
                hitLoginAp2();
            } catch (RestClientException exception) {
                LOGGER.info(exception.getMessage());
                hitLoginAp2();
            }
            LOGGER.info("======== RESPONSE AP2ServiceImpl.transactionCheck() - Ap2TransactionResponse: " + mapper.writeValueAsString(response));
        }
    }

    @Override
    public Ap2GetTransactionResponse getTransactionRequest(String date) throws Exception{
        Ap2GetTransactionRequest request = new Ap2GetTransactionRequest();
        Ap2GetTransactionResponse response = new Ap2GetTransactionResponse();
        
        request.setDate(date);
        request.setStoreId(Ap2TokenRequest.getStoreId());
        
        LOGGER.info("======== REQUEST AP2ServiceImpl.getTransactionRequest() - Ap2GetTransactionRequest: " + mapper.writeValueAsString(request));

        String urlAp2 = crudService.getGlobalConfigParamByKey(GlobalConstants.AP2_URL);
        String url = urlAp2.concat("/api/v1/simulation/");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", Ap2TokenRequest.getToken());

        HttpEntity<Ap2GetTransactionRequest> entity = new HttpEntity<>(request, headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        try {
            ResponseEntity<Ap2GetTransactionResponse> responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, Ap2GetTransactionResponse.class);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                LOGGER.info("======== AP2ServiceImpl.transactionCheck() - Status code response from AP2: " + responseEntity.getStatusCode());
            } else {
                response = responseEntity.getBody();
            }
        } catch (final HttpClientErrorException httpClientErrorException) {
            LOGGER.info(httpClientErrorException.getMessage());
        } catch (HttpServerErrorException httpServerErrorException) {
            LOGGER.info(httpServerErrorException.getMessage());
        } catch (RestClientException exception) {
            LOGGER.info(exception.getMessage());
        }
        LOGGER.info("======== RESPONSE AP2ServiceImpl.transactionCheck() - Ap2TransactionResponse: " + mapper.writeValueAsString(response));

        return response;
    }

    private String invoice() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    private String timeStampToDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    private String timeStampToTimeStampString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public Date formatUTCDateToLocalDate() throws ParseException {
        Date originalDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(originalDate);
        long gmtTime = cal.getTime().getTime();

        long timezoneAlteredTime = gmtTime + TimeZone.getTimeZone("Asia/Jakarta").getRawOffset();
        Calendar calNewZone = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
        calNewZone.setTimeInMillis(timezoneAlteredTime);
        return calNewZone.getTime();
    }

    @Override
    public Ap2LoginResponse recover(ResourceAccessException e) {
        LOGGER.info("======== RECOVER FALLBACK due to some exception : " + e.getMessage());
        Ap2LoginResponse response = new Ap2LoginResponse();
        /* Set error response */
        response.setAp2UserRequest(null);
        response.setMessage("400");
        response.setMessage("Application Error : " + e.getMessage());
        response.setToken(null);
        return response;
    }

    @Override
    public Ap2LoginResponse recover(RuntimeException e) {
        LOGGER.info("======== RECOVER FALLBACK due to some exception : " + e.getMessage());
        Ap2LoginResponse response = new Ap2LoginResponse();
        /* Set error response */
        response.setAp2UserRequest(null);
        response.setMessage("400");
        response.setMessage("Application Error : " + e.getMessage());
        response.setToken(null);
        return response;
    }

}
