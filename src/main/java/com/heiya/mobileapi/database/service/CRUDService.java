/**
 * 
 */
package com.heiya.mobileapi.database.service;

import com.heiya.mobileapi.firebase.model.PaymentNotification;
import java.util.Date;
import java.util.List;

import com.heiya.mobileapi.payment.model.StoreTransactionDetail;
import com.heiya.mobileapi.payment.model.Transaction;

/**
 * @author Dian Krisnanjaya
 *
 */
public interface CRUDService {
	
	/* ------------ Global Configuration ------------- */
	
	public String getGlobalConfigParamByKey(String configKey);
	
	/* ------------ Payment ------------- */
	
	public void addPayment(Transaction payment);
	
	public Transaction getPaymentByOrderNo(String orderNo);
	
	public void updatePayment(Transaction payment);
	
	public List<Transaction> getPaymentByStatusAndTouchpoint(String status, String touchpoint);
	
	public void saveStoreTransactionDetail(StoreTransactionDetail transactionDtl);
	
	public StoreTransactionDetail getStoreTransactionDetailByOrderNo(String orderNo);
	
	public List<Transaction> getPaymentListByStatusAndTime(String paymentStatus, Date channelTransactionTime);
        
        /* ------------ Payment ------------- */
        
        public PaymentNotification getPaymentNotificationByOrderNo(String orderNo);
        
        public void addNotification(PaymentNotification paymentNotification);
}
