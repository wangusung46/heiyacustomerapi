package com.heiya.mobileapi.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.customer.dto.response.CustomerChangeDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerInquiryChangeDTOResponse;
import com.heiya.mobileapi.customer.model.Customer;
import com.heiya.mobileapi.customer.model.Token;
import com.heiya.mobileapi.customer.repository.CustomerRepository;
import com.heiya.mobileapi.customer.repository.TokenRepository;
import com.heiya.mobileapi.database.service.CRUDService;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.firebase.model.PaymentNotification;
import com.heiya.mobileapi.firebase.repository.PaymentNotificationRepository;
import com.heiya.mobileapi.otp.controller.ReportController;
import com.heiya.mobileapi.payment.dto.request.ChargePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.CreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.CustomerChangeDTORequest;
import com.heiya.mobileapi.payment.dto.request.Gopay;
import com.heiya.mobileapi.payment.dto.request.GopayCallbackDTORequest;
import com.heiya.mobileapi.payment.dto.request.GopayCreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.HWBrewingDTORequest;
import com.heiya.mobileapi.payment.dto.request.HWPickupOrderDTORequest;
import com.heiya.mobileapi.payment.dto.request.HeiyaGeneralCallbackDTORequest;
import com.heiya.mobileapi.payment.dto.request.Items;
import com.heiya.mobileapi.payment.dto.request.PickupDTORequest;
import com.heiya.mobileapi.payment.dto.request.PickupOrderDetailRequest;
import com.heiya.mobileapi.payment.dto.request.ShopeePay;
import com.heiya.mobileapi.payment.dto.request.ShopeepayCreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.TransactionDetails;
import com.heiya.mobileapi.payment.dto.request.XenditChannelProperties;
import com.heiya.mobileapi.payment.dto.request.XenditCreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.XenditDanaCreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.XenditGeneralCallbackDTORequest;
import com.heiya.mobileapi.payment.dto.request.XenditLinkAjaCreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.XenditMetadata;
import com.heiya.mobileapi.payment.dto.request.XenditOvoCreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.response.Action;
import com.heiya.mobileapi.payment.dto.response.BaseHWResponse;
import com.heiya.mobileapi.payment.dto.response.CheckNotificationDTOResponse;
import com.heiya.mobileapi.payment.dto.response.GopayCreatePaymentDTOResponse;
import com.heiya.mobileapi.payment.dto.response.GopayTrxStatusDTOResponse;
import com.heiya.mobileapi.payment.dto.response.ListCheckNotificationDTOResponse;
import com.heiya.mobileapi.payment.dto.response.MidtransResponse;
import com.heiya.mobileapi.payment.dto.response.OrderDTOResponse;
import com.heiya.mobileapi.payment.dto.response.OrderListDTOResponse;
import com.heiya.mobileapi.payment.dto.response.PaymentProviderDTOResponse;
import com.heiya.mobileapi.payment.dto.response.PaymentProviderListDTOResponse;
import com.heiya.mobileapi.payment.dto.response.ShopeepayCreatePaymentDTOResponse;
import com.heiya.mobileapi.payment.dto.response.TokenDTOResponse;
import com.heiya.mobileapi.payment.dto.response.TrxStatusDTOResponse;
import com.heiya.mobileapi.payment.dto.response.XenditCreatePaymentDTOResponse;
import com.heiya.mobileapi.payment.dto.response.XenditResponse;
import com.heiya.mobileapi.payment.model.PaymentProvider;
import com.heiya.mobileapi.payment.model.StoreTransactionDetail;
import com.heiya.mobileapi.payment.model.Transaction;
import com.heiya.mobileapi.payment.repository.PaymentProviderRepository;
import com.heiya.mobileapi.payment.repository.TransactionRepository;
import com.heiya.mobileapi.product.dto.response.DiscountDTOResponse;
import com.heiya.mobileapi.product.model.Machine;
import com.heiya.mobileapi.product.repository.MachineRepository;
import com.heiya.mobileapi.product.service.ProductService;
import com.heiya.mobileapi.util.GopayConnectUtil;
import com.heiya.mobileapi.util.XenditConnectUtil;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    @Qualifier("paymentRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private PaymentProviderRepository providerRepo;

    @Autowired
    private TransactionRepository paymentRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MachineRepository machineRepo;

    @Autowired
    private CRUDService crudService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentNotificationRepository notificationRepository;

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private ReportController reportController;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PaymentProviderListDTOResponse queryAllPaymentProviders() throws Exception {
        PaymentProviderListDTOResponse response = new PaymentProviderListDTOResponse();
        LOGGER.info("======== START PaymentServiceImpl.queryAllPaymentProviders()");
        List<PaymentProvider> providerList = providerRepo.findAll();

        if (providerList != null && !providerList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<PaymentProviderDTOResponse> providerResList = new ArrayList<>();
            for (PaymentProvider provider : providerList) {
                PaymentProviderDTOResponse providerRes = new PaymentProviderDTOResponse();
                providerRes.setId(provider.getId());
                providerRes.setProviderName(provider.getProviderName());
                providerRes.setApiEndpoint(provider.getApiEndpoint());
                providerRes.setIsActive(provider.getIsActive());

                providerResList.add(providerRes);
            }

            response.setPaymentProviderList(providerResList);
            LOGGER.info("======== PaymentServiceImpl.queryAllPaymentProviders() - Provider response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("There are no any providers available right now");
            LOGGER.info("======== PaymentServiceImpl.performCustomerLogin() - No providers available");
        }
        return response;
    }

    @Override
    public XenditCreatePaymentDTOResponse createXenditOvoPayment(CreatePaymentDTORequest request) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.createXenditEWalletPayment() with request : " + request);
        XenditCreatePaymentDTOResponse paymentResponse = new XenditCreatePaymentDTOResponse();

        try {
            /* Define URL & headers */
            String ewalletBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_URL);
            String url = ewalletBaseURL.concat("/ewallets");
            HttpHeaders headers = getXenditRequestHeaders();

            //Generate order no
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
            String orderNo = "operator".concat(formatter.format(new Date()));
            request.setExternalId(orderNo);

            //Mapping request body for Xendit
            XenditOvoCreatePaymentDTORequest xenditRequest = new XenditOvoCreatePaymentDTORequest();
            xenditRequest.setExternalId(orderNo);

            xenditRequest.setAmount(request.getAmount());

            String phoneNo = request.getPhone();
            if (phoneNo.startsWith("+62")) {
                phoneNo = phoneNo.replace("+62", "0");
            }
            xenditRequest.setPhone(phoneNo);
            xenditRequest.setEwalletType(request.getEwalletType());

            LOGGER.info("======== REQUEST PaymentServiceImpl.createXenditEWalletPayment(): " + mapper.writeValueAsString(request));

            HttpEntity<XenditOvoCreatePaymentDTORequest> entity = new HttpEntity<>(xenditRequest, headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<XenditCreatePaymentDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, XenditCreatePaymentDTOResponse.class);
            paymentResponse = responseEntity.getBody();

            if (responseEntity.getStatusCode() == HttpStatus.OK && paymentResponse != null) {
                paymentResponse.setSuccess(true); //0 = success
                paymentResponse.setResultCode("200"); //HTTP success code
                paymentResponse.setResultMsg("Xendit eWallet transaction is created");
                LOGGER.info("======== RESPONSE PaymentServiceImpl.createXenditEWalletPayment() - XenditCreatePaymentDTOResponse: " + mapper.writeValueAsString(paymentResponse));

                /* Saving payment data to DB */
                Transaction payment = new Transaction();
                this.setPaymentObj(request, paymentResponse, payment);
                crudService.addPayment(payment);
                /* End */
            } else {
                paymentResponse.setSuccess(false);
                paymentResponse.setResultCode(paymentResponse.getErrorCode());
                paymentResponse.setResultMsg(paymentResponse.getMessage());
            }
            LOGGER.debug("======== RESPONSE PaymentServiceImpl.createXenditEWalletPayment() - BaseHWResponse: "
                    + mapper.writeValueAsString(paymentResponse));
            /* End Mapping */
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            paymentResponse.setSuccess(false);
            paymentResponse.setResultCode("400");
            paymentResponse.setResultMsg(e.getMessage());
        }

        return paymentResponse;
    }

    @Override
    public XenditCreatePaymentDTOResponse createXenditDanaPayment(CreatePaymentDTORequest request) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.createXenditDanaPayment() with request : " + request);
        XenditCreatePaymentDTOResponse paymentResponse = new XenditCreatePaymentDTOResponse();

        try {
            /* Define URL & headers */
            String ewalletBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_URL);
            String url = ewalletBaseURL.concat("/ewallets");
            HttpHeaders headers = getXenditRequestHeaders();

            //Generate order no
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
            String orderNo = "operator".concat(formatter.format(new Date()));
            request.setExternalId(orderNo);

            //Mapping request body for Xendit Dana
            XenditDanaCreatePaymentDTORequest xenditRequest = new XenditDanaCreatePaymentDTORequest();
            xenditRequest.setExternalId(orderNo);

            xenditRequest.setAmount(request.getAmount());
            xenditRequest.setEwalletType(request.getEwalletType());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY, 1); //add 1 jam dari jam saat ini utk expiry pembayaran Dana
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            xenditRequest.setExpirationDate(sdf.format(calendar.getTime()));

            xenditRequest.setCallbackUrl(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_DANA_CALLBACKURL));
            xenditRequest.setRedirectUrl("http://myheiya.id/paymentsuccess");

            LOGGER.info("======== REQUEST PaymentServiceImpl.createXenditDanaPayment(): " + mapper.writeValueAsString(request));

            HttpEntity<XenditDanaCreatePaymentDTORequest> entity = new HttpEntity<>(xenditRequest, headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<XenditCreatePaymentDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, XenditCreatePaymentDTOResponse.class);
            paymentResponse = responseEntity.getBody();
            LOGGER.info("======== RESPONSE PaymentServiceImpl.createXenditDanaPayment() - XenditCreatePaymentDTOResponse: " + mapper.writeValueAsString(paymentResponse));

            if (responseEntity.getStatusCode() == HttpStatus.OK && paymentResponse != null) {
                paymentResponse.setSuccess(true); //0 = success
                paymentResponse.setResultCode("200"); //HTTP success code
                paymentResponse.setResultMsg("Xendit DANA transaction is created");

                /* Saving payment data to DB */
                Transaction payment = new Transaction();
                this.setPaymentDanaAndLinkAjaObj(request, paymentResponse, payment);
                crudService.addPayment(payment);
                /* End */
            } else {
                paymentResponse.setSuccess(false);
                paymentResponse.setResultCode(paymentResponse.getErrorCode());
                paymentResponse.setResultMsg(paymentResponse.getMessage());
            }
            LOGGER.debug("======== RESPONSE PaymentServiceImpl.createXenditDanaPayment() - BaseHWResponse: "
                    + mapper.writeValueAsString(paymentResponse));
            /* End Mapping */
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            paymentResponse.setSuccess(false);
            paymentResponse.setResultCode("400");
            paymentResponse.setResultMsg(e.getMessage());
        }

        return paymentResponse;
    }

    @Override
    public XenditCreatePaymentDTOResponse createXenditLinkAjaPayment(CreatePaymentDTORequest request) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.createXenditLinkAjaPayment() with request : " + request);
        XenditCreatePaymentDTOResponse paymentResponse = new XenditCreatePaymentDTOResponse();

        try {
            /* Define URL & headers */
            String ewalletBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_URL);
            String url = ewalletBaseURL.concat("/ewallets");
            HttpHeaders headers = getXenditRequestHeaders();

            //Generate order no
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
            String orderNo = "operator".concat(formatter.format(new Date()));
            request.setExternalId(orderNo);

            //Mapping request body for Xendit LinkAja
            XenditLinkAjaCreatePaymentDTORequest xenditRequest = new XenditLinkAjaCreatePaymentDTORequest();
            xenditRequest.setExternalId(orderNo);
            String phoneNo = request.getPhone();
            if (phoneNo.startsWith("+62")) {
                phoneNo = phoneNo.replace("+62", "0");
            }
            xenditRequest.setPhone(phoneNo);

            xenditRequest.setAmount(request.getAmount());

            //set items
            Items items = new Items();
            items.setId(String.valueOf(request.getGoodsId()));
            items.setName(request.getProductName());
            items.setPrice(request.getAmount());
            items.setQuantity(1); //In Heiya, quantity always 1
            List<Items> itemList = new ArrayList<>();
            itemList.add(items);
            xenditRequest.setItems(itemList);

            xenditRequest.setCallbackUrl(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_LINKAJA_CALLBACKURL));
            xenditRequest.setRedirectUrl("http://myheiya.id/paymentsuccess");
            xenditRequest.setEwalletType(request.getEwalletType());

            LOGGER.info("======== REQUEST PaymentServiceImpl.createXenditLinkAjaPayment(): " + mapper.writeValueAsString(request));

            HttpEntity<XenditLinkAjaCreatePaymentDTORequest> entity = new HttpEntity<>(xenditRequest, headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<XenditCreatePaymentDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, XenditCreatePaymentDTOResponse.class);
            paymentResponse = responseEntity.getBody();
            LOGGER.info("======== RESPONSE PaymentServiceImpl.createXenditLinkAjaPayment() - XenditCreatePaymentDTOResponse: " + mapper.writeValueAsString(paymentResponse));

            if (responseEntity.getStatusCode() == HttpStatus.OK && paymentResponse != null) {
                paymentResponse.setSuccess(true); //0 = success
                paymentResponse.setResultCode("200"); //HTTP success code
                paymentResponse.setResultMsg("Xendit LINKAJA transaction is created");

                /* Saving payment data to DB */
                Transaction payment = new Transaction();
                this.setPaymentDanaAndLinkAjaObj(request, paymentResponse, payment);
                crudService.addPayment(payment);
                /* End */
            } else {
                paymentResponse.setSuccess(false);
                paymentResponse.setResultCode(paymentResponse.getErrorCode());
                paymentResponse.setResultMsg(paymentResponse.getMessage());
            }
            LOGGER.debug("======== RESPONSE PaymentServiceImpl.createXenditLinkAjaPayment() - BaseHWResponse: "
                    + mapper.writeValueAsString(paymentResponse));
            /* End Mapping */
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            paymentResponse.setSuccess(false);
            paymentResponse.setResultCode("400");
            paymentResponse.setResultMsg(e.getMessage());
        }

        return paymentResponse;
    }

    @Override
    public XenditResponse chargeXenditPayment(ChargePaymentDTORequest request) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.chargeXenditPayment() with request : " + request);
        XenditResponse xenditResponse = new XenditResponse();

        try {
            /* Define URL & headers */
            String ewalletBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_URL);
            String url = ewalletBaseURL.concat("/ewallets/charges");
            HttpHeaders headers = getXenditRequestHeaders();

            //Generate order no
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
            String orderNo = "operator".concat(formatter.format(new Date()));
            request.setExternalId(orderNo);

            //Mapping request body for Xendit
            XenditCreatePaymentDTORequest xenditRequest = new XenditCreatePaymentDTORequest();
            XenditChannelProperties channelProperties = new XenditChannelProperties();
            XenditMetadata xenditMetadata = new XenditMetadata();

            switch (request.getEwalletType()) {
                case GlobalConstants.EWALLET_TYPE_OVO_NEW: {
                    xenditRequest.setReferenceId(request.getExternalId());
                    xenditRequest.setCurrency("IDR");
                    xenditRequest.setAmount(request.getAmount());
                    xenditRequest.setCheckoutMethod("ONE_TIME_PAYMENT");
                    xenditRequest.setChannelCode(request.getEwalletType());
                    String phoneNo = request.getPhone();
                    if (phoneNo.startsWith("0")) {
                        phoneNo = phoneNo.replaceFirst("0", "+62");
                    } else if (phoneNo.startsWith("62")) {
                        phoneNo = phoneNo.replaceFirst("62", "+62");
                    }
                    channelProperties.setMobileNumber(phoneNo);
                    channelProperties.setSuccessRedirectUrl("http://myheiya.id/paymentsuccess");
                    channelProperties.setFailureRedirectUrl("http://myheiya.id/paymentfailed");
                    xenditRequest.setChannelProperties(channelProperties);
                    xenditMetadata.setBranchArea(null);
                    xenditMetadata.setBranchCity(null);
                    xenditMetadata.setBranchCode("tree_branch");
                    xenditRequest.setMetadata(xenditMetadata);
                    break;
                }
                case GlobalConstants.EWALLET_TYPE_SHOPEEPAY_NEW: {
                    xenditRequest.setReferenceId(request.getExternalId());
                    xenditRequest.setCurrency("IDR");
                    xenditRequest.setAmount(request.getAmount());
                    xenditRequest.setCheckoutMethod("ONE_TIME_PAYMENT");
                    xenditRequest.setChannelCode(request.getEwalletType());
                    String phoneNo = request.getPhone();
                    if (phoneNo.startsWith("0")) {
                        phoneNo = phoneNo.replaceFirst("0", "+62");
                    } else if (phoneNo.startsWith("62")) {
                        phoneNo = phoneNo.replaceFirst("62", "+62");
                    }
                    channelProperties.setMobileNumber(phoneNo);
                    channelProperties.setSuccessRedirectUrl("http://myheiya.id/paymentsuccess");
                    channelProperties.setFailureRedirectUrl("http://myheiya.id/paymentfailed");
                    xenditRequest.setChannelProperties(channelProperties);
                    xenditMetadata.setBranchArea(null);
                    xenditMetadata.setBranchCity(null);
                    xenditMetadata.setBranchCode("tree_branch");
                    xenditRequest.setMetadata(xenditMetadata);
                    break;
                }
                case GlobalConstants.EWALLET_TYPE_DANA_NEW: {
                    xenditRequest.setReferenceId(request.getExternalId());
                    xenditRequest.setCurrency("IDR");
                    xenditRequest.setAmount(request.getAmount());
                    xenditRequest.setCheckoutMethod("ONE_TIME_PAYMENT");
                    xenditRequest.setChannelCode(request.getEwalletType());
                    String phoneNo = request.getPhone();
                    if (phoneNo.startsWith("0")) {
                        phoneNo = phoneNo.replaceFirst("0", "+62");
                    } else if (phoneNo.startsWith("62")) {
                        phoneNo = phoneNo.replaceFirst("62", "+62");
                    }
                    channelProperties.setMobileNumber(phoneNo);
                    channelProperties.setSuccessRedirectUrl("http://myheiya.id/paymentsuccess");
                    channelProperties.setFailureRedirectUrl("http://myheiya.id/paymentfailed");
                    xenditRequest.setChannelProperties(channelProperties);
                    xenditMetadata.setBranchArea(null);
                    xenditMetadata.setBranchCity(null);
                    xenditMetadata.setBranchCode("tree_branch");
                    xenditRequest.setMetadata(xenditMetadata);
                    break;
                }
                case GlobalConstants.EWALLET_TYPE_LINKAJA_NEW: {
                    xenditRequest.setReferenceId(request.getExternalId());
                    xenditRequest.setCurrency("IDR");
                    xenditRequest.setAmount(request.getAmount());
                    xenditRequest.setCheckoutMethod("ONE_TIME_PAYMENT");
                    xenditRequest.setChannelCode(request.getEwalletType());
                    String phoneNo = request.getPhone();
                    if (phoneNo.startsWith("0")) {
                        phoneNo = phoneNo.replaceFirst("0", "+62");
                    } else if (phoneNo.startsWith("62")) {
                        phoneNo = phoneNo.replaceFirst("62", "+62");
                    }
                    channelProperties.setMobileNumber(phoneNo);
                    channelProperties.setSuccessRedirectUrl("http://myheiya.id/paymentsuccess");
                    channelProperties.setFailureRedirectUrl("http://myheiya.id/paymentfailed");
                    xenditRequest.setChannelProperties(channelProperties);
                    xenditMetadata.setBranchArea(null);
                    xenditMetadata.setBranchCity(null);
                    xenditMetadata.setBranchCode("tree_branch");
                    xenditRequest.setMetadata(xenditMetadata);
                    break;
                }
                default:
                    break;
            }

            LOGGER.info("======== REQUEST PaymentServiceImpl.chargeXenditPayment(): " + mapper.writeValueAsString(request));

            HttpEntity<XenditCreatePaymentDTORequest> entity = new HttpEntity<>(xenditRequest, headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<XenditResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, XenditResponse.class);
            xenditResponse = responseEntity.getBody();

            if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED && xenditResponse != null) {
                xenditResponse.setSuccess(true); //0 = success
                xenditResponse.setResultCode("200"); //HTTP success code
                xenditResponse.setResultMsg("Xendit eWallet transaction is created");
                LOGGER.info("======== RESPONSE PaymentServiceImpl.chargeXenditPayment() - XenditResponse: " + mapper.writeValueAsString(xenditResponse));

                /* Saving payment data to DB */
                Transaction payment = new Transaction();
                this.setNewPaymentObj(request, xenditResponse, payment);
                crudService.addPayment(payment);
                /* End */
            } else {
                xenditResponse.setSuccess(false);
                xenditResponse.setResultCode("404");
                xenditResponse.setResultMsg("Not Found");
            }
            LOGGER.debug("======== RESPONSE PaymentServiceImpl.chargeXenditPayment() - BaseHWResponse: "
                    + mapper.writeValueAsString(xenditResponse));
            /* End Mapping */
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            xenditResponse.setSuccess(false);
            xenditResponse.setResultCode("400");
            xenditResponse.setResultMsg(e.getMessage());
        }

        return xenditResponse;
    }

    @Override
    public TrxStatusDTOResponse getXenditTrxStatus(String externalId, String ewalletType) throws Exception {
        TrxStatusDTOResponse response = new TrxStatusDTOResponse();

        try {
            /* Define URL & headers */
            String xenditBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_URL);
            String url = xenditBaseURL.concat("/ewallets/charges/").concat(externalId);

            HttpHeaders headers = getXenditRequestHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<TrxStatusDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, TrxStatusDTOResponse.class);
            response = responseEntity.getBody();

            //Transaction payment = crudService.getPaymentByOrderNo(externalId);
            switch (response.getStatus()) {
                case "PENDING":
                    response.setStatus("PENDING");
                    break;
                case "COMPLETED":
                    //OVO, LINKAJA
                    response.setStatus("COMPLETED");
                    break;
                case "PAID":
                    //DANA
                    response.setStatus("COMPLETED"); //seragamkan response ke mobile jadi "COMPLETED"
                    break;
                case "FAILED":
                case "EXPIRED":
                    response.setStatus("FAILED");
                    response.setReason("Failed: Payment is not completed on the eWallet. Please try again.");
                    break;
                default:
                    break;
            }

            //set global response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Transaction status retrieved successfully");
            //LOGGER.debug("======== RESPONSE PaymentServiceImpl.getXenditTrxStatus() - XenditTrxStatusDTOResponse: " + mapper.writeValueAsString(response));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg(e.getMessage());
        }

        return response;
    }

    //@Override
    public TrxStatusDTOResponse getNewXenditTrxStatus(String externalId) throws Exception {
        LOGGER.info("======== REQUEST PaymentServiceImpl.getXenditTrxStatus() external ID : " + externalId);
        TrxStatusDTOResponse response = new TrxStatusDTOResponse();

        try {
            /* Define URL & headers */
            String xenditBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_URL);
            String url = xenditBaseURL.concat("/ewallets/charges/").concat(externalId);

            HttpHeaders headers = getXenditRequestHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<TrxStatusDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, TrxStatusDTOResponse.class);
            response = responseEntity.getBody();

            //Transaction payment = crudService.getPaymentByOrderNo(externalId);
            switch (response.getStatus()) {
                case "PENDING":
                    response.setStatus("PENDING");
                    break;
                case "COMPLETED":
                    //OVO, LINKAJA
                    response.setStatus("COMPLETED");
                    break;
                case "PAID":
                    //DANA
                    response.setStatus("COMPLETED"); //seragamkan response ke mobile jadi "COMPLETED"
                    break;
                case "SUCCEEDED":
                    //SHOPEEPAY
                    response.setStatus("COMPLETED"); //seragamkan response ke mobile jadi "COMPLETED"
                    break;
                case "FAILED":
                case "EXPIRED":
                    response.setStatus("FAILED");
                    response.setReason("Failed: Payment is not completed on the eWallet. Please try again.");
                    break;
                default:
                    break;
            }

            //set global response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Transaction status retrieved successfully");
            //LOGGER.debug("======== RESPONSE PaymentServiceImpl.getXenditTrxStatus() - XenditTrxStatusDTOResponse: " + mapper.writeValueAsString(response));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg(e.getMessage());
        }

        return response;
    }

    @Override
    public TrxStatusDTOResponse getGopayTrxStatus(String externalId, String trigger) throws Exception {
        //LOGGER.info("======== REQUEST PaymentServiceImpl.getGopayTrxStatus() for external ID : " + externalId);
        TrxStatusDTOResponse response = new TrxStatusDTOResponse();
        GopayTrxStatusDTOResponse gopayResponse = new GopayTrxStatusDTOResponse();

        try {
            /* Define URL & headers */
            String gopayBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_URL1); //Midtrans URL
            String url = gopayBaseURL.concat("/").concat(externalId).concat("/status");

            HttpHeaders headers = getGopayRequestHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<GopayTrxStatusDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, GopayTrxStatusDTOResponse.class);
            gopayResponse = responseEntity.getBody();

            if (gopayResponse != null && !gopayResponse.getStatusCode().equals("404")) { //trx is found
                //set global response
                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Transaction status retrieved successfully");
                //map trx status response
                response.setAmount(gopayResponse.getGrossAmount());
                response.setBusinessId(gopayResponse.getTransactionId());
                response.setEwalletType(gopayResponse.getPaymentType().toUpperCase());
                response.setExternalId(gopayResponse.getOrderId());
                response.setTransactionDate(gopayResponse.getTransactionTime()); //already in yyyy-MM-dd HH:mm:ss
                switch (gopayResponse.getTransactionStatus()) {
                    case GlobalConstants.TRX_STATUS_PENDING:
                        response.setStatus("PENDING");
                        break;
                    case "settlement":
                        response.setStatus("COMPLETED"); //seragramkan
                        //Update ke DB untuk status payment, walaupun sudah dihandle scheduler (buat backup jika scheduler gagal)
                        if (trigger.equals(GlobalConstants.PAYMENT_STATUS_TRIGGER_APP)) {
                            GopayCallbackDTORequest callbackRequest = new GopayCallbackDTORequest();
                            callbackRequest.setTransactionTime(gopayResponse.getTransactionTime());
                            callbackRequest.setTransactionStatus("COMPLETED");
                            callbackRequest.setOrderId(gopayResponse.getOrderId());
                            //Use callback method for GOPAY to update the status to DB & call HW to submit the order
                            this.callbackStatusByGopay(callbackRequest);
                        }
                        break;
                    default:
                        response.setStatus("FAILED"); //could be expire, deny, refund, cancel
                        response.setReason("Failed: Payment is not completed on the eWallet. Please try again.");
                        break;
                }
            } else {
                //set global response
                response.setSuccess(false);
                response.setResultCode(gopayResponse.getStatusCode());
                response.setResultMsg(gopayResponse.getStatusMessage());
            }

            //LOGGER.debug("======== RESPONSE PaymentServiceImpl.getGopayTrxStatus() - TrxStatusDTOResponse: " + mapper.writeValueAsString(response));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg(e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse callbackStatusByXendit(XenditGeneralCallbackDTORequest callbackRequest) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.callbackStatusByXendit() with request : " + mapper.writeValueAsString(callbackRequest));
        BaseHWResponse response = new BaseHWResponse();
        BaseResponse baseResponse = new BaseResponse();

        /*  Check whether the order no is already paid or not.
		 *  Only paid order no that will able to process this callback functionality
         */
        Transaction payment = crudService.getPaymentByOrderNo(callbackRequest.getExternalId()); //get stored transaction from DB by order no from Xendit callback
        if (payment == null) {
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("404");
            baseResponse.setResultMsg("Payment with order no. " + callbackRequest.getExternalId() + " not found in our database.");
            LOGGER.info("======== PaymentServiceImpl.callbackStatusByXendit() error response : " + mapper.writeValueAsString(baseResponse));
            return baseResponse;
        }

        try {
            /* Define URL & headers */
            String hwApiBaseUrl2 = crudService.getGlobalConfigParamByKey(GlobalConstants.CLIENT_URL2);
            String url = hwApiBaseUrl2.concat("/order");

            if (payment != null && payment.getPaymentStatus().equals("pending")) {
                if (callbackRequest.getStatus().equals("COMPLETED")) {
                    /* Set request parameter */
                    HWPickupOrderDTORequest request = new HWPickupOrderDTORequest();
                    request.setOrderNo(callbackRequest.getExternalId());
                    request.setOrderType("normal");
                    request.setOperateCode(crudService.getGlobalConfigParamByKey(GlobalConstants.OPERATE_CODE));
                    request.setPaymentTime(this.convertDateFormat(callbackRequest.getCreated()));

                    //SET AMOUNT and DISCOUNT
                    DiscountDTOResponse discRes = productService.getProductDiscount(Integer.parseInt(payment.getGoodsCode()));
                    if (discRes != null && discRes.getResultCode().equals("200")) {
                        BigDecimal oriAmount = payment.getTotalFee();
                        BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
                        BigDecimal discAmount = (oriAmount.multiply(discInPercent)).divide(new BigDecimal(100));
                        BigDecimal finalAmount = oriAmount.subtract(discAmount);
                        request.setOriginalAmount(String.valueOf(oriAmount));
                        request.setDepositAmount(String.valueOf(discAmount));
                        request.setPaymentAmount(String.valueOf(finalAmount));
                    } else {
                        request.setOriginalAmount("");
                        request.setDepositAmount("");
                        request.setPaymentAmount(String.valueOf(payment.getTotalFee()));
                    }

                    request.setMachineCode(payment.getMachineCode());
                    request.setOrderSource(GlobalConstants.ORDER_SOURCE);

                    //Order request list
                    PickupOrderDetailRequest orderDtl = new PickupOrderDetailRequest();
                    orderDtl.setTasteid(String.valueOf(payment.getTasteId()));
                    orderDtl.setName(payment.getGoodsName());
                    orderDtl.setGoodid(payment.getGoodsCode());
                    //Generate order goods no
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
                    String randomString = RandomStringUtils.randomAlphabetic(4).toUpperCase();
                    String orderGoodsNo = "operate".concat(formatter.format(new Date())).concat("_").concat(randomString);
                    orderDtl.setOrderGoodsNo(orderGoodsNo);
                    orderDtl.setBrewingCode("");
                    orderDtl.setOrderPrice(request.getPaymentAmount());

                    List<PickupOrderDetailRequest> orderList = new ArrayList<>();
                    orderList.add(orderDtl);
                    request.setDetail(orderList);

                    /* Set header */
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                    LOGGER.info("======== START PaymentServiceImpl.callbackStatusByXendit() Order request to HW: " + mapper.writeValueAsString(request));

                    HttpEntity<HWPickupOrderDTORequest> entity = new HttpEntity<>(request, headers);

                    /* Build URI from URL */
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

                    /*Re-initialize the RestTemplate with these codes*/
                    restTemplate = new RestTemplate();
                    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
                    mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
                    restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
                    /* End of RestTemplate */

                    ResponseEntity<BaseHWResponse> responseEntity;
                    responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, BaseHWResponse.class);
                    response = responseEntity.getBody();
                    LOGGER.info("======== RESPONSE PaymentServiceImpl.callbackStatusByXendit() - BaseHWResponse: " + mapper.writeValueAsString(response));

                    //Update status from Xendit to DB
                    if (response.getSuccess() == 0) {
                        payment.setPaymentStatus(GlobalConstants.TRX_STATUS_SETTLEMENT);
                        payment.setSettlementTime(this.formatUTCDateToLocalZone(callbackRequest.getCreated()));
                        crudService.updatePayment(payment);

                        //save also transaction detail
                        this.saveStoreTransactionDetail(request, orderDtl);

                        /* Response to Xendit */
                        baseResponse.setSuccess(true);
                        baseResponse.setResultCode("200");
                        baseResponse.setResultMsg("Payment callback has been processed successfully.");
                    } else { //{Pickup order failed
                        if (response.getResultMsg().equals("Dupilcated Order ")) {
                            LOGGER.info("======== PaymentServiceImpl.callbackStatusByXendit() - Repeat Duplicated Order");
                        } else {
                            //If there is an error from HW Server API, then mark the status as "undelivered"
                            payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNDELIVERED);
                            payment.setSettlementTime(this.formatUTCDateToLocalZone(callbackRequest.getCreated()));
                            payment.setErrorMessage(response.getResultCode().concat(" : ").concat(response.getResultMsg()));
                            crudService.updatePayment(payment);

                            /* Response to Xendit */
                            baseResponse.setSuccess(false);
                            baseResponse.setResultCode("412");
                            baseResponse.setResultMsg("Payment callback has been processed successfully.");
                        }
                    }
                    LOGGER.info("======== PaymentServiceImpl.callbackStatusByXendit() - Payment order no " + payment.getOrderNo() + " has been updated to DB with status: " + payment.getPaymentStatus());
                } else {
                    //Xendit payment failed due to any reason. Update to DB
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                    payment.setErrorMessage(callbackRequest.getFailureCode());
                    crudService.updatePayment(payment);
                }
            } else {
                baseResponse.setSuccess(true);
                baseResponse.setResultCode("400");
                baseResponse.setResultMsg("Payment callback received with settlement status.");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            /* Set BAD response */
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("400");
            baseResponse.setResultMsg("Application Error : " + e.getMessage());
            return baseResponse;
        }

        LOGGER.info("======== COMPLETED PaymentServiceImpl.callbackStatusByXendit()");
        return baseResponse;
    }

    @Override
    public BaseResponse callbackStatusByHeiya(HeiyaGeneralCallbackDTORequest callbackRequest) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.callbackStatusByHeiya() with request : " + mapper.writeValueAsString(callbackRequest));
        BaseHWResponse response = new BaseHWResponse();
        BaseResponse baseResponse = new BaseResponse();

        /*  Check whether the order no is already paid or not.
		 *  Only paid order no that will able to process this callback functionality
         */
        Transaction payment = crudService.getPaymentByOrderNo(callbackRequest.getExternalId()); //get stored transaction from DB by order no from Xendit callback
        if (payment == null) {
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("404");
            baseResponse.setResultMsg("Payment with order no. " + callbackRequest.getExternalId() + " not found in our database.");
            LOGGER.info("======== PaymentServiceImpl.callbackStatusByHeiya() error response : " + mapper.writeValueAsString(baseResponse));
            return baseResponse;
        }

        try {
            /* Define URL & headers */
            String hwApiBaseUrl2 = crudService.getGlobalConfigParamByKey(GlobalConstants.CLIENT_URL2);
            String url = hwApiBaseUrl2.concat("/order");

            if (payment != null && payment.getPaymentStatus().equals("pending")) {
//                if (callbackRequest.getStatus().equals("COMPLETED")) {
                /* Set request parameter */
                HWPickupOrderDTORequest request = new HWPickupOrderDTORequest();
                request.setOrderNo(callbackRequest.getExternalId());
                request.setOrderType("normal");
                request.setOperateCode(crudService.getGlobalConfigParamByKey(GlobalConstants.OPERATE_CODE));
                request.setPaymentTime(this.convertDateFormatFree(callbackRequest.getCreated()));

                request.setOriginalAmount("");
                request.setDepositAmount("");
                request.setPaymentAmount(String.valueOf(payment.getTotalFee()));

                request.setMachineCode(payment.getMachineCode());
                request.setOrderSource(GlobalConstants.ORDER_SOURCE);

                //Order request list
                PickupOrderDetailRequest orderDtl = new PickupOrderDetailRequest();
                orderDtl.setTasteid(String.valueOf(payment.getTasteId()));
                orderDtl.setName(payment.getGoodsName());
                orderDtl.setGoodid(payment.getGoodsCode());
                //Generate order goods no
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
                String randomString = RandomStringUtils.randomAlphabetic(4).toUpperCase();
                String orderGoodsNo = "free".concat(formatter.format(new Date())).concat("_").concat(randomString);
                orderDtl.setOrderGoodsNo(orderGoodsNo);
                orderDtl.setBrewingCode("");
                orderDtl.setOrderPrice(request.getPaymentAmount());

                List<PickupOrderDetailRequest> orderList = new ArrayList<>();
                orderList.add(orderDtl);
                request.setDetail(orderList);

                /* Set header */
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                LOGGER.info("======== START PaymentServiceImpl.callbackStatusByHeiya() Order request to HW: " + mapper.writeValueAsString(request));

                HttpEntity<HWPickupOrderDTORequest> entity = new HttpEntity<>(request, headers);

                /* Build URI from URL */
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

                /*Re-initialize the RestTemplate with these codes*/
                restTemplate = new RestTemplate();
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
                mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
                restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
                /* End of RestTemplate */

                ResponseEntity<BaseHWResponse> responseEntity;
                responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, BaseHWResponse.class);
                response = responseEntity.getBody();
                LOGGER.info("======== RESPONSE PaymentServiceImpl.callbackStatusByHeiya() - BaseHWResponse: " + mapper.writeValueAsString(response));

                //Update status from Xendit to DB
                if (response.getSuccess() == 0) {
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_SETTLEMENT);
                    payment.setSettlementTime(this.formatUTCDateToLocalZoneForHeiya(callbackRequest.getCreated()));
                    crudService.updatePayment(payment);

                    //save also transaction detail
                    this.saveStoreTransactionDetail(request, orderDtl);

                    /* Response to Xendit */
                    baseResponse.setSuccess(true);
                    baseResponse.setResultCode("200");
                    baseResponse.setResultMsg("Payment callback has been processed successfully.");
                } else { //{Pickup order failed
                    if (response.getResultMsg().equals("Dupilcated Order ")) {
                        LOGGER.info("======== PaymentServiceImpl.callbackStatusByHeiya() - Repeat Duplicated Order");
                    } else {
                        //If there is an error from HW Server API, then mark the status as "undelivered"
                        payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNDELIVERED);
                        payment.setSettlementTime(this.formatUTCDateToLocalZoneForHeiya(callbackRequest.getCreated()));
                        payment.setErrorMessage(response.getResultCode().concat(" : ").concat(response.getResultMsg()));
                        crudService.updatePayment(payment);

                        /* Response to Xendit */
                        baseResponse.setSuccess(false);
                        baseResponse.setResultCode("412");
                        baseResponse.setResultMsg("Payment callback has been processed successfully.");
                    }
                }
                LOGGER.info("======== PaymentServiceImpl.callbackStatusByHeiya() - Payment order no " + payment.getOrderNo() + " has been updated to DB with status: " + payment.getPaymentStatus());
            } else {
                baseResponse.setSuccess(true);
                baseResponse.setResultCode("400");
                baseResponse.setResultMsg("Payment callback received with settlement status.");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            /* Set BAD response */
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("400");
            baseResponse.setResultMsg("Application Error : " + e.getMessage());
            return baseResponse;
        }

        LOGGER.info("======== COMPLETED PaymentServiceImpl.callbackStatusByXendit()");
        return baseResponse;
    }

    @Override
    public BaseResponse callbackStatusByXenditForDana(XenditGeneralCallbackDTORequest callbackRequest) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.callbackStatusByXenditForDana() with request : " + mapper.writeValueAsString(callbackRequest));
        BaseHWResponse response = new BaseHWResponse();
        BaseResponse baseResponse = new BaseResponse();

        /*  Check whether the order no is already paid or not.
		 *  Only paid order no that will able to process this callback functionality
         */
        Transaction payment = crudService.getPaymentByOrderNo(callbackRequest.getExternalId()); //get stored transaction from DB by order no from Xendit callback
        if (payment == null) {
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("404");
            baseResponse.setResultMsg("Payment with order no. " + callbackRequest.getExternalId() + " not found in our database.");
            LOGGER.info("======== PaymentServiceImpl.callbackStatusByXenditForDana() error response : " + mapper.writeValueAsString(baseResponse));
            return baseResponse;
        }

        try {
            /* Define URL & headers */
            String hwApiBaseUrl2 = crudService.getGlobalConfigParamByKey(GlobalConstants.CLIENT_URL2);
            String url = hwApiBaseUrl2.concat("/order");

            if (payment != null && payment.getPaymentStatus().equals("pending")) {
                if (callbackRequest.getPaymentStatus().equals("PAID") || callbackRequest.getPaymentStatus().equals("COMPLETED")) {
                    /* Set request parameter */
                    HWPickupOrderDTORequest request = new HWPickupOrderDTORequest();
                    request.setOrderNo(callbackRequest.getExternalId());
                    request.setOrderType("normal");
                    request.setOperateCode(crudService.getGlobalConfigParamByKey(GlobalConstants.OPERATE_CODE));
                    request.setPaymentTime(this.convertDateFormatDANA(callbackRequest.getTransactionDate()));

                    //SET AMOUNT and DISCOUNT
                    DiscountDTOResponse discRes = productService.getProductDiscount(Integer.parseInt(payment.getGoodsCode()));
                    if (discRes != null && discRes.getResultCode().equals("200")) {
                        BigDecimal oriAmount = payment.getTotalFee();
                        BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
                        BigDecimal discAmount = (oriAmount.multiply(discInPercent)).divide(new BigDecimal(100));
                        BigDecimal finalAmount = oriAmount.subtract(discAmount);
                        request.setOriginalAmount(String.valueOf(oriAmount));
                        request.setDepositAmount(String.valueOf(discAmount));
                        request.setPaymentAmount(String.valueOf(finalAmount));
                    } else {
                        request.setOriginalAmount("");
                        request.setDepositAmount("");
                        request.setPaymentAmount(String.valueOf(payment.getTotalFee()));
                    }

                    request.setMachineCode(payment.getMachineCode());
                    request.setOrderSource(GlobalConstants.ORDER_SOURCE);

                    //Order request list
                    PickupOrderDetailRequest orderDtl = new PickupOrderDetailRequest();
                    orderDtl.setTasteid(String.valueOf(payment.getTasteId()));
                    orderDtl.setName(payment.getGoodsName());
                    orderDtl.setGoodid(payment.getGoodsCode());
                    //Generate order goods no
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
                    String randomString = RandomStringUtils.randomAlphabetic(4).toUpperCase();
                    String orderGoodsNo = "operate".concat(formatter.format(new Date())).concat("_").concat(randomString);
                    orderDtl.setOrderGoodsNo(orderGoodsNo);
                    orderDtl.setBrewingCode("");
                    orderDtl.setOrderPrice(request.getPaymentAmount());

                    List<PickupOrderDetailRequest> orderList = new ArrayList<>();
                    orderList.add(orderDtl);
                    request.setDetail(orderList);

                    /* Set header */
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                    LOGGER.info("======== START PaymentServiceImpl.callbackStatusByXenditForDana() Order request to HW: " + mapper.writeValueAsString(request));

                    HttpEntity<HWPickupOrderDTORequest> entity = new HttpEntity<>(request, headers);

                    /* Build URI from URL */
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

                    /*Re-initialize the RestTemplate with these codes*/
                    restTemplate = new RestTemplate();
                    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
                    mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
                    restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
                    /* End of RestTemplate */

                    ResponseEntity<BaseHWResponse> responseEntity;
                    responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, BaseHWResponse.class);
                    response = responseEntity.getBody();
                    LOGGER.info("======== RESPONSE PaymentServiceImpl.callbackStatusByXenditForDana() - BaseHWResponse: " + mapper.writeValueAsString(response));

                    //If Response from HW is success, update status from Xendit to DB
                    LOGGER.info("======== RESPONSE PaymentServiceImpl.callbackStatusByXenditForDana() - Success Code: " + response.getSuccess());
                    if (response.getSuccess() == 0) {
                        payment.setPaymentStatus(GlobalConstants.TRX_STATUS_SETTLEMENT);
                        Date settlementDate = this.formatUTCDateToLocalZoneForDANA(callbackRequest.getTransactionDate());
                        payment.setSettlementTime(payment.getChannelTransactionTime());
                        payment.setChannelTransactionId(callbackRequest.getBusinessId());
                        crudService.updatePayment(payment);

                        //save also transaction detail
                        this.saveStoreTransactionDetail(request, orderDtl);

                        /* Response to Xendit */
                        baseResponse.setSuccess(true);
                        baseResponse.setResultCode("200");
                        baseResponse.setResultMsg("Payment callback has been processed successfully.");
                    } else { //{Pickup order failed
                        if (response.getResultMsg().equals("Dupilcated Order ")) {
                            LOGGER.info("======== PaymentServiceImpl.callbackStatusByXendit() - Repeat Duplicated Order");
                        } else {
                            //If there is an error from HW Server API, then mark the status as "undelivered"
                            payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNDELIVERED);
                            Date settlementDate = this.formatUTCDateToLocalZoneForDANA(callbackRequest.getTransactionDate());
                            payment.setSettlementTime(settlementDate);
                            payment.setChannelTransactionId(callbackRequest.getBusinessId());
                            payment.setErrorMessage(response.getResultCode().concat(" : ").concat(response.getResultMsg()));
                            crudService.updatePayment(payment);

                            /* Response to Xendit */
                            baseResponse.setSuccess(false);
                            baseResponse.setResultCode("412");
                            baseResponse.setResultMsg("Payment callback received with error in delivery (undelivered).");
                        }
                    }
                    LOGGER.info("======== PaymentServiceImpl.callbackStatusByXenditForDana() - Payment order no " + payment.getOrderNo() + " has been updated to DB with status: " + payment.getPaymentStatus());
                } else {
                    //Xendit payment failed due to any reason. Update to DB
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                    payment.setErrorMessage("Transaksi gagal diproses di sisi DANA (expired/canceled/failed). Check Xendit dashboard.");
                    crudService.updatePayment(payment);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            /* Set BAD response */
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("400");
            baseResponse.setResultMsg("Application Error : " + e.getMessage());
            return baseResponse;
        }

        LOGGER.info("======== COMPLETED PaymentServiceImpl.callbackStatusByXenditForDana()");
        return baseResponse;
    }

    @Override
    public BaseResponse callbackStatusByXenditForLinkAja(XenditGeneralCallbackDTORequest callbackRequest) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.callbackStatusByXenditForLinkAja() with request : " + mapper.writeValueAsString(callbackRequest));
        BaseHWResponse response = new BaseHWResponse();
        BaseResponse baseResponse = new BaseResponse();

        /*  Check whether the order no is already paid or not.
		 *  Only paid order no that will able to process this callback functionality
         */
        Transaction payment = crudService.getPaymentByOrderNo(callbackRequest.getExternalId()); //get stored transaction from DB by order no from Xendit callback
        if (payment == null) {
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("404");
            baseResponse.setResultMsg("Payment with order no. " + callbackRequest.getExternalId() + " not found in our database.");
            LOGGER.info("======== PaymentServiceImpl.callbackStatusByXenditForLinkAja() error response : " + mapper.writeValueAsString(baseResponse));
            return baseResponse;
        }

        try {
            /* Define URL & headers */
            String hwApiBaseUrl2 = crudService.getGlobalConfigParamByKey(GlobalConstants.CLIENT_URL2);
            String url = hwApiBaseUrl2.concat("/order");

            if (payment != null && payment.getPaymentStatus().equals("pending")) {
                if (callbackRequest.getStatus().equals("SUCCESS_COMPLETED") || callbackRequest.getStatus().equals("COMPLETED")) {
                    /* Set request parameter */
                    HWPickupOrderDTORequest request = new HWPickupOrderDTORequest();
                    request.setOrderNo(callbackRequest.getExternalId());
                    request.setOrderType("normal");
                    request.setOperateCode(crudService.getGlobalConfigParamByKey(GlobalConstants.OPERATE_CODE));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    request.setPaymentTime(sdf.format(this.getDateBasedOnLocalZone()));

                    //SET AMOUNT and DISCOUNT
                    DiscountDTOResponse discRes = productService.getProductDiscount(Integer.parseInt(payment.getGoodsCode()));
                    if (discRes != null && discRes.getResultCode().equals("200")) {
                        BigDecimal oriAmount = payment.getTotalFee();
                        BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
                        BigDecimal discAmount = (oriAmount.multiply(discInPercent)).divide(new BigDecimal(100));
                        BigDecimal finalAmount = oriAmount.subtract(discAmount);
                        request.setOriginalAmount(String.valueOf(oriAmount));
                        request.setDepositAmount(String.valueOf(discAmount));
                        request.setPaymentAmount(String.valueOf(finalAmount));
                    } else {
                        request.setOriginalAmount("");
                        request.setDepositAmount("");
                        request.setPaymentAmount(String.valueOf(payment.getTotalFee()));
                    }

                    request.setMachineCode(payment.getMachineCode());
                    request.setOrderSource(GlobalConstants.ORDER_SOURCE);

                    //Order request list
                    PickupOrderDetailRequest orderDtl = new PickupOrderDetailRequest();
                    orderDtl.setTasteid(String.valueOf(payment.getTasteId()));
                    orderDtl.setName(payment.getGoodsName());
                    orderDtl.setGoodid(payment.getGoodsCode());
                    //Generate order goods no
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
                    String randomString = RandomStringUtils.randomAlphabetic(4).toUpperCase();
                    String orderGoodsNo = "operate".concat(formatter.format(new Date())).concat("_").concat(randomString);
                    orderDtl.setOrderGoodsNo(orderGoodsNo);
                    orderDtl.setBrewingCode("");
                    orderDtl.setOrderPrice(request.getPaymentAmount());

                    List<PickupOrderDetailRequest> orderList = new ArrayList<>();
                    orderList.add(orderDtl);
                    request.setDetail(orderList);

                    /* Set header */
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                    LOGGER.info("======== START PaymentServiceImpl.callbackStatusByXenditForLinkAja() Order request to HW: " + mapper.writeValueAsString(request));

                    HttpEntity<HWPickupOrderDTORequest> entity = new HttpEntity<>(request, headers);

                    /* Build URI from URL */
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

                    /*Re-initialize the RestTemplate with these codes*/
                    restTemplate = new RestTemplate();
                    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
                    mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
                    restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
                    /* End of RestTemplate */

                    ResponseEntity<BaseHWResponse> responseEntity;
                    responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, BaseHWResponse.class);
                    response = responseEntity.getBody();
                    LOGGER.info("======== RESPONSE PaymentServiceImpl.callbackStatusByXenditForLinkAja() - BaseHWResponse: " + mapper.writeValueAsString(response));

                    //If Response from HW is success, update status from Xendit to DB
                    if (response.getSuccess() == 0) {
                        payment.setPaymentStatus(GlobalConstants.TRX_STATUS_SETTLEMENT);
                        Date settlementDate = this.getDateBasedOnLocalZone();
                        payment.setSettlementTime(settlementDate);
                        //payment.setChannelTransactionId(); //LinkAja does not has Trx ID
                        crudService.updatePayment(payment);

                        //save also transaction detail
                        this.saveStoreTransactionDetail(request, orderDtl);

                        /* Response to Xendit */
                        baseResponse.setSuccess(true);
                        baseResponse.setResultCode("200");
                        baseResponse.setResultMsg("Payment callback has been processed successfully.");
                    } else { //{Pickup order failed
                        if (response.getResultMsg().equals("Dupilcated Order ")) {
                            LOGGER.info("======== PaymentServiceImpl.callbackStatusByXendit() - Repeat Duplicated Order");
                        } else {
                            //If there is an error from HW Server API, then mark the status as "undelivered"
                            payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNDELIVERED);
                            Date settlementDate = this.getDateBasedOnLocalZone();
                            payment.setSettlementTime(settlementDate);
                            //payment.setChannelTransactionId(); //LinkAja does not has Trx ID
                            payment.setErrorMessage(response.getResultCode().concat(" : ").concat(response.getResultMsg()));
                            crudService.updatePayment(payment);

                            /* Response to Xendit */
                            baseResponse.setSuccess(false);
                            baseResponse.setResultCode("412");
                            baseResponse.setResultMsg("Payment callback received with error in delivery (undelivered).");
                        }

                    }
                    LOGGER.info("======== PaymentServiceImpl.callbackStatusByXenditForLinkAja() - Payment order no " + payment.getOrderNo() + " has been updated to DB with status: " + payment.getPaymentStatus());
                } else {
                    //Xendit payment failed due to any reason. Update to DB
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                    payment.setErrorMessage("Transaksi gagal diproses di sisi LINKAJA (expired/canceled/failed). Check Xendit dashboard.");
                    crudService.updatePayment(payment);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            /* Set BAD response */
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("400");
            baseResponse.setResultMsg("Application Error : " + e.getMessage());
            return baseResponse;
        }

        LOGGER.info("======== COMPLETED PaymentServiceImpl.callbackStatusByXenditForLinkAja()");
        return baseResponse;
    }

    //GOPAY
    @Override
    public GopayCreatePaymentDTOResponse createGopayEWalletPayment(CreatePaymentDTORequest request) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.createGopayEWalletPayment() with request : " + request);
        GopayCreatePaymentDTOResponse paymentResponse = new GopayCreatePaymentDTOResponse();

        try {
            /* Define URL & headers */
            String ewalletBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_URL1);
            String url = ewalletBaseURL.concat("/charge");
            HttpHeaders headers = getGopayRequestHeaders();

            //Generate order no
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
            String orderNo = "operator".concat(formatter.format(new Date()));
            request.setExternalId(orderNo);

            /* Mapping HW charge request body to Gopay charge request body */
            GopayCreatePaymentDTORequest gopayChargeRequest = new GopayCreatePaymentDTORequest();
            gopayChargeRequest.setPaymentType(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_PROVIDER1));
            TransactionDetails trxDetail = new TransactionDetails();
            trxDetail.setOrderId(request.getExternalId());
            /*
				 * Commented this discount code because mobile has already pass the discounted amount
             */
 /*DiscountDTOResponse discRes = productService.getProductDiscount(request.getGoodsId());
				if (discRes != null && discRes.getResultCode().equals("200")) {
					BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
					BigDecimal discAmount = (request.getAmount().multiply(discInPercent)).divide(new BigDecimal(100));
					BigDecimal finalAmount = request.getAmount().subtract(discAmount);
					trxDetail.setGrossAmount(String.valueOf(finalAmount));
				}
				else {
					trxDetail.setGrossAmount(String.valueOf(request.getAmount()));
				}
				* End of Discount code
             */
            trxDetail.setGrossAmount(String.valueOf(request.getAmount()));
            gopayChargeRequest.setTransactionDetails(trxDetail);
            Gopay gopayCallback = new Gopay();
            Boolean gopayEnableCallback = Boolean.valueOf(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_ENABLE_CALLBACK1));
            gopayCallback.setEnableCallback(gopayEnableCallback);
            String gopayCallbackUrl = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_CALLBACK_URL1);
            String callbackSettledUrl = gopayCallbackUrl.concat("/").concat(request.getExternalId()); //TODO: Kayaknya perlu callback app, instead callback URL
            gopayCallback.setCallbackUrl(callbackSettledUrl);
            gopayChargeRequest.setGopay(gopayCallback);
            LOGGER.info("======== REQUEST PaymentServiceImpl.createGopayEWalletPayment(): " + mapper.writeValueAsString(gopayChargeRequest));
            /* End */

            HttpEntity<GopayCreatePaymentDTORequest> entity = new HttpEntity<>(gopayChargeRequest, headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<GopayCreatePaymentDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, GopayCreatePaymentDTOResponse.class);
            paymentResponse = responseEntity.getBody();
            LOGGER.info("======== RESPONSE PaymentServiceImpl.createGopayEWalletPayment() - XenditCreatePaymentDTOResponse: " + mapper.writeValueAsString(paymentResponse));

            if (responseEntity.getStatusCode() == HttpStatus.OK && paymentResponse != null) {
                paymentResponse.setSuccess(true); //0 = success
                paymentResponse.setResultCode("200"); //HTTP success code
                paymentResponse.setResultMsg("Gopay eWallet transaction is created");
                paymentResponse.setExternal_id(paymentResponse.getOrderId()); //only set this in gopay for mobile app purpose

                /* Saving payment data to DB */
                Transaction payment = new Transaction();
                this.setPaymentGopayObj(request, paymentResponse, payment);
                crudService.addPayment(payment);
                /* End */
            } else {
                paymentResponse.setSuccess(false);
                paymentResponse.setResultCode(paymentResponse.getStatusCode());
                paymentResponse.setResultMsg(paymentResponse.getStatusMessage());
            }
            LOGGER.debug("======== RESPONSE PaymentServiceImpl.createGopayEWalletPayment() - BaseHWResponse: "
                    + mapper.writeValueAsString(paymentResponse));
            /* End Mapping */
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            paymentResponse.setSuccess(false);
            paymentResponse.setResultCode("400");
            paymentResponse.setResultMsg(e.getMessage());
        }

        return paymentResponse;
    }

    @Override
    public ShopeepayCreatePaymentDTOResponse createShopeepayEWalletPayment(CreatePaymentDTORequest request) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.createShopeepayEWalletPayment() with request : " + request);
        ShopeepayCreatePaymentDTOResponse paymentResponse = new ShopeepayCreatePaymentDTOResponse();

        try {
            /* Define URL & headers */
            String ewalletBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_URL1);
            String url = ewalletBaseURL.concat("/charge");
            HttpHeaders headers = getGopayRequestHeaders();

            //Generate order no
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
            String orderNo = "operator".concat(formatter.format(new Date()));
            request.setExternalId(orderNo);

            /* Mapping HW charge request body to Gopay charge request body */
            ShopeepayCreatePaymentDTORequest shopeepayChargeRequest = new ShopeepayCreatePaymentDTORequest();
            shopeepayChargeRequest.setPaymentType(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_PROVIDER2));
            TransactionDetails trxDetail = new TransactionDetails();
            trxDetail.setOrderId(request.getExternalId());

            trxDetail.setGrossAmount(String.valueOf(request.getAmount()));
            shopeepayChargeRequest.setTransactionDetails(trxDetail);
            ShopeePay shopeepay = new ShopeePay();
            String gopayCallbackUrl = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_CALLBACK_URL1);
            String callbackSettledUrl = gopayCallbackUrl.concat("/").concat(request.getExternalId()); //TODO: Kayaknya perlu callback app, instead callback URL
            shopeepay.setCallbackUrl(callbackSettledUrl);
            shopeepayChargeRequest.setShopeepay(shopeepay);
            LOGGER.info("======== REQUEST PaymentServiceImpl.createShopeepayEWalletPayment(): " + mapper.writeValueAsString(shopeepayChargeRequest));
            /* End */

            HttpEntity<ShopeepayCreatePaymentDTORequest> entity = new HttpEntity<>(shopeepayChargeRequest, headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<ShopeepayCreatePaymentDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, ShopeepayCreatePaymentDTOResponse.class);
            paymentResponse = responseEntity.getBody();
            LOGGER.info("======== RESPONSE PaymentServiceImpl.createShopeepayEWalletPayment() - XenditCreatePaymentDTOResponse: " + mapper.writeValueAsString(paymentResponse));

            if (responseEntity.getStatusCode() == HttpStatus.OK && paymentResponse != null) {
                paymentResponse.setSuccess(true); //0 = success
                paymentResponse.setResultCode("200"); //HTTP success code
                paymentResponse.setResultMsg("Shopeepay eWallet transaction is created");
                paymentResponse.setExternalId(paymentResponse.getOrderId()); //only set this in gopay for mobile app purpose

                /* Saving payment data to DB */
                Transaction payment = new Transaction();
                this.setPaymentShopeepayObj(request, paymentResponse, payment);
                crudService.addPayment(payment);
                /* End */
            } else {
                paymentResponse.setSuccess(false);
                paymentResponse.setResultCode(paymentResponse.getStatusCode());
                paymentResponse.setResultMsg(paymentResponse.getStatusMessage());
            }
            LOGGER.debug("======== RESPONSE PaymentServiceImpl.createShopeepayEWalletPayment() - BaseHWResponse: "
                    + mapper.writeValueAsString(paymentResponse));
            /* End Mapping */
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            paymentResponse.setSuccess(false);
            paymentResponse.setResultCode("400");
            paymentResponse.setResultMsg(e.getMessage());
        }

        return paymentResponse;
    }

    @Override
    public MidtransResponse chargeMidtransPayment(ChargePaymentDTORequest request) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.chargeMidtransPayment() with request : " + request);
        MidtransResponse midtransResponse = new MidtransResponse();

        try {
            /* Define URL & headers */
            String ewalletBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_URL1);
            String url = ewalletBaseURL.concat("/charge");
            HttpHeaders headers = getGopayRequestHeaders();

            //Generate order no
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
            String orderNo = "operator".concat(formatter.format(new Date()));
            request.setExternalId(orderNo);

            /* Mapping HW charge request body to Gopay charge request body */
            GopayCreatePaymentDTORequest gopayChargeRequest = new GopayCreatePaymentDTORequest();
            gopayChargeRequest.setPaymentType(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_PROVIDER1));
            TransactionDetails trxDetail = new TransactionDetails();
            trxDetail.setOrderId(request.getExternalId());

            trxDetail.setGrossAmount(String.valueOf(request.getAmount()));
            gopayChargeRequest.setTransactionDetails(trxDetail);
            Gopay gopayCallback = new Gopay();
            Boolean gopayEnableCallback = Boolean.valueOf(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_ENABLE_CALLBACK1));
            gopayCallback.setEnableCallback(gopayEnableCallback);
            String gopayCallbackUrl = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_CALLBACK_URL1);
            String callbackSettledUrl = gopayCallbackUrl.concat("/").concat(request.getExternalId()); //TODO: Kayaknya perlu callback app, instead callback URL
            gopayCallback.setCallbackUrl(callbackSettledUrl);
            gopayChargeRequest.setGopay(gopayCallback);
            LOGGER.info("======== REQUEST PaymentServiceImpl.chargeMidtransPayment(): " + mapper.writeValueAsString(gopayChargeRequest));
            /* End */

            HttpEntity<GopayCreatePaymentDTORequest> entity = new HttpEntity<>(gopayChargeRequest, headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            ResponseEntity<MidtransResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, MidtransResponse.class);
            midtransResponse = responseEntity.getBody();
            LOGGER.info("======== RESPONSE PaymentServiceImpl.chargeMidtransPayment() - XenditCreatePaymentDTOResponse: " + mapper.writeValueAsString(midtransResponse));

            if (responseEntity.getStatusCode() == HttpStatus.OK && midtransResponse != null) {
                midtransResponse.setSuccess(true); //0 = success
                midtransResponse.setResultCode("200"); //HTTP success code
                midtransResponse.setResultMsg("Gopay eWallet transaction is created");
                midtransResponse.setExternal_id(midtransResponse.getOrderId()); //only set this in gopay for mobile app purpose

                /* Saving payment data to DB */
                Transaction payment = new Transaction();
                this.setNewPaymentGopayObj(request, midtransResponse, payment);
                crudService.addPayment(payment);
                /* End */
            } else {
                midtransResponse.setSuccess(false);
                midtransResponse.setResultCode(midtransResponse.getStatusCode());
                midtransResponse.setResultMsg(midtransResponse.getStatusMessage());
            }
            LOGGER.debug("======== RESPONSE PaymentServiceImpl.chargeMidtransPayment() - BaseHWResponse: "
                    + mapper.writeValueAsString(midtransResponse));
            /* End Mapping */
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            midtransResponse.setSuccess(false);
            midtransResponse.setResultCode("400");
            midtransResponse.setResultMsg(e.getMessage());
        }

        return midtransResponse;
    }

    @Override
    public BaseResponse callbackStatusByGopay(GopayCallbackDTORequest callbackRequest) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.callbackStatusByGopay() with request : " + mapper.writeValueAsString(callbackRequest));
        BaseHWResponse response = new BaseHWResponse();
        BaseResponse baseResponse = new BaseResponse();

        /*  Check whether the order no is already paid or not.
		 *  Only paid order no that will able to process this callback functionality
         */
        Transaction payment = crudService.getPaymentByOrderNo(callbackRequest.getOrderId()); //get stored transaction from DB by order no from Gopay callback
        if (payment == null) {
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("404");
            baseResponse.setResultMsg("Payment with order no. " + callbackRequest.getOrderId() + " not found in our database.");
            LOGGER.info("======== PaymentServiceImpl.callbackStatusByGopay() error response : " + mapper.writeValueAsString(baseResponse));
            return baseResponse;
        }

        try {
            /* Define URL & headers */
            String hwApiBaseUrl2 = crudService.getGlobalConfigParamByKey(GlobalConstants.CLIENT_URL2);
            String url = hwApiBaseUrl2.concat("/order");

            if (payment != null && payment.getPaymentStatus().equals("pending")) {
                //If this callback come from Midtrans then the status will be "settlement", but if from our scheduler, the status = "COMPLETED"
                if (callbackRequest.getTransactionStatus().equals("settlement") || callbackRequest.getTransactionStatus().equals("COMPLETED")) {
                    /* Set request parameter */
                    HWPickupOrderDTORequest request = new HWPickupOrderDTORequest();
                    request.setOrderNo(callbackRequest.getOrderId());
                    request.setOrderType("normal");
                    request.setOperateCode(crudService.getGlobalConfigParamByKey(GlobalConstants.OPERATE_CODE));
                    request.setPaymentTime(callbackRequest.getTransactionTime()); //gopay sudah dalam bentuk yyyy-MM-dd HH:mm:ss

                    //SET AMOUNT and DISCOUNT
                    DiscountDTOResponse discRes = productService.getProductDiscount(Integer.parseInt(payment.getGoodsCode()));
                    if (discRes != null && discRes.getResultCode().equals("200")) {
                        BigDecimal oriAmount = payment.getTotalFee();
                        BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
                        BigDecimal discAmount = (oriAmount.multiply(discInPercent)).divide(new BigDecimal(100));
                        BigDecimal finalAmount = oriAmount.subtract(discAmount);
                        request.setOriginalAmount(String.valueOf(oriAmount));
                        request.setDepositAmount(String.valueOf(discAmount));
                        request.setPaymentAmount(String.valueOf(finalAmount));
                    } else {
                        request.setOriginalAmount("");
                        request.setDepositAmount("");
                        request.setPaymentAmount(String.valueOf(payment.getTotalFee()));
                    }

                    request.setMachineCode(payment.getMachineCode());
                    request.setOrderSource(GlobalConstants.ORDER_SOURCE);

                    //Order request list
                    PickupOrderDetailRequest orderDtl = new PickupOrderDetailRequest();
                    orderDtl.setTasteid(String.valueOf(payment.getTasteId()));
                    orderDtl.setName(payment.getGoodsName());
                    orderDtl.setGoodid(payment.getGoodsCode());
                    //Generate order goods no
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
                    String randomString = RandomStringUtils.randomAlphabetic(4).toUpperCase();
                    String orderGoodsNo = "operate".concat(formatter.format(new Date())).concat("_").concat(randomString);
                    orderDtl.setOrderGoodsNo(orderGoodsNo);
                    orderDtl.setBrewingCode("");
                    orderDtl.setOrderPrice(request.getPaymentAmount());

                    List<PickupOrderDetailRequest> orderList = new ArrayList<>();
                    orderList.add(orderDtl);
                    request.setDetail(orderList);

                    /* Set header */
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                    LOGGER.info("======== START PaymentServiceImpl.callbackStatusByGopay() Order request to HW: " + mapper.writeValueAsString(request));

                    HttpEntity<HWPickupOrderDTORequest> entity = new HttpEntity<>(request, headers);

                    /* Build URI from URL */
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

                    /*Re-initialize the RestTemplate with these codes*/
                    restTemplate = new RestTemplate();
                    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
                    mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
                    restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
                    /* End of RestTemplate */

                    ResponseEntity<BaseHWResponse> responseEntity;
                    responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, BaseHWResponse.class);
                    response = responseEntity.getBody();
                    LOGGER.info("======== RESPONSE PaymentServiceImpl.callbackStatusByGopay() - BaseHWResponse: " + mapper.writeValueAsString(response));

                    //Update status from Gopay to DB
                    if (response.getSuccess() == 0) {
                        payment.setPaymentStatus(GlobalConstants.TRX_STATUS_SETTLEMENT);
//                        payment.setCallback(true);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        payment.setSettlementTime(sdf.parse(callbackRequest.getTransactionTime()));
                        crudService.updatePayment(payment);

                        //save also transaction detail
                        this.saveStoreTransactionDetail(request, orderDtl);

                        /* Response back to Midtrans */
                        baseResponse.setSuccess(true);
                        baseResponse.setResultCode("200");
                        baseResponse.setResultMsg("Payment callback has been processed successfully.");
                    } else { //{Pickup order failed
                        if (response.getResultMsg().equals("Dupilcated Order ")) {
                            LOGGER.info("======== PaymentServiceImpl.callbackStatusByXendit() - Repeat Duplicated Order");
                        } else {
                            //If there is an error from HW Server API, then mark the status as "undelivered"
                            payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNDELIVERED);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            payment.setSettlementTime(sdf.parse(callbackRequest.getTransactionTime()));
                            payment.setErrorMessage(response.getResultCode().concat(" : ").concat(response.getResultMsg()));
                            crudService.updatePayment(payment);

                            /* Response back to Midtrans */
                            baseResponse.setSuccess(false);
                            baseResponse.setResultCode("412");
                            baseResponse.setResultMsg("Payment callback received with error in delivery (undelivered).");
                        }

                    }
                    LOGGER.info("======== PaymentServiceImpl.callbackStatusByGopay() - Payment order no " + payment.getOrderNo() + " has been updated to DB with status: " + payment.getPaymentStatus());
                } //this else condition is based on https://docs.midtrans.com/en/after-payment/http-notification?id=status-definition
                else if (callbackRequest.getTransactionStatus().equals("deny")) {
                    //Gopay payment failed due to any reason. Update to DB
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                    payment.setErrorMessage(callbackRequest.getStatusMessage());
                    crudService.updatePayment(payment);
                } else if (callbackRequest.getTransactionStatus().equals("cancel")) {
                    //Gopay payment failed due to any reason. Update to DB
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                    payment.setErrorMessage("The transaction is canceled.");
                    crudService.updatePayment(payment);
                } else if (callbackRequest.getTransactionStatus().equals("expire")) {
                    //Gopay payment failed due to any reason. Update to DB
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                    payment.setErrorMessage("Transaction is expired, because the payment was delayed.");
                    crudService.updatePayment(payment);
                } else if (callbackRequest.getTransactionStatus().equals("FAILED")) { //from scheduler
                    //Gopay payment failed due to any reason. Update to DB
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                    payment.setErrorMessage(callbackRequest.getStatusMessage());
                    crudService.updatePayment(payment);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            /* Set BAD response */
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("400");
            baseResponse.setResultMsg("Application Error : " + e.getMessage());
            return baseResponse;
        }

        LOGGER.info("======== COMPLETED PaymentServiceImpl.callbackStatusByGopay()");
        return baseResponse;
    }

    //END OF GOPAY
    @Override
    public OrderListDTOResponse getListOfOrders(long customerId) throws Exception {
        OrderListDTOResponse response = new OrderListDTOResponse();
        LOGGER.info("======== START PaymentServiceImpl.getListOfOrders()");
        List<Transaction> orderList = paymentRepo.findOrderList(customerId);
        Customer customer = customerRepo.getOne(customerId);

        if (orderList != null && !orderList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<OrderDTOResponse> orderResList = new ArrayList<>();
            for (Transaction trx : orderList) {
                OrderDTOResponse orderRes = new OrderDTOResponse();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				                        System.out.println(trx.getCustomerId());
                orderRes.setPaymentDate(sdf.format(trx.getChannelTransactionTime()));

                switch (trx.getPaymentStatus()) {
                    case GlobalConstants.TRX_STATUS_SETTLEMENT:
                        orderRes.setPaymentStatus("Pickup");
                        break;
                    case GlobalConstants.TRX_STATUS_PICKUPEXPIRED:
                        orderRes.setPaymentStatus("Expired");
                        break;
                    case GlobalConstants.TRX_STATUS_COMPLETED:
                        orderRes.setPaymentStatus("Completed");
                        break;
                    default:
                        break;
                }

                orderRes.setOrderNo(trx.getOrderNo());
                orderRes.setProductName(trx.getGoodsName());
                orderRes.setPrice(String.valueOf(trx.getTotalFee()));
                orderRes.setMachineCode(trx.getMachineCode());
                Machine machine = machineRepo.findByMachineCode(trx.getMachineCode());
                orderRes.setMachineLocation(machine.getLocation());
                orderRes.setEWalletType(trx.getEwalletType());

                Date trxDate = trx.getChannelTransactionTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(trxDate);
                calendar.add(Calendar.HOUR_OF_DAY, 24); //add 24 jam dari trx date
                orderRes.setExpiryTime(sdf.format(calendar.getTime()));
                //TODO: bikin status baru = pickup_expired

                orderRes.setId(customer.getId());
                orderRes.setFirstName(customer.getFirstName());
                orderRes.setLastName(customer.getLastName());
                orderRes.setMobileNo(customer.getMobileNo());

                orderResList.add(orderRes);
            }

            response.setOrderList(orderResList);
            /* Response to Xendit */
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Order list retrieved successfully.");

            LOGGER.info("======== PaymentServiceImpl.getListOfOrders() - response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("There are no any orders right now");
            LOGGER.info("======== PaymentServiceImpl.getListOfOrders() - No orders available");
        }
        return response;
    }

    @Override
    public OrderListDTOResponse getListOfOrdersByStatus(long customerId, String status) throws Exception {
        OrderListDTOResponse response = new OrderListDTOResponse();
        LOGGER.info("======== START PaymentServiceImpl.getListOfOrdersByStatus() for customer ID: " + customerId + " with status: " + status);
        switch (status) {
            case "Pickup":
                status = GlobalConstants.TRX_STATUS_SETTLEMENT;
                break;
            case "Expired":
                status = GlobalConstants.TRX_STATUS_PICKUPEXPIRED;
                break;
            case "Completed":
                status = GlobalConstants.TRX_STATUS_COMPLETED;
                break;
            default:
                break;
        }

        List<Transaction> orderList = paymentRepo.findOrderListByStatus(customerId, status);

        Customer customer = customerRepo.getOne(customerId);

        if (orderList != null && !orderList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<OrderDTOResponse> orderResList = new ArrayList<>();
            for (Transaction trx : orderList) {
                OrderDTOResponse orderRes = new OrderDTOResponse();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                orderRes.setPaymentDate(sdf.format(trx.getChannelTransactionTime()));

                switch (trx.getPaymentStatus()) {
                    case GlobalConstants.TRX_STATUS_SETTLEMENT:
                        orderRes.setPaymentStatus("Pickup");
                        break;
                    case GlobalConstants.TRX_STATUS_PICKUPEXPIRED:
                        orderRes.setPaymentStatus("Expired");
                        break;
                    case GlobalConstants.TRX_STATUS_COMPLETED:
                        orderRes.setPaymentStatus("Completed");
                        break;
                    default:
                        break;
                }

                orderRes.setOrderNo(trx.getOrderNo());
                orderRes.setProductName(trx.getGoodsName());
                orderRes.setPrice(String.valueOf(trx.getTotalFee()));
                orderRes.setMachineCode(trx.getMachineCode());
                Machine machine = machineRepo.findByMachineCode(trx.getMachineCode());
                orderRes.setMachineLocation(machine.getLocation());
                orderRes.setEWalletType(trx.getEwalletType());

                Date trxDate = trx.getChannelTransactionTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(trxDate);
                calendar.add(Calendar.HOUR_OF_DAY, 24); //add 24 jam dari trx date
                orderRes.setExpiryTime(sdf.format(calendar.getTime()));
                //TODO: bikin status baru = pickup_expired

                orderRes.setId(customer.getId());
                orderRes.setFirstName(customer.getFirstName());
                orderRes.setLastName(customer.getLastName());
                orderRes.setMobileNo(customer.getMobileNo());

                orderResList.add(orderRes);
            }

            response.setOrderList(orderResList);
            /* Response to Xendit */
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Order list retrieved successfully.");

            LOGGER.info("======== PaymentServiceImpl.getListOfOrdersByStatus() - response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("There are no any orders right now");
            LOGGER.info("======== PaymentServiceImpl.getListOfOrdersByStatus() - No orders available");
        }
        return response;
    }

    @Override
    public BaseResponse startBrewing(PickupDTORequest request) throws Exception {
        LOGGER.info("======== START PaymentServiceImpl.startBrewing() with request : " + mapper.writeValueAsString(request));
        BaseHWResponse response = new BaseHWResponse();
        BaseResponse baseResponse = new BaseResponse();

        /*  Check whether the customer is coming to the correct machine by checking machine QR code
         */
        Transaction payment = crudService.getPaymentByOrderNo(request.getOrderNo());
        if (payment == null) {
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("404");
            baseResponse.setResultMsg("Payment with order no. " + request.getOrderNo() + " not found in our database.");
            LOGGER.info("======== PaymentServiceImpl.startBrewing() error response : " + mapper.writeValueAsString(baseResponse));
            return baseResponse;
        } else {
            if (request.getMachineCode().length() <= 15) {
                if (!request.getMachineCode().equals(payment.getMachineCode())) {
                    //means customer is coming to the wrong machine
                    baseResponse.setSuccess(false);
                    baseResponse.setResultCode("412");
                    baseResponse.setResultMsg("You are coming to the wrong machine. Please check your order & visit the correct machine.");
                    LOGGER.info("======== PaymentServiceImpl.startBrewing() error response : " + mapper.writeValueAsString(baseResponse));
                    return baseResponse;
                }
            } else {
                if (!request.getMachineCode().contains(payment.getMachineCode())) { //full QR text
                    //means customer is coming to the wrong machine
                    baseResponse.setSuccess(false);
                    baseResponse.setResultCode("412");
                    baseResponse.setResultMsg("You are coming to the wrong machine. Please check your order & visit the correct machine.");
                    LOGGER.info("======== PaymentServiceImpl.startBrewing() error response : " + mapper.writeValueAsString(baseResponse));
                    return baseResponse;
                }
            }
        }

        try {
            /* Define URL & headers */
            String hwApiBaseUrl2 = crudService.getGlobalConfigParamByKey(GlobalConstants.CLIENT_URL1);
            String url = hwApiBaseUrl2.concat("/brewingnow");

            /* Set request parameter */
            HWBrewingDTORequest hwRequest = new HWBrewingDTORequest();
            StoreTransactionDetail storeTrxDtl = crudService.getStoreTransactionDetailByOrderNo(request.getOrderNo());
            hwRequest.setOrderGoodsNo(storeTrxDtl.getOrderGoodsNo());
            hwRequest.setOperateCode(crudService.getGlobalConfigParamByKey(GlobalConstants.OPERATE_CODE));
            hwRequest.setMachineCode(payment.getMachineCode());

            /* Set header */
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity<HWBrewingDTORequest> entity = new HttpEntity<>(hwRequest, headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            /*Re-initialize the RestTemplate with these codes*/
            restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
            restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
            /* End of RestTemplate */

            ResponseEntity<BaseHWResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, BaseHWResponse.class);
            response = responseEntity.getBody();
            LOGGER.info("======== RESPONSE PaymentServiceImpl.startBrewing() - BaseHWResponse: " + mapper.writeValueAsString(response));

            //Update status to DB
            if (response.getSuccess() == 0) {
                payment.setPaymentStatus(GlobalConstants.TRX_STATUS_COMPLETED);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //payment.setPickupTime();
                crudService.updatePayment(payment);

                /* Response */
                baseResponse.setSuccess(true);
                baseResponse.setResultCode("200");
                baseResponse.setResultMsg("Payment callback has been processed successfully.");
            } else { //{Pickup order failed
                //If there is an error from HW Server API, then mark the status as "undelivered"
                payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNDELIVERED);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //payment.setPickupTime();
                payment.setErrorMessage(response.getResultCode().concat(" : ").concat(response.getResultMsg()));
                crudService.updatePayment(payment);

                /* Response */
                baseResponse.setSuccess(false);
                baseResponse.setResultCode("412");
                baseResponse.setResultMsg("Payment callback received with error in delivery (undelivered).");
            }
            LOGGER.info("======== PaymentServiceImpl.startBrewing() - Payment order no " + payment.getOrderNo() + " has been picked-up and updated to DB with status: " + payment.getPaymentStatus());
        } catch (JsonProcessingException | RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            /* Set BAD response */
            baseResponse.setSuccess(false);
            baseResponse.setResultCode("400");
            baseResponse.setResultMsg("Application Error : " + e.getMessage());
            return baseResponse;
        }

        LOGGER.info("======== COMPLETED PaymentServiceImpl.startBrewing()");
        return baseResponse;
    }

    /*
    * JOB
    * This method will be called by scheduler to expire pickup order if more than specified hours
     */
    @Override
    public void expirePickupOrder() throws Exception {
        List<Transaction> settlementPayments = crudService.getPaymentByStatusAndTouchpoint(GlobalConstants.TRX_STATUS_SETTLEMENT, GlobalConstants.MOBILE);

        if (!settlementPayments.isEmpty()) {
            for (Transaction payment : settlementPayments) {
                Date trxDate = payment.getChannelTransactionTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(trxDate);
                calendar.add(Calendar.HOUR_OF_DAY, 24); //add 24 jam dari trx date
                Date expireDate = calendar.getTime();
                Date today = this.getDateBasedOnLocalZone();
                if (today.after(expireDate)) {
                    LOGGER.info("\n\n>>>> START SCHEDULER - Order No : " + payment.getOrderNo() + ", Today : " + today + " & expire date : " + expireDate);
                    payment.setPaymentStatus(GlobalConstants.TRX_STATUS_PICKUPEXPIRED);
                    crudService.updatePayment(payment);
                    LOGGER.info("\n\n>>>> END SCHEDULER - Updated order no " + payment.getOrderNo() + " to pickup_expired");
                }
            }
        }
    }

    /*
	 * JOB
	 * This method will be called by scheduler to check transaction status from ewallet and update it to DB
     */
    @Override
    public void checkAndUpdateTransactionStatus() throws Exception {
        //Get data from minus 1 hour before
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();
//        oneHourBack = this.getDateBasedOnLocalZone();
        List<Transaction> pendingPayments = crudService.getPaymentListByStatusAndTime(GlobalConstants.TRX_STATUS_PENDING, oneHourBack);

        if (!pendingPayments.isEmpty()) {
            for (Transaction payment : pendingPayments) {
//                if (payment.getCallback() == false) {
                switch (payment.getChannelType()) {
                    case "midtrans":
                        /* Call Gopay transaction status */
                        TrxStatusDTOResponse statusResponse = this.getGopayTrxStatus(payment.getChannelTransactionId(), GlobalConstants.PAYMENT_STATUS_TRIGGER_JOB);
                        if (statusResponse.getStatus() != null) {
                            if (statusResponse.getStatus().equals("COMPLETED")) {
                                LOGGER.info("\n\n>>>> [SCHEDULER] Payment Scheduler is running to process settlement for order " + payment.getOrderNo());
                                if (payment.getEwalletType().equals("GOPAY")) {
                                    GopayCallbackDTORequest callbackRequest = new GopayCallbackDTORequest();
                                    callbackRequest.setTransactionTime(statusResponse.getTransactionDate());
                                    callbackRequest.setTransactionStatus(statusResponse.getStatus());
                                    callbackRequest.setOrderId(statusResponse.getExternalId());
                                    //Use callback method for GOPAY to update the status to DB & call HW to submit the order
                                    this.callbackStatusByGopay(callbackRequest);
                                } else if (payment.getEwalletType().equals("SHOPEEPAY")) {
                                    GopayCallbackDTORequest callbackRequest = new GopayCallbackDTORequest();
                                    callbackRequest.setTransactionTime(statusResponse.getTransactionDate());
                                    callbackRequest.setTransactionStatus(statusResponse.getStatus());
                                    callbackRequest.setOrderId(statusResponse.getExternalId());
                                    //Use callback method for GOPAY to update the status to DB & call HW to submit the order
                                    this.callbackStatusByGopay(callbackRequest);
                                }
                                LOGGER.info("======== [SCHEDULER] PaymentServiceImpl.checkAndUpdateTransactionStatus() - Payment order no " + payment.getOrderNo() + " has been updated to DB with status: " + payment.getPaymentStatus());
                            } //this else condition is based on https://docs.midtrans.com/en/after-payment/http-notification?id=status-definition
                            else if (statusResponse.getStatus().equals("FAILED")) {
                                //Gopay payment failed due to any reason. Update to DB
                                payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                                payment.setErrorMessage("The transaction is unpaid due to cancelled/expired/failed on eWallet side");
                                crudService.updatePayment(payment);
                            }
                        }
                        break;
                    case "xendit":
                        /* Call Xendit transaction status */
//                        TrxStatusDTOResponse xenditStatusResponse = this.getNewXenditTrxStatus(payment.getChannelOrderId());
                        TrxStatusDTOResponse xenditStatusResponse = this.getXenditTrxStatus(payment.getChannelOrderId(), payment.getEwalletType());
                        if (xenditStatusResponse.getStatus() != null) {
                            if (xenditStatusResponse.getStatus().equals("COMPLETED")) {
                                switch (payment.getEwalletType()) {
                                    case "OVO":
                                        XenditGeneralCallbackDTORequest ovoCallbackReq = new XenditGeneralCallbackDTORequest();
                                        ovoCallbackReq.setEvent(null);
                                        ovoCallbackReq.setId(xenditStatusResponse.getBusinessId());
                                        ovoCallbackReq.setExternalId(xenditStatusResponse.getExternalId());
                                        ovoCallbackReq.setBusinessId(xenditStatusResponse.getBusinessId());
                                        ovoCallbackReq.setPhone(null);
                                        ovoCallbackReq.setEwalletType(payment.getEwalletType());
                                        ovoCallbackReq.setAmount(xenditStatusResponse.getAmount());
                                        ovoCallbackReq.setCreated(xenditStatusResponse.getTransactionDate());
                                        ovoCallbackReq.setStatus(xenditStatusResponse.getStatus());
                                        //Use callback method for OVO to update the status to DB & call HW to submit the order
                                        this.callbackStatusByXendit(ovoCallbackReq);
                                        break;
                                    case "DANA": {
                                        XenditGeneralCallbackDTORequest callbackReq = new XenditGeneralCallbackDTORequest();
                                        callbackReq.setExternalId(xenditStatusResponse.getExternalId());
                                        callbackReq.setAmount(xenditStatusResponse.getAmount());
                                        callbackReq.setBusinessId(xenditStatusResponse.getBusinessId());
                                        callbackReq.setPaymentStatus(xenditStatusResponse.getStatus());
                                        if (xenditStatusResponse.getTransactionDate() != null) {
                                            callbackReq.setTransactionDate(xenditStatusResponse.getTransactionDate());
                                        } else {
                                            callbackReq.setTransactionDate(xenditStatusResponse.getExpirationDate());
                                        }
                                        callbackReq.setEwalletType(payment.getEwalletType());
                                        callbackReq.setCallbackAuthenticationToken("");
                                        //Use callback method for DANA to update the status to DB & call HW to submit the order
                                        this.callbackStatusByXenditForDana(callbackReq);
                                        break;
                                    }
                                    case "LINKAJA": {
                                        XenditGeneralCallbackDTORequest callbackReq = new XenditGeneralCallbackDTORequest();
                                        callbackReq.setExternalId(xenditStatusResponse.getExternalId());
                                        callbackReq.setAmount(xenditStatusResponse.getAmount());
                                        callbackReq.setStatus(xenditStatusResponse.getStatus());
                                        callbackReq.setEwalletType(payment.getEwalletType());
                                        callbackReq.setTransactionDate(xenditStatusResponse.getPaymentTimestamp());
                                        callbackReq.setCallbackAuthenticationToken("");
                                        //Use callback method for LINKAJA to update the status to DB & call HW to submit the order
                                        this.callbackStatusByXenditForLinkAja(callbackReq);
                                        break;
                                    }
                                    default:
                                        break;
                                }

                                LOGGER.info("======== [SCHEDULER] PaymentServiceImpl.checkAndUpdateTransactionStatus() - Payment order no " + payment.getOrderNo() + " has been updated to DB with status: " + payment.getPaymentStatus());
                            } else if (xenditStatusResponse.getStatus().equals("FAILED")) {
                                //TAMBAH FAILURE CODE
                                //Xendit payment failed due to any reason. Update to DB
                                payment.setPaymentStatus(GlobalConstants.TRX_STATUS_UNPAID);
                                payment.setErrorMessage("The transaction is unpaid due to cancelled/expired/failed on eWallet side");
                                crudService.updatePayment(payment);
                            }
                        }
                        break;
                    case "heiya":
                        /* Call Xendit transaction status */
                        if (payment.getEwalletType().equals("free_payment")) {
                            HeiyaGeneralCallbackDTORequest heiyaCallbackReq = new HeiyaGeneralCallbackDTORequest();
//                        heiyaCallbackReq.setEvent(null);
//                        heiyaCallbackReq.setId(xenditStatusResponse.getBusinessId());
                            heiyaCallbackReq.setExternalId(payment.getOrderNo());
//                        heiyaCallbackReq.setBusinessId(xenditStatusResponse.getBusinessId());
//                        heiyaCallbackReq.setPhone(null);
//                        heiyaCallbackReq.setEwalletType(payment.getEwalletType());
//                        heiyaCallbackReq.setAmount(xenditStatusResponse.getAmount());
                            heiyaCallbackReq.setCreated(payment.getChannelTransactionTime().toString());
//                        heiyaCallbackReq.setStatus(xenditStatusResponse.getStatus());
//Use callback method for OVO to update the status to DB & call HW to submit the order
                            this.callbackStatusByHeiya(heiyaCallbackReq);
                        }
                        LOGGER.info("======== [SCHEDULER] PaymentServiceImpl.checkAndUpdateTransactionStatus() - Payment order no " + payment.getOrderNo() + " has been updated to DB with status: " + payment.getPaymentStatus());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void setPaymentObj(CreatePaymentDTORequest req, XenditCreatePaymentDTOResponse res, Transaction payment) throws Exception {
        payment.setOrderNo(req.getExternalId());
        payment.setMachineCode(req.getMachineCode());
        payment.setTotalFee(req.getAmount());
        DiscountDTOResponse discRes = productService.getProductDiscount(req.getGoodsId());
        if (discRes != null && discRes.getResultCode().equals("200")) {
            BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
            BigDecimal discAmount = (req.getAmount().multiply(discInPercent)).divide(new BigDecimal(100));
            payment.setDiscount(discAmount);
        } else {
            payment.setDiscount(new BigDecimal(0));
        }
        payment.setGoodsCode(String.valueOf(req.getGoodsId()));
        payment.setGoodsProtocol(String.valueOf(req.getGoodsProtocol()));
        payment.setGoodsName(req.getProductName());
        //payment.setChannelType(this.PAYMENTTYPE);
        payment.setTasteId(req.getTasteId());
        payment.setTransactionTouchpoint(GlobalConstants.MOBILE);
        payment.setChannelType(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_PROVIDER));
        payment.setEwalletType(res.getEwalletType());

        String phoneNo = req.getPhone();
        if (phoneNo.startsWith("+62")) {
            phoneNo = phoneNo.replace("+62", "0");
        }
        Customer customer = customerRepo.findByMobileNo(phoneNo); //check lg format no
        payment.setCustomerId(customer.getId());

        payment.setChannelTransactionId(res.getBusinessId());
        payment.setChannelOrderId(req.getExternalId());
        payment.setChannelCurrency(GlobalConstants.EWALLET_BASE_CURRENCY);
        payment.setChannelTransactionTime(formatUTCDateToLocalZone(res.getCreated()));
        payment.setChannelFraudStatus(null);
        if (res.getStatus().equals("PENDING")) {
            payment.setPaymentStatus("pending");
        } else if (res.getStatus().equals("ACTIVE")) {
            payment.setPaymentStatus("settlement");
        }

        payment.setSysUpdateTime(new Date());

    }

    public void setNewPaymentObj(ChargePaymentDTORequest req, XenditResponse res, Transaction payment) throws Exception {
        switch (req.getEwalletType()) {
            case GlobalConstants.EWALLET_TYPE_OVO_NEW:
                req.setEwalletType(GlobalConstants.EWALLET_TYPE_OVO);
                break;
            case GlobalConstants.EWALLET_TYPE_DANA_NEW:
                req.setEwalletType(GlobalConstants.EWALLET_TYPE_DANA);
                break;
            case GlobalConstants.EWALLET_TYPE_LINKAJA_NEW:
                req.setEwalletType(GlobalConstants.EWALLET_TYPE_LINKAJA);
                break;
            case GlobalConstants.EWALLET_TYPE_SHOPEEPAY_NEW:
                req.setEwalletType(GlobalConstants.EWALLET_TYPE_SHOPEEPAY);
                break;
            default:
                break;
        }
        payment.setOrderNo(req.getExternalId());
        payment.setMachineCode(req.getMachineCode());
        payment.setTotalFee(req.getAmount());
        DiscountDTOResponse discRes = productService.getProductDiscount(req.getGoodsId());
        if (discRes != null && discRes.getResultCode().equals("200")) {
            BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
            BigDecimal discAmount = (req.getAmount().multiply(discInPercent)).divide(new BigDecimal(100));
            payment.setDiscount(discAmount);
        } else {
            payment.setDiscount(new BigDecimal(0));
        }
        payment.setGoodsCode(String.valueOf(req.getGoodsId()));
        payment.setGoodsProtocol(String.valueOf(req.getGoodsProtocol()));
        payment.setGoodsName(req.getProductName());
        //payment.setChannelType(this.PAYMENTTYPE);
        payment.setTasteId(req.getTasteId());
        payment.setTransactionTouchpoint(GlobalConstants.MOBILE);
        payment.setChannelType(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_PROVIDER));
        payment.setEwalletType(req.getEwalletType());

        String phoneNo = req.getPhone();
        if (phoneNo.startsWith("+62")) {
            phoneNo = phoneNo.replace("+62", "0");
        }
        Customer customer = customerRepo.findByMobileNo(phoneNo); //check lg format no
        payment.setCustomerId(customer.getId());

        payment.setChannelTransactionId(res.getBusinessId());
        payment.setChannelOrderId(res.getId());
        payment.setChannelCurrency(GlobalConstants.EWALLET_BASE_CURRENCY);
        payment.setChannelTransactionTime(formatUTCDateToLocalZone(res.getCreated()));
        payment.setChannelFraudStatus(null);
        if (res.getStatus().equals("PENDING")) {
            payment.setPaymentStatus("pending");
        } else if (res.getStatus().equals("ACTIVE")) {
            payment.setPaymentStatus("settlement");
        }

        payment.setSysUpdateTime(new Date());

    }

    public void setPaymentDanaAndLinkAjaObj(CreatePaymentDTORequest req, XenditCreatePaymentDTOResponse res, Transaction payment) throws Exception {
        payment.setOrderNo(req.getExternalId());
        payment.setMachineCode(req.getMachineCode());
        payment.setTotalFee(req.getAmount());
        DiscountDTOResponse discRes = productService.getProductDiscount(req.getGoodsId());
        if (discRes != null && discRes.getResultCode().equals("200")) {
            BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
            BigDecimal discAmount = (req.getAmount().multiply(discInPercent)).divide(new BigDecimal(100));
            payment.setDiscount(discAmount);
        } else {
            payment.setDiscount(new BigDecimal(0));
        }
        payment.setGoodsCode(String.valueOf(req.getGoodsId()));
        payment.setGoodsProtocol(String.valueOf(req.getGoodsProtocol()));
        payment.setGoodsName(req.getProductName());
        //payment.setChannelType(this.PAYMENTTYPE);
        payment.setTasteId(req.getTasteId());
        payment.setTransactionTouchpoint(GlobalConstants.MOBILE);
        payment.setChannelType(crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_PROVIDER));
        payment.setEwalletType(res.getEwalletType());

        String phoneNo = req.getPhone();
        if (phoneNo.startsWith("+62")) {
            phoneNo = phoneNo.replace("+62", "0");
        }
        Customer customer = customerRepo.findByMobileNo(phoneNo); //check lg format no
        payment.setCustomerId(customer.getId());

        //payment.setChannelTransactionId(res.getBusinessId()); //Dana doesn't has transaction ID
        payment.setChannelOrderId(req.getExternalId()); //not needed in this payment
        payment.setChannelCurrency(GlobalConstants.EWALLET_BASE_CURRENCY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        payment.setChannelTransactionTime(formatUTCDateToLocalZone(sdf.format(new Date()))); //Dana doesn't has transaction time in his payment response
        payment.setPaymentStatus("pending");

        payment.setChannelDeeplink(res.getCheckoutUrl());
        payment.setSysUpdateTime(new Date());

    }

    public void setPaymentGopayObj(CreatePaymentDTORequest req, GopayCreatePaymentDTOResponse res, Transaction payment) throws Exception {
        payment.setOrderNo(req.getExternalId());
        payment.setMachineCode(req.getMachineCode());
        payment.setTotalFee(req.getAmount());
        DiscountDTOResponse discRes = productService.getProductDiscount(req.getGoodsId());
        if (discRes != null && discRes.getResultCode().equals("200")) {
            BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
            BigDecimal discAmount = (req.getAmount().multiply(discInPercent)).divide(new BigDecimal(100));
            payment.setDiscount(discAmount);
        } else {
            payment.setDiscount(new BigDecimal(0));
        }
        payment.setGoodsCode(String.valueOf(req.getGoodsId()));
        payment.setGoodsProtocol(String.valueOf(req.getGoodsProtocol()));
        payment.setGoodsName(req.getProductName());
        //payment.setChannelType(this.PAYMENTTYPE);
        payment.setTasteId(req.getTasteId());
        payment.setTransactionTouchpoint(GlobalConstants.MOBILE);
        payment.setChannelType("midtrans");
        payment.setEwalletType(res.getPaymentType().toUpperCase());

        String phoneNo = req.getPhone();
        if (phoneNo.startsWith("+62")) {
            phoneNo = phoneNo.replace("+62", "0");
        }
        Customer customer = customerRepo.findByMobileNo(phoneNo); //check lg format no
        payment.setCustomerId(customer.getId());

        payment.setChannelTransactionId(res.getTransactionId());
        payment.setChannelOrderId(req.getExternalId());
        payment.setChannelCurrency(GlobalConstants.EWALLET_BASE_CURRENCY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        payment.setChannelTransactionTime(sdf.parse(res.getTransactionTime()));
        payment.setChannelFraudStatus(res.getFraudStatus());
        payment.setPaymentStatus(res.getTransactionStatus());

        if (res.getActions() != null) {
            List<Action> actions = res.getActions();
            for (int i = 0; i < actions.size(); i++) {
                if (actions.get(i).getName().equals("generate-qr-code")) {
                    /*Get QR image URL from Gopay response*/
                    payment.setChannelQrCode(actions.get(i).getUrl());
                } else if (actions.get(i).getName().equals("deeplink-redirect")) {
                    /*Get Deeplink-redirect URL from Gopay response*/
                    payment.setChannelDeeplink(actions.get(i).getUrl());
                }
            }
        }

        payment.setSysUpdateTime(new Date());

    }

    public void setPaymentShopeepayObj(CreatePaymentDTORequest req, ShopeepayCreatePaymentDTOResponse res, Transaction payment) throws Exception {
        payment.setOrderNo(req.getExternalId());
        payment.setMachineCode(req.getMachineCode());
        payment.setTotalFee(req.getAmount());
        DiscountDTOResponse discRes = productService.getProductDiscount(req.getGoodsId());
        if (discRes != null && discRes.getResultCode().equals("200")) {
            BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
            BigDecimal discAmount = (req.getAmount().multiply(discInPercent)).divide(new BigDecimal(100));
            payment.setDiscount(discAmount);
        } else {
            payment.setDiscount(new BigDecimal(0));
        }
        payment.setGoodsCode(String.valueOf(req.getGoodsId()));
        payment.setGoodsProtocol(String.valueOf(req.getGoodsProtocol()));
        payment.setGoodsName(req.getProductName());
        //payment.setChannelType(this.PAYMENTTYPE);
        payment.setTasteId(req.getTasteId());
        payment.setTransactionTouchpoint(GlobalConstants.MOBILE);
        payment.setChannelType("midtrans");
        payment.setEwalletType(res.getPaymentType().toUpperCase());

        String phoneNo = req.getPhone();
        if (phoneNo.startsWith("+62")) {
            phoneNo = phoneNo.replace("+62", "0");
        }
        Customer customer = customerRepo.findByMobileNo(phoneNo); //check lg format no
        payment.setCustomerId(customer.getId());

        payment.setChannelTransactionId(res.getTransactionId());
        payment.setChannelOrderId(req.getExternalId());
        payment.setChannelCurrency(GlobalConstants.EWALLET_BASE_CURRENCY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        payment.setChannelTransactionTime(sdf.parse(res.getTransactionTime()));
        payment.setChannelFraudStatus(res.getFraudStatus());
        payment.setPaymentStatus(res.getTransactionStatus());

        if (res.getActions() != null) {
            List<Action> actions = res.getActions();
            for (int i = 0; i < actions.size(); i++) {
                if (actions.get(i).getName().equals("generate-qr-code")) {
                    /*Get QR image URL from Gopay response*/
                    payment.setChannelQrCode(actions.get(i).getUrl());
                } else if (actions.get(i).getName().equals("deeplink-redirect")) {
                    /*Get Deeplink-redirect URL from Gopay response*/
                    payment.setChannelDeeplink(actions.get(i).getUrl());
                }
            }
        }

        payment.setSysUpdateTime(new Date());

    }

    public void setNewPaymentGopayObj(ChargePaymentDTORequest req, MidtransResponse res, Transaction payment) throws Exception {
        payment.setOrderNo(req.getExternalId());
        payment.setMachineCode(req.getMachineCode());
        payment.setTotalFee(req.getAmount());
        DiscountDTOResponse discRes = productService.getProductDiscount(req.getGoodsId());
        if (discRes != null && discRes.getResultCode().equals("200")) {
            BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
            BigDecimal discAmount = (req.getAmount().multiply(discInPercent)).divide(new BigDecimal(100));
            payment.setDiscount(discAmount);
        } else {
            payment.setDiscount(new BigDecimal(0));
        }
        payment.setGoodsCode(String.valueOf(req.getGoodsId()));
        payment.setGoodsProtocol(String.valueOf(req.getGoodsProtocol()));
        payment.setGoodsName(req.getProductName());
        //payment.setChannelType(this.PAYMENTTYPE);
        payment.setTasteId(req.getTasteId());
        payment.setTransactionTouchpoint(GlobalConstants.MOBILE);
        payment.setChannelType("midtrans");
        payment.setEwalletType(res.getPaymentType().toUpperCase());

        String phoneNo = req.getPhone();
        if (phoneNo.startsWith("+62")) {
            phoneNo = phoneNo.replace("+62", "0");
        }
        Customer customer = customerRepo.findByMobileNo(phoneNo); //check lg format no
        payment.setCustomerId(customer.getId());

        payment.setChannelTransactionId(res.getTransactionId());
        payment.setChannelOrderId(req.getExternalId());
        payment.setChannelCurrency(GlobalConstants.EWALLET_BASE_CURRENCY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        payment.setChannelTransactionTime(sdf.parse(res.getTransactionTime()));
        payment.setChannelFraudStatus(res.getFraudStatus());
        payment.setPaymentStatus(res.getTransactionStatus());

        if (res.getActions() != null) {
            List<Action> actions = res.getActions();
            for (int i = 0; i < actions.size(); i++) {
                if (actions.get(i).getName().equals("generate-qr-code")) {
                    /*Get QR image URL from Gopay response*/
                    payment.setChannelQrCode(actions.get(i).getUrl());
                } else if (actions.get(i).getName().equals("deeplink-redirect")) {
                    /*Get Deeplink-redirect URL from Gopay response*/
                    payment.setChannelDeeplink(actions.get(i).getUrl());
                }
            }
        }

        payment.setSysUpdateTime(new Date());

    }

    public void saveStoreTransactionDetail(HWPickupOrderDTORequest request, PickupOrderDetailRequest orderDtl) throws Exception {
        StoreTransactionDetail storeTrx = new StoreTransactionDetail();

        StoreTransactionDetail trxDtlExist = crudService.getStoreTransactionDetailByOrderNo(request.getOrderNo());
        if (trxDtlExist != null) {
            //order detail is already exist, update it instead of insert new (must be unique)
            storeTrx.setId(trxDtlExist.getId());
        }

        storeTrx.setOrderNo(request.getOrderNo());
        storeTrx.setTasteId(orderDtl.getTasteid());
        storeTrx.setName(orderDtl.getName());
        storeTrx.setGoodId(orderDtl.getGoodid());
        storeTrx.setOrderGoodsNo(orderDtl.getOrderGoodsNo());
        storeTrx.setBrewingCode(orderDtl.getBrewingCode());
        storeTrx.setOrderPrice(new BigDecimal(orderDtl.getOrderPrice()));
        crudService.saveStoreTransactionDetail(storeTrx);

    }

    public Date formatUTCDateToLocalZone(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date originalDate = sdf.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(originalDate);
        long gmtTime = cal.getTime().getTime();

        long timezoneAlteredTime = gmtTime + TimeZone.getTimeZone("Asia/Jakarta").getRawOffset();
        Calendar calNewZone = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
        calNewZone.setTimeInMillis(timezoneAlteredTime);
        return calNewZone.getTime();
    }

    public Date formatUTCDateToLocalZoneForHeiya(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date originalDate = sdf.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(originalDate);
        long gmtTime = cal.getTime().getTime();

        long timezoneAlteredTime = gmtTime + TimeZone.getTimeZone("Asia/Jakarta").getRawOffset();
        Calendar calNewZone = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
        calNewZone.setTimeInMillis(timezoneAlteredTime);
        return calNewZone.getTime();
    }

    public Date formatUTCDateToLocalZoneForDANA(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date originalDate = sdf.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(originalDate);
        long gmtTime = cal.getTime().getTime();

        long timezoneAlteredTime = gmtTime + TimeZone.getTimeZone("Asia/Jakarta").getRawOffset();
        Calendar calNewZone = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
        calNewZone.setTimeInMillis(timezoneAlteredTime);
        return calNewZone.getTime();
    }

    public HttpHeaders getXenditRequestHeaders() throws Exception {
        //Get Encoded Server Key
        XenditConnectUtil xenditUtil = applicationContext.getBean(XenditConnectUtil.class);
        String accessToken = xenditUtil.getEncodedServerKey();

        //Set encoded key as Authorization header
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set("Authorization", accessToken);
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        requestHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return requestHeader;
    }

    public HttpHeaders getGopayRequestHeaders() throws Exception {
        //Get Encoded Server Key
        GopayConnectUtil gopayUtil = applicationContext.getBean(GopayConnectUtil.class);
        String accessToken = gopayUtil.getEncodedServerKey();

        //Set encoded key as Authorization header
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.set("Authorization", accessToken);
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        requestHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return requestHeader;
    }

    public String convertDateFormat(String givenDateStr) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date givenDate = sdf1.parse(givenDateStr);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf2.format(givenDate);
        return formattedDate;
    }

    public String convertDateFormatFree(String givenDateStr) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date givenDate = sdf1.parse(givenDateStr);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf2.format(givenDate);
        return formattedDate;
    }

    public String convertDateFormatDANA(String givenDateStr) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date givenDate = sdf1.parse(givenDateStr);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf2.format(givenDate);
        return formattedDate;
    }

    public Date getDateBasedOnLocalZone() throws ParseException {
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
    public CustomerChangeDTOResponse performCustomerChange(CustomerChangeDTORequest request) throws Exception {
        CustomerChangeDTOResponse response = new CustomerChangeDTOResponse();
        LOGGER.info("======== START PaymentServiceImpl.performCustomerChange() Change mobile no " + request.getPhoneNo() + " to " + request.getFriendNo());

        String phoneNo1 = request.getPhoneNo();
        if (phoneNo1.startsWith("+62")) {
            phoneNo1 = phoneNo1.replace("+62", "0");

        }

        String phoneNo2 = request.getFriendNo();
        if (phoneNo2.startsWith("+62")) {
            phoneNo2 = phoneNo2.replace("+62", "0");
        }

        Transaction transaction = paymentRepo.findPaymentByOrderNo(request.getOrderId());
        Customer customer = customerRepo.findByMobileNo(phoneNo1);
        Customer customerChange = customerRepo.findByMobileNo(phoneNo2);

//                System.out.println(transaction.getCustomerId());
        if (transaction != null) {
            if (transaction.getCustomerChange() == null && transaction.getPaymentStatus().equals(GlobalConstants.TRX_STATUS_SETTLEMENT)) {
                if (!Objects.equals(transaction.getCustomerId(), customerChange.getId())) {
                    transaction.setCustomerChange("change");
                    transaction.setCustomerId(customerChange.getId());
                    paymentRepo.save(transaction);

                    response.setCustomerId(customer.getId());
                    response.setFirstName(customer.getFirstName());
                    response.setLastName(customer.getLastName());
                    response.setMobileNo(customer.getMobileNo());
                    response.setEmail(customer.getEmail());
                    response.setCustomerIdChange(customerChange.getId());
                    response.setFirstNameChange(customerChange.getFirstName());
                    response.setLastNameChange(customerChange.getLastName());
                    response.setMobileNoChange(customerChange.getMobileNo());
                    response.setEmailChange(customerChange.getEmail());
                    response.setSuccess(true);
                    response.setResultCode("200");
                    response.setResultMsg("Successfully Changed Customer");
                    LOGGER.info("======== END PaymentServiceImpl.performCustomerChange() - Change mobile no " + request.getPhoneNo() + " to " + request.getFriendNo() + " success");
                } else {
                    response.setSuccess(false);
                    response.setResultCode("412");
                    response.setResultMsg("Number invalid");
                    LOGGER.info("======== END PaymentServiceImpl.performCustomerChange() - Change mobile no " + request.getPhoneNo() + " to " + request.getFriendNo() + " not success");
                }

            } else if (transaction.getCustomerChange().equals("change")) {
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("This payment cannot be continued anymore");
                LOGGER.info("======== END PaymentServiceImpl.performCustomerChange() - Change mobile no " + request.getPhoneNo() + " to " + request.getFriendNo() + " not success");
            } else {
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("Error data in database");
                LOGGER.info("======== END PaymentServiceImpl.performCustomerChange() - Change mobile no " + request.getPhoneNo() + " to " + request.getFriendNo() + " not success");

            }

        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("Aplication Error");
            LOGGER.info("======== END PaymentServiceImpl.performCustomerChange() - Aplication Error");
        }
        return response;
    }

    @Override
    public BaseResponse getOrderIdChange(String orderId) throws Exception {
        CustomerInquiryChangeDTOResponse response = new CustomerInquiryChangeDTOResponse();
        LOGGER.info("======== START PaymentServiceImpl.getOrderIdChange() With order no " + orderId);

        Transaction transaction = paymentRepo.findPaymentByOrderNo(orderId);

        if (transaction != null) {
            if (transaction.getOrderNo().equals(orderId) && transaction.getPaymentStatus().equals(GlobalConstants.TRX_STATUS_SETTLEMENT)) {
                if (transaction.getTransactionTouchpoint().equals(GlobalConstants.MOBILE)) {
                    if (transaction.getCustomerChange() == null) {
                        response.setOrderId(transaction.getOrderNo());
                        response.setCustomerId(transaction.getCustomerId());
                        response.setSuccess(true);
                        response.setResultCode("200");
                        response.setResultMsg("Order id can be changed");
                        LOGGER.info("======== END PaymentServiceImpl.getOrderIdChange() - Order id " + transaction.getOrderNo() + " can be changed");
                    } else {
                        response.setOrderId(transaction.getOrderNo());
                        response.setCustomerId(transaction.getCustomerId());
                        response.setSuccess(false);
                        response.setResultCode("404");
                        response.setResultMsg("Order id can't be changed");
                        LOGGER.info("======== END PaymentServiceImpl.getOrderIdChange() - Order id " + transaction.getOrderNo() + " can't be changed");
                    }
                } else {
                    response.setOrderId(transaction.getOrderNo());
                    response.setCustomerId(transaction.getCustomerId());
                    response.setSuccess(false);
                    response.setResultCode("404");
                    response.setResultMsg("Order id not a mobile transaction");
                    LOGGER.info("======== END PaymentServiceImpl.getOrderIdChange() - Order id " + transaction.getOrderNo() + " can't be changed bechause not a mobile transaction");
                }
            } else {
                response.setOrderId(null);
                response.setCustomerId(0);
                response.setSuccess(false);
                response.setResultCode("404");
                response.setResultMsg("Payment with order no. " + transaction.getOrderNo() + " not found in our database");
                LOGGER.info("======== END PaymentServiceImpl.getOrderIdChange() - Order id " + transaction.getOrderNo() + " can't be changed bechause not found in our database");
            }
        } else {
            response.setOrderId(null);
            response.setCustomerId(0);
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("Aplication Error");
            LOGGER.info("======== END PaymentServiceImpl.getOrderIdChange() - Aplication Error");
        }
        return response;
    }

    @Override
    public ListCheckNotificationDTOResponse checkTransactionStatusNotification() throws Exception {
        ListCheckNotificationDTOResponse response = new ListCheckNotificationDTOResponse();
        try {
            String[] arrayMediaExpireNotifs = crudService.getGlobalConfigParamByKey(GlobalConstants.MEDIA_NOTIFICATION).split(",");
            String[] arrayPaymentNotifs = crudService.getGlobalConfigParamByKey(GlobalConstants.PAYMENT_NOTIFICATION).split(",");
            String[] arrayExpireNotifs = crudService.getGlobalConfigParamByKey(GlobalConstants.EXPIRE_NOTIFICATION).split(",");

            // Fungsi waktu 1 hari ke belakang
            Calendar calendarExpire = Calendar.getInstance();
            calendarExpire.setTime(this.getDateBasedOnLocalZone());
            calendarExpire.add(Calendar.HOUR, -100);
            Date oneDayBack = calendarExpire.getTime();

            PaymentNotification paymentNotification = new PaymentNotification();

            Customer customer = new Customer();

            List<CheckNotificationDTOResponse> checkNotificationDTOResponses = new ArrayList<>();

            CheckNotificationDTOResponse dTOPaymentResponse = new CheckNotificationDTOResponse();
            CheckNotificationDTOResponse dTOExpireResponse = new CheckNotificationDTOResponse();

            Date back;

            List<Transaction> listTransaction = paymentRepo.findPaymentByTime(oneDayBack);

            if (listTransaction != null) {
                for (Transaction transaction : listTransaction) {
                    paymentNotification = notificationRepository.findPaymentNotificationByOrderNo(transaction.getOrderNo());
                    customer = customerRepo.getOne(transaction.getCustomerId());
                    List<Token> listToken = tokenRepository.findByIdCustomer(transaction.getCustomerId());
                    List<TokenDTOResponse> tokens = new ArrayList<>();
                    for (Token token : listToken) {
                        if (token.getIsLoggedin() == true) {
                            if (paymentNotification == null) {
                                paymentNotification = new PaymentNotification();
                                paymentNotification.setChannelTransactionTime(transaction.getChannelTransactionTime());
                                paymentNotification.setCustomerId(transaction.getCustomerId());
                                paymentNotification.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
                                paymentNotification.setGoodsCode(transaction.getGoodsCode());
                                paymentNotification.setGoodsName(transaction.getGoodsName());
                                paymentNotification.setOrderNo(transaction.getOrderNo());
                                paymentNotification.setPaymentStatus(transaction.getPaymentStatus());
                                notificationRepository.save(paymentNotification);
                            }
                            for (String mediaExpireNotif : arrayMediaExpireNotifs) {
                                if (mediaExpireNotif.equals(GlobalConstants.MEDIA_NOTIFICATION_FIREBASE)
                                        && paymentNotification.getFirebaseExpireNotification() == null
                                        && paymentNotification.getFirebasePaymentNotification() == null) {
                                    paymentNotification.setFirebasePaymentNotification(false);
                                    paymentNotification.setFirebaseExpireNotification(false);
                                } else if (mediaExpireNotif.equals(GlobalConstants.MEDIA_NOTIFICATION_OTP)
                                        && paymentNotification.getOtpExpireNotification() == null
                                        && paymentNotification.getOtpPaymentNotification() == null) {
                                    paymentNotification.setOtpPaymentNotification(false);
                                    paymentNotification.setOtpExpireNotification(false);
                                } else {

                                }
                                notificationRepository.save(paymentNotification);
                            }

                            for (Integer i = 0; i < arrayPaymentNotifs.length; i++) {
                                if (arrayPaymentNotifs != null || arrayPaymentNotifs.length != 0) {

                                    notificationRepository.save(paymentNotification);
                                    calendarExpire.setTime(this.getDateBasedOnLocalZone());
                                    calendarExpire.add(Calendar.MINUTE, -(60 - Integer.parseInt(arrayPaymentNotifs[i])));
                                    back = calendarExpire.getTime();

                                    if (i.equals(paymentNotification.getFirebaseCheckPaymentNotification())) {
                                        if (paymentNotification.getFirebasePaymentNotification() != null && paymentNotification.getFirebaseCheckPaymentNotification() < arrayPaymentNotifs.length) {
                                            if (paymentNotification.getFirebaseCheckPaymentNotification() < arrayExpireNotifs.length && transaction.getChannelTransactionTime().before(back)) {
                                                if (transaction.getPaymentStatus().equals(GlobalConstants.TRX_STATUS_PENDING)) {
                                                    dTOPaymentResponse.setOrderNo(transaction.getOrderNo());
                                                    dTOPaymentResponse.setNotifMsg(GlobalConstants.NOTIFICATION_FIREBASE_CHECK_PAYMENT);

//                                                    List<TokenDTOResponse> tokens = new ArrayList<>();
//                                                    for (Token token : listToken) {
                                                    TokenDTOResponse tdtor = new TokenDTOResponse();
                                                    tdtor.setIdToken(token.getIdToken());
                                                    tokens.add(tdtor);

//                                                    }
                                                    dTOPaymentResponse.setToken(tokens);
                                                    dTOPaymentResponse.setCustomerName(paymentNotification.getCustomerName());
                                                    paymentNotification.setChannelTransactionTime(transaction.getChannelTransactionTime());
                                                    paymentNotification.setFirebaseCheckPaymentNotification(paymentNotification.getFirebaseCheckPaymentNotification() + 1);
                                                    paymentNotification.setPaymentStatus(transaction.getPaymentStatus());
                                                    notificationRepository.save(paymentNotification);

                                                    checkNotificationDTOResponses.add(dTOPaymentResponse);
                                                }
                                            }
                                        }
                                        if (paymentNotification.getOtpPaymentNotification() != null && paymentNotification.getOtpCheckPaymentNotification() < arrayPaymentNotifs.length) {
                                            // Untuk Otp Check Payment
                                        }
                                    } else if (paymentNotification.getFirebaseCheckPaymentNotification() == arrayPaymentNotifs.length && transaction.getChannelTransactionTime().before(back)) {
                                        if (paymentNotification.getFirebasePaymentNotification() == false && transaction.getPaymentStatus().equals(GlobalConstants.TRX_STATUS_SETTLEMENT)) {
                                            dTOPaymentResponse.setOrderNo(transaction.getOrderNo());
                                            dTOPaymentResponse.setNotifMsg(GlobalConstants.NOTIFICATION_FIREBASE_SECCESS_PAYMENT);

//                                            List<TokenDTOResponse> tokens = new ArrayList<>();
//                                            for (Token token : listToken) {
                                            TokenDTOResponse tdtor = new TokenDTOResponse();
                                            tdtor.setIdToken(token.getIdToken());
                                            tokens.add(tdtor);

//                                            }
                                            dTOPaymentResponse.setToken(tokens);
                                            dTOPaymentResponse.setCustomerName(paymentNotification.getCustomerName());
                                            paymentNotification.setChannelTransactionTime(transaction.getChannelTransactionTime());
                                            paymentNotification.setFirebasePaymentNotification(true);
                                            paymentNotification.setPaymentStatus(transaction.getPaymentStatus());
                                            notificationRepository.save(paymentNotification);

                                            checkNotificationDTOResponses.add(dTOPaymentResponse);
                                        } else if (paymentNotification.getFirebasePaymentNotification() == false && transaction.getPaymentStatus().equals(GlobalConstants.TRX_STATUS_UNPAID)) {
                                            dTOPaymentResponse.setOrderNo(transaction.getOrderNo());
                                            dTOPaymentResponse.setNotifMsg(GlobalConstants.NOTIFICATION_FIREBASE_EXPIRE_PAYMENT);

//                                            List<TokenDTOResponse> tokens = new ArrayList<>();
//                                            for (Token token : listToken) {
                                            TokenDTOResponse tdtor = new TokenDTOResponse();
                                            tdtor.setIdToken(token.getIdToken());
                                            tokens.add(tdtor);

//                                            }
                                            dTOPaymentResponse.setToken(tokens);
                                            dTOPaymentResponse.setCustomerName(paymentNotification.getCustomerName());
                                            paymentNotification.setChannelTransactionTime(transaction.getChannelTransactionTime());
                                            paymentNotification.setFirebasePaymentNotification(true);
                                            paymentNotification.setPaymentStatus(transaction.getPaymentStatus());
                                            notificationRepository.save(paymentNotification);

                                            checkNotificationDTOResponses.add(dTOPaymentResponse);
                                        }
                                        if (paymentNotification.getOtpPaymentNotification() != null) {
                                            // Untuk Otp Payment
                                        }
                                    }
                                }
                            }

                            for (Integer i = 0; i < arrayExpireNotifs.length; i++) {
                                if (arrayExpireNotifs != null || arrayExpireNotifs.length != 0) {

                                    notificationRepository.save(paymentNotification);
                                    calendarExpire.setTime(this.getDateBasedOnLocalZone());
                                    calendarExpire.add(Calendar.MINUTE, -(1440 - Integer.parseInt(arrayExpireNotifs[i])));
                                    back = calendarExpire.getTime();

                                    if (i.equals(paymentNotification.getFirebaseCheckExpireNotification())) {
                                        if (paymentNotification.getFirebaseExpireNotification() != null && paymentNotification.getFirebaseCheckExpireNotification() < arrayExpireNotifs.length) {
                                            if (paymentNotification.getFirebaseCheckExpireNotification() < arrayExpireNotifs.length && transaction.getChannelTransactionTime().before(back)) {
                                                if (transaction.getPaymentStatus().equals(GlobalConstants.TRX_STATUS_SETTLEMENT)) {
                                                    dTOExpireResponse.setOrderNo(transaction.getOrderNo());
                                                    dTOExpireResponse.setNotifMsg(GlobalConstants.NOTIFICATION_FIREBASE_CHECK_EXPIRE);

//                                                    List<TokenDTOResponse> tokens = new ArrayList<>();
//                                                    for (Token token : listToken) {
                                                    TokenDTOResponse tdtor = new TokenDTOResponse();
                                                    tdtor.setIdToken(token.getIdToken());
                                                    tokens.add(tdtor);

//                                                    }
                                                    dTOExpireResponse.setToken(tokens);
                                                    dTOExpireResponse.setCustomerName(paymentNotification.getCustomerName());
                                                    paymentNotification.setChannelTransactionTime(transaction.getChannelTransactionTime());
                                                    paymentNotification.setFirebaseCheckExpireNotification(paymentNotification.getFirebaseCheckExpireNotification() + 1);
                                                    paymentNotification.setPaymentStatus(transaction.getPaymentStatus());
                                                    notificationRepository.save(paymentNotification);

                                                    checkNotificationDTOResponses.add(dTOExpireResponse);
                                                }
                                            }
                                        }
                                        if (paymentNotification.getOtpExpireNotification() != null && paymentNotification.getFirebaseCheckPaymentNotification() < arrayExpireNotifs.length) {
                                            // Untuk Otp Expire
                                        }
                                    } else if (paymentNotification.getFirebaseCheckExpireNotification() == arrayExpireNotifs.length && transaction.getChannelTransactionTime().before(back)) {
                                        if (paymentNotification.getFirebaseExpireNotification() == false && transaction.getPaymentStatus().equals(GlobalConstants.TRX_STATUS_COMPLETED)) {
                                            dTOExpireResponse.setOrderNo(transaction.getOrderNo());
                                            dTOExpireResponse.setNotifMsg(GlobalConstants.NOTIFICATION_FIREBASE_SECCESS_PICKUP);

//                                            List<TokenDTOResponse> tokens = new ArrayList<>();
//                                            for (Token token : listToken) {
                                            TokenDTOResponse tdtor = new TokenDTOResponse();
                                            tdtor.setIdToken(token.getIdToken());
                                            tokens.add(tdtor);

//                                            }
                                            dTOExpireResponse.setToken(tokens);
                                            dTOExpireResponse.setCustomerName(paymentNotification.getCustomerName());
                                            paymentNotification.setChannelTransactionTime(transaction.getChannelTransactionTime());
                                            paymentNotification.setFirebaseExpireNotification(true);
                                            paymentNotification.setPaymentStatus(transaction.getPaymentStatus());
                                            notificationRepository.save(paymentNotification);

                                            checkNotificationDTOResponses.add(dTOExpireResponse);
                                        } else if (paymentNotification.getFirebaseExpireNotification() == false && transaction.getPaymentStatus().equals(GlobalConstants.TRX_STATUS_PICKUPEXPIRED)) {
                                            dTOExpireResponse.setOrderNo(transaction.getOrderNo());
                                            dTOExpireResponse.setNotifMsg(GlobalConstants.NOTIFICATION_FIREBASE_EXPIRE_PICKUP);

//                                            List<TokenDTOResponse> tokens = new ArrayList<>();
//                                            for (Token token : listToken) {
                                            TokenDTOResponse tdtor = new TokenDTOResponse();
                                            tdtor.setIdToken(token.getIdToken());
                                            tokens.add(tdtor);

//                                            }
                                            dTOExpireResponse.setToken(tokens);
                                            dTOExpireResponse.setCustomerName(paymentNotification.getCustomerName());
                                            paymentNotification.setChannelTransactionTime(transaction.getChannelTransactionTime());
                                            paymentNotification.setFirebaseExpireNotification(true);
                                            paymentNotification.setPaymentStatus(transaction.getPaymentStatus());
                                            notificationRepository.save(paymentNotification);

                                            checkNotificationDTOResponses.add(dTOExpireResponse);
                                        }
                                        if (paymentNotification.getOtpExpireNotification() != null) {
                                            // Untuk Otp Expire
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            response.setSuccess(true);
            response.setResultCode("200");
            response.setCheckNotificationDTOResponses(checkNotificationDTOResponses);
            response.setResultMsg("Send Notification = " + response.getCheckNotificationDTOResponses().size());
        } catch (NumberFormatException | ParseException e) {
            LOGGER.error(e.getMessage());
        }
        return response;
    }

    @Override
    public void checkAndUpdateTransactionStatusForEmail() throws Exception {
        //Get data from minus 1 hour before
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();
        List<Transaction> pendingPayments = crudService.getPaymentListByStatusAndTimeAndSendEmail(GlobalConstants.TRX_STATUS_UNDELIVERED, oneHourBack, null);
        for (Transaction pendingPayment : pendingPayments) {
            pendingPayment.setSendEmail(Boolean.TRUE);
            paymentRepo.save(pendingPayment);
            reportController.doReport(pendingPayment.getOrderNo());
        }
    }

}
