package com.heiya.mobileapi.payment.service;

import com.heiya.mobileapi.payment.dto.request.CustomerChangeDTORequest;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.payment.dto.request.ChargePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.CreatePaymentDTORequest;
import com.heiya.mobileapi.payment.dto.request.GopayCallbackDTORequest;
import com.heiya.mobileapi.payment.dto.request.HeiyaGeneralCallbackDTORequest;
import com.heiya.mobileapi.payment.dto.request.PickupDTORequest;
import com.heiya.mobileapi.payment.dto.request.XenditGeneralCallbackDTORequest;
import com.heiya.mobileapi.payment.dto.response.CheckNotificationDTOResponse;
import com.heiya.mobileapi.payment.dto.response.GopayCreatePaymentDTOResponse;
import com.heiya.mobileapi.payment.dto.response.ListCheckNotificationDTOResponse;
import com.heiya.mobileapi.payment.dto.response.MidtransResponse;
import com.heiya.mobileapi.payment.dto.response.OrderListDTOResponse;
import com.heiya.mobileapi.payment.dto.response.PaymentProviderListDTOResponse;
import com.heiya.mobileapi.payment.dto.response.ShopeepayCreatePaymentDTOResponse;
import com.heiya.mobileapi.payment.dto.response.XenditCreatePaymentDTOResponse;
import com.heiya.mobileapi.payment.dto.response.TrxStatusDTOResponse;
import com.heiya.mobileapi.payment.dto.response.XenditResponse;

/**
 * @author Dian Krisnanjaya
 *
 */
public interface PaymentService {

    public PaymentProviderListDTOResponse queryAllPaymentProviders() throws Exception;

    public XenditCreatePaymentDTOResponse createXenditOvoPayment(CreatePaymentDTORequest request) throws Exception;

    public XenditCreatePaymentDTOResponse createXenditDanaPayment(CreatePaymentDTORequest request) throws Exception;

    public XenditCreatePaymentDTOResponse createXenditLinkAjaPayment(CreatePaymentDTORequest request) throws Exception;

    public TrxStatusDTOResponse getXenditTrxStatus(String externalId, String ewalletType) throws Exception;
    
    public TrxStatusDTOResponse getNewXenditTrxStatus(String externalId) throws Exception;

    public TrxStatusDTOResponse getGopayTrxStatus(String externalId, String trigger) throws Exception;

    public BaseResponse callbackStatusByXendit(XenditGeneralCallbackDTORequest callbackRequest) throws Exception;
    
    public BaseResponse callbackStatusByHeiya(HeiyaGeneralCallbackDTORequest callbackRequest) throws Exception;

    public BaseResponse callbackStatusByXenditForDana(XenditGeneralCallbackDTORequest callbackRequest) throws Exception;

    public BaseResponse callbackStatusByXenditForLinkAja(XenditGeneralCallbackDTORequest callbackRequest) throws Exception;

    public OrderListDTOResponse getListOfOrders(long customerId) throws Exception;

    public OrderListDTOResponse getListOfOrdersByStatus(long customerId, String status) throws Exception;

    public GopayCreatePaymentDTOResponse createGopayEWalletPayment(CreatePaymentDTORequest request) throws Exception;

    public ShopeepayCreatePaymentDTOResponse createShopeepayEWalletPayment(CreatePaymentDTORequest request) throws Exception;
    
    public MidtransResponse chargeMidtransPayment(ChargePaymentDTORequest request) throws Exception;
    
    public XenditResponse chargeXenditPayment(ChargePaymentDTORequest request) throws Exception;

    public BaseResponse callbackStatusByGopay(GopayCallbackDTORequest callbackRequest) throws Exception;

    public BaseResponse startBrewing(PickupDTORequest request) throws Exception;

    public BaseResponse performCustomerChange(CustomerChangeDTORequest request) throws Exception;

    public BaseResponse getOrderIdChange(String orderId) throws Exception;

    //Jobs
    public void expirePickupOrder() throws Exception; //should be triggered by Scheduler

    public void checkAndUpdateTransactionStatus() throws Exception; //should be triggered by Scheduler

    public ListCheckNotificationDTOResponse checkTransactionStatusNotification() throws Exception; //should be triggered by Scheduler
}
