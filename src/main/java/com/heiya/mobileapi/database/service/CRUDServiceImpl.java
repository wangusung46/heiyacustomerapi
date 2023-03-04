/**
 *
 */
package com.heiya.mobileapi.database.service;

import com.heiya.mobileapi.database.model.GlobalConfigs;
import com.heiya.mobileapi.database.repository.GlobalConfigsRepository;
import com.heiya.mobileapi.firebase.model.PaymentNotification;
import com.heiya.mobileapi.firebase.repository.PaymentNotificationRepository;
import com.heiya.mobileapi.payment.model.StoreTransactionDetail;
import com.heiya.mobileapi.payment.model.Transaction;
import com.heiya.mobileapi.payment.repository.StoreTransactionDetailRepository;
import com.heiya.mobileapi.payment.repository.TransactionRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Dian Krisnanjaya
 *
 */
@Service
public class CRUDServiceImpl implements CRUDService {

    @Autowired
    private GlobalConfigsRepository globalConfigsRepo;

    @Autowired
    private TransactionRepository payRepo;

    @Autowired
    private StoreTransactionDetailRepository storeTrxDetailRepo;

    @Autowired
    private PaymentNotificationRepository notificationRepository;

    /* ------------ Global Configuration ------------- */
    @Override
    public String getGlobalConfigParamByKey(String configKey) {
        GlobalConfigs config = globalConfigsRepo.findParamByKey(configKey);
        return config.getConfigValue();
    }

    /* ------------ Payment ------------- */
    @Override
    public void addPayment(Transaction payment) {
        payRepo.save(payment);
    }

    @Override
    public Transaction getPaymentByOrderNo(String orderNo) {
        return payRepo.findPaymentByOrderNo(orderNo);
    }

    @Override
    public void updatePayment(Transaction payment) {
        payRepo.save(payment);
    }

    @Override
    public List<Transaction> getPaymentByStatusAndTouchpoint(String status, String touchpoint) {
        return payRepo.findPaymentByStatusAndTouchpoint(status, touchpoint);
    }

    @Override
    public void saveStoreTransactionDetail(StoreTransactionDetail transactionDtl) {
        storeTrxDetailRepo.save(transactionDtl);
    }

    @Override
    public StoreTransactionDetail getStoreTransactionDetailByOrderNo(String orderNo) {
        return storeTrxDetailRepo.findByOrderNo(orderNo);
    }

    @Override
    public List<Transaction> getPaymentListByStatusAndTime(String paymentStatus, Date channelTransactionTime) {
        return payRepo.findPaymentListByStatusAndTime(paymentStatus, channelTransactionTime);
    }

    @Override
    public List<Transaction> getPaymentListByStatusAndTimeAndSendEmail(String paymentStatus, Date channelTransactionTime, Boolean sendEmail) {
        return payRepo.findByPaymentStatusAndChannelTransactionTimeGreaterThanEqualAndSendEmail(paymentStatus, channelTransactionTime, sendEmail);
    }

    @Override
    public PaymentNotification getPaymentNotificationByOrderNo(String orderNo) {
        return notificationRepository.findPaymentNotificationByOrderNo(orderNo);
    }

    @Override
    public void addNotification(PaymentNotification paymentNotification) {
        notificationRepository.save(paymentNotification);
    }

}
