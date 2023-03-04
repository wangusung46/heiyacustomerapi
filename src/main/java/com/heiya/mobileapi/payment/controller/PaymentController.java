package com.heiya.mobileapi.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.firebase.controller.PushNotificationController;
import com.heiya.mobileapi.firebase.dto.request.PushOrderNotificationDTORequest;
import com.heiya.mobileapi.payment.dto.request.ChargePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.CreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.CustomerChangeDTORequest;
import com.heiya.mobileapi.payment.dto.request.GopayCallbackDTORequest;
import com.heiya.mobileapi.payment.dto.request.PickupDTORequest;
import com.heiya.mobileapi.payment.dto.request.XenditGeneralCallbackDTORequest;
import com.heiya.mobileapi.payment.dto.response.CheckNotificationDTOResponse;
import com.heiya.mobileapi.payment.dto.response.ListCheckNotificationDTOResponse;
import com.heiya.mobileapi.payment.dto.response.MidtransResponse;
import com.heiya.mobileapi.payment.dto.response.OrderListDTOResponse;
import com.heiya.mobileapi.payment.dto.response.PaymentProviderListDTOResponse;
import com.heiya.mobileapi.payment.dto.response.TokenDTOResponse;
import com.heiya.mobileapi.payment.dto.response.TrxStatusDTOResponse;
import com.heiya.mobileapi.payment.dto.response.XenditCreatePaymentDTOResponse;
import com.heiya.mobileapi.payment.dto.response.XenditResponse;
import com.heiya.mobileapi.payment.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dian Krisnanjaya This controller will handle all of payment inquiry
 * and transaction
 *
 */
@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PushNotificationController controller;

    @ApiOperation("Get All Payment Gateway Providers")
    @GetMapping(value = "/query/providers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doGetAllProviders() throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doGetAllProviders");
        PaymentProviderListDTOResponse response;
        try {
            response = paymentService.queryAllPaymentProviders();
            LOGGER.info("======== COMPLETED PaymentController.doGetAllProviders");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @ApiOperation("[EWALLET] Charge an item(s) by using E-Wallet services")
//    @PostMapping(value = "/ewallet/create", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> doCreateEwalletPayment(@RequestBody CreatePaymentDTORequest request) throws JsonProcessingException {
//        LOGGER.info("\n\n======== START PaymentController.doCreateEwalletPayment");
//        XenditCreatePaymentDTOResponse xenditResponse = null;
//        XenditResponse xenditNewResponse = null;
//        GopayCreatePaymentDTOResponse gopayResponse = null;
//        ShopeepayCreatePaymentDTOResponse shopeepayResponse = new ShopeepayCreatePaymentDTOResponse();
//
//        try {
//            switch (request.getEwalletType()) {
//                case GlobalConstants.EWALLET_TYPE_GOPAY:
//                    gopayResponse = paymentService.createGopayEWalletPayment(request);
//                    LOGGER.info("======== COMPLETED PaymentController.doCreateEwalletPayment");
//                    return ResponseEntity.ok(gopayResponse);
//                case GlobalConstants.EWALLET_TYPE_OVO:
//                    xenditResponse = paymentService.createXenditOvoPayment(request);
//                    LOGGER.info("======== COMPLETED PaymentController.doCreateEwalletPayment");
//                    return ResponseEntity.ok(xenditResponse);
//                case GlobalConstants.EWALLET_TYPE_DANA:
//                    xenditResponse = paymentService.createXenditDanaPayment(request);
//                    LOGGER.info("======== COMPLETED PaymentController.doCreateEwalletPayment");
//                    return ResponseEntity.ok(xenditResponse);
//                case GlobalConstants.EWALLET_TYPE_LINKAJA:
//                    xenditResponse = paymentService.createXenditLinkAjaPayment(request);
//                    LOGGER.info("======== COMPLETED PaymentController.doCreateEwalletPayment");
//                    return ResponseEntity.ok(xenditResponse);
//                case GlobalConstants.EWALLET_TYPE_SHOPEEPAY:
//                    shopeepayResponse = paymentService.createShopeepayEWalletPayment(request);
//                    LOGGER.info("======== COMPLETED PaymentController.doCreateEwalletPayment");
//                    return ResponseEntity.ok(shopeepayResponse);
//                default:
//                    break;
//            }
//            return ResponseEntity.ok(null);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @ApiOperation("[EWALLET 2021] Charge an item(s) by using E-Wallet services")
    @PostMapping(value = "/ewallet/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doChargesEwalletPayment(@RequestBody ChargePaymentDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doChargesEwalletPayment");
        MidtransResponse midtransResponse;
        XenditResponse xenditResponse;
        try {
            midtransResponse = paymentService.chargeMidtransPayment(request);
            xenditResponse = paymentService.chargeXenditPayment(request);
            switch (request.getChannelType()) {
                case GlobalConstants.CHANNEL_TYPE_MIDTRANS:
                    switch (request.getEwalletType()) {
                        case GlobalConstants.EWALLET_TYPE_GOPAY:
                            LOGGER.info("======== COMPLETED PaymentController.doChargesEwalletPayment MIDTRANS GOPAY");
                            return ResponseEntity.ok(midtransResponse);
                        case GlobalConstants.EWALLET_TYPE_SHOPEEPAY:
                            LOGGER.info("======== COMPLETED PaymentController.doChargesEwalletPayment MIDTRANS SHOPEEPAY");
                            return ResponseEntity.ok(midtransResponse);
                        default:
                            return ResponseEntity.badRequest().body(null);
                    }
                case GlobalConstants.CHANNEL_TYPE_XENDIT:
                    switch (request.getEwalletType()) {
                        case GlobalConstants.EWALLET_TYPE_OVO_NEW:
                            LOGGER.info("======== COMPLETED PaymentController.doChargesEwalletPayment XENDIT OVO");
                            return ResponseEntity.ok(xenditResponse);
                        case GlobalConstants.EWALLET_TYPE_DANA_NEW:
                            LOGGER.info("======== COMPLETED PaymentController.doChargesEwalletPayment XENDIT DANA");
                            return ResponseEntity.ok(xenditResponse);
                        case GlobalConstants.EWALLET_TYPE_LINKAJA_NEW:
                            LOGGER.info("======== COMPLETED PaymentController.doChargesEwalletPayment XENDIT LINKAJA");
                            return ResponseEntity.ok(xenditResponse);
                        case GlobalConstants.EWALLET_TYPE_SHOPEEPAY_NEW:
                            // BELUM BISA
                            LOGGER.info("======== COMPLETED PaymentController.doChargesEwalletPayment XENDIT SHOPEEPAY");
                            return ResponseEntity.ok(xenditResponse);
                        default:
                            return ResponseEntity.badRequest().body(null);
                    }
                default:
                    return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("[XENDIT] Create eWallet Payment - Submit payment by using Xendit for DANA, OVO, LinkAja & Shopeepay") //please use doCreateEwalletPayment() instead of this
    @PostMapping(value = "/xendit/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doXenditOvoCreatePayment(@RequestBody CreatePaymentDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doXenditOvoCreatePayment");
        XenditCreatePaymentDTOResponse response;
        try {
            response = paymentService.createXenditOvoPayment(request);
            LOGGER.info("======== COMPLETED PaymentController.doXenditOvoCreatePayment");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("[XENDIT] Get Xendit eWallet transaction status")  //please use transaction status v2 instead of this
    @GetMapping(value = "/xendit/status/{externalId}/{ewalletType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doXenditTransactionStatus(@PathVariable("externalId") String externalId, @PathVariable("ewalletType") String ewalletType) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doTransactionStatus");
        TrxStatusDTOResponse response;
        try {
            response = paymentService.getXenditTrxStatus(externalId, ewalletType);
            LOGGER.info("======== COMPLETED PaymentController.doTransactionStatus");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Get eWallet transaction status v2 (for all eWallet Type)")
    @GetMapping(value = "/ewallet/status/{externalId}/{ewalletType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doEwalletTransactionStatus(@PathVariable("externalId") String externalId, @PathVariable("ewalletType") String ewalletType) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doEwalletTransactionStatus for external ID : " + externalId);
        TrxStatusDTOResponse response;
        try {
            switch (ewalletType) {
                case GlobalConstants.EWALLET_TYPE_GOPAY:
                    response = paymentService.getGopayTrxStatus(externalId, GlobalConstants.PAYMENT_STATUS_TRIGGER_APP);
                    LOGGER.info("======== COMPLETED PaymentController.doEwalletTransactionStatus - GOPAY with response : " + response);
                    return ResponseEntity.ok(response);
                case GlobalConstants.EWALLET_TYPE_OVO:
                    response = paymentService.getXenditTrxStatus(externalId, ewalletType);
                    LOGGER.info("======== COMPLETED PaymentController.doEwalletTransactionStatus - OVO with response : " + response);
                    return ResponseEntity.ok(response);
                case GlobalConstants.EWALLET_TYPE_DANA:
                    response = paymentService.getXenditTrxStatus(externalId, ewalletType);
                    LOGGER.info("======== COMPLETED PaymentController.doEwalletTransactionStatus - DANA with response : " + response);
                    return ResponseEntity.ok(response);
                case GlobalConstants.EWALLET_TYPE_LINKAJA:
                    response = paymentService.getXenditTrxStatus(externalId, ewalletType);
                    LOGGER.info("======== COMPLETED PaymentController.doEwalletTransactionStatus - LINKAJA with response : " + response);
                    return ResponseEntity.ok(response);
                case GlobalConstants.EWALLET_TYPE_SHOPEEPAY:
                    response = paymentService.getXenditTrxStatus(externalId, GlobalConstants.PAYMENT_STATUS_TRIGGER_APP);
                    LOGGER.info("======== COMPLETED PaymentController.doEwalletTransactionStatus - SHOPEEPAY with response : " + response);
                    return ResponseEntity.ok(response);
                default:
                    return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("[XENDIT] Callback API for completed transaction (triggered by Xendit)")
    @PostMapping(value = "/xendit/status/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doProvideXenditCallback(@RequestBody XenditGeneralCallbackDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doProvideXenditCallback for : " + request.getEwalletType());
        BaseResponse response;
        try {
            switch (request.getEwalletType()) {
                case GlobalConstants.EWALLET_TYPE_OVO:
                    response = paymentService.callbackStatusByXendit(request);
                    if (response != null) {
                        if (response.getResultCode().equals("200")) {
                            LOGGER.info("======== COMPLETED PaymentController.doProvideXenditCallback - OVO");
                            return ResponseEntity.ok(response);
                        } else {
                            return ResponseEntity.badRequest().body(response.getResultMsg());
                        }
                    } else {
                        return ResponseEntity.badRequest().body(null);
                    }
                case GlobalConstants.EWALLET_TYPE_SHOPEEPAY:
                    response = paymentService.callbackStatusByXendit(request);
                    if (response != null) {
                        if (response.getResultCode().equals("200")) {
                            LOGGER.info("======== COMPLETED PaymentController.doProvideXenditCallback - SHOPEEPAY");
                            return ResponseEntity.ok(response);
                        } else {
                            return ResponseEntity.badRequest().body(response.getResultMsg());
                        }
                    } else {
                        return ResponseEntity.badRequest().body(null);
                    }
                case GlobalConstants.EWALLET_TYPE_DANA:
                    response = paymentService.callbackStatusByXenditForDana(request);
                    if (response != null) {
                        if (response.getResultCode().equals("200")) {
                            LOGGER.info("======== COMPLETED PaymentController.doProvideXenditCallback - DANA");
                            return ResponseEntity.ok(response);
                        } else {
                            return ResponseEntity.badRequest().body(response.getResultMsg());
                        }
                    } else {
                        return ResponseEntity.badRequest().body(null);
                    }
                case GlobalConstants.EWALLET_TYPE_LINKAJA:
                    response = paymentService.callbackStatusByXenditForLinkAja(request);
                    if (response != null) {
                        if (response.getResultCode().equals("200")) {
                            LOGGER.info("======== COMPLETED PaymentController.doProvideXenditCallback - LINKAJA");
                            return ResponseEntity.ok(response);
                        } else {
                            return ResponseEntity.badRequest().body(response.getResultMsg());
                        }
                    } else {
                        return ResponseEntity.badRequest().body(null);
                    }
                default:
                    return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("[GOPAY] Callback API for completed transaction (triggered by Midtrans)")
    @PostMapping(value = "/gopay/status/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doProvideGopayCallback(@RequestBody GopayCallbackDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doProvideGopayCallback");
        BaseResponse response;
        try {
            response = paymentService.callbackStatusByGopay(request);
            LOGGER.info("======== COMPLETED PaymentController.doProvideGopayCallback");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*@ApiOperation("[XENDIT] Callback API for completed transaction (triggered by Xendit)")
	@PostMapping(value = "/xendit/status/callback", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> doProvideXenditCallback(@RequestBody XenditCallbackDTORequest request) throws JsonProcessingException {
		LOGGER.info("\n\n======== START PaymentController.doProvideXenditCallback");
		BaseResponse response = null;
		
		try {
			response = paymentService.callbackStatusByXendit(request);
			if (response != null && response.getResultCode().equals("200")) {
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.badRequest().body(response.getResultMsg());
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}*/
 /*@ApiOperation("[DANA] Callback API for completed transaction (triggered by Xendit)")
	@PostMapping(value = "/dana/status/callback", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> doProvideDanaCallback(@RequestBody XenditDanaCallbackDTORequest request) throws JsonProcessingException {
		LOGGER.info("\n\n======== START PaymentController.doProvideDanaCallback");
		BaseResponse response = null;
		
		try {
			response = paymentService.callbackStatusByXenditForDana(request);
			LOGGER.info("======== COMPLETED PaymentController.doProvideDanaCallback");
			return ResponseEntity.ok(response);
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
 /*@ApiOperation("[LINKAJA] Callback API for completed transaction (triggered by Xendit)")
	@PostMapping(value = "/linkaja/status/callback", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> doProvideLinkAjaCallback(@RequestBody XenditLinkAjaCallbackDTORequest request) throws JsonProcessingException {
		LOGGER.info("\n\n======== START PaymentController.doProvideLinkAjaCallback");
		BaseResponse response = null;
		
		try {
			response = paymentService.callbackStatusByXenditForLinkAja(request);
			LOGGER.info("======== COMPLETED PaymentController.doProvideLinkAjaCallback");
			return ResponseEntity.ok(response);
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}*/
    @ApiOperation("[SCHEDULER] Run Job to Check & Update Payment Status (triggered by HW Payment API - Since the scheduler instance doesn't work in Heiya Mobile API)")
    @PostMapping(value = "/paymentJob", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doRunPaymentJobByHWPaymentAPI() throws JsonProcessingException {
//		LOGGER.info("\n\n======== START PaymentController.doRunPaymentJobByHWPaymentAPI");
        BaseResponse response = new BaseResponse();

        try {
            paymentService.checkAndUpdateTransactionStatus();
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Payment status job has been triggered by HW Payment API. Check the log for more detail");
//			LOGGER.info("======== COMPLETED PaymentController.doRunPaymentJobByHWPaymentAPI by HW Payment API : " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("[SCHEDULER] Run Job to Check & Update Pickup Order Status (triggered by HW Payment API - Since the scheduler instance doesn't work in Heiya Mobile API)")
    @PostMapping(value = "/pickupOrderJob", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doRunPickupOrderJobByHWPaymentAPI() throws JsonProcessingException {
//		LOGGER.info("\n\n======== START PaymentController.doRunPickupOrderJobByHWPaymentAPI");
        BaseResponse response = new BaseResponse();

        try {
            paymentService.expirePickupOrder();
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Pickup Order status job has been triggered by HW Payment API. Check the log for more detail");
//			LOGGER.info("======== COMPLETED PaymentController.doRunPickupOrderJobByHWPaymentAPI by HW Payment API : " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Get list of orders")
    @GetMapping(value = "/order/list/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doListOrders(@PathVariable("customerId") long customerId) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doListOrders");
        OrderListDTOResponse response;
        try {
            response = paymentService.getListOfOrders(customerId); //TODO: Bikin implementasi (jgn lupa soal expiry time)
            LOGGER.info("======== COMPLETED PaymentController.doListOrders");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Get list of orders by status")
    @GetMapping(value = "/order/list/{customerId}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doListOrdersByStatus(@PathVariable("customerId") long customerId, @PathVariable("status") String status) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doListOrdersByStatus");
        OrderListDTOResponse response;
        try {
            response = paymentService.getListOfOrdersByStatus(customerId, status); //TODO: Bikin implementasi (jgn lupa soal expiry time)
            LOGGER.info("======== COMPLETED PaymentController.doListOrdersByStatus");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Start brewing & pickup the ordered beverages from vending machine")
    @PostMapping(value = "/order/pickup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doPickupAndBrewOrder(@RequestBody PickupDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doPickupAndBrewOrder");
        BaseResponse response;
        try {
            response = paymentService.startBrewing(request);
            LOGGER.info("======== COMPLETED PaymentController.doPickupAndBrewOrder");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Execute Customer Change")
    @PostMapping(value = "/customerchange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doCustomerChange(@RequestBody CustomerChangeDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doCustomerChange");
        BaseResponse response;
        try {
            response = paymentService.performCustomerChange(request);
            LOGGER.info("======== COMPLETED PaymentController.doCustomerChange");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Inquiry Customer Change")
    @GetMapping(value = "/customerchange/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doInquiryOrderIdChange(@PathVariable("orderId") String orderId) throws JsonProcessingException {
        LOGGER.info("\n\n======== START PaymentController.doInquiryOrderIdChange");
        BaseResponse response;
        try {
            response = paymentService.getOrderIdChange(orderId);
            LOGGER.info("======== COMPLETED PaymentController.doInquiryOrderIdChange");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/checkTransaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doCheckTransactionStatusNotification() throws JsonProcessingException {
        ListCheckNotificationDTOResponse response;
        List<CheckNotificationDTOResponse> checkNotificationDTOResponses;
        PushOrderNotificationDTORequest request = new PushOrderNotificationDTORequest();
        try {
            response = paymentService.checkTransactionStatusNotification();
            checkNotificationDTOResponses = response.getCheckNotificationDTOResponses();
            for (CheckNotificationDTOResponse checkNotificationDTOResponse : checkNotificationDTOResponses) {
                for (TokenDTOResponse tokenDTOResponse : checkNotificationDTOResponse.getToken()) {
                    request.setToken(tokenDTOResponse.getIdToken());
                    request.setOrderNo(checkNotificationDTOResponse.getOrderNo());
                    request.setTopic(checkNotificationDTOResponse.getNotifMsg());
                    request.setCustomerName(checkNotificationDTOResponse.getCustomerName());
                    controller.doSendPaymentTokenNotification(request);
                }
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
