/**
 *
 */
package com.heiya.mobileapi.firebase.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dian Krisnanjaya
 *
 */
@Setter
@Getter
@Entity
@Table(name = "payment_notification")
public class PaymentNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "goods_code")
    private String goodsCode;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "channel_transaction_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date channelTransactionTime;

    @Column(name = "payment_status")
    private String paymentStatus;
    
    @Column(name = "firebase_expire_notification")
    private Boolean firebaseExpireNotification;
    
    @Column(name = "otp_expire_notification")
    private Boolean otpExpireNotification;
    
    @Column(name = "firebase_check_expire_notification")
    private Integer firebaseCheckExpireNotification = 0;
    
    @Column(name = "otp_check_expire_notification")
    private Integer otpCheckExpireNotification = 0;
    
    @Column(name = "firebase_payment_notification")
    private Boolean firebasePaymentNotification;
    
    @Column(name = "otp_payment_notification")
    private Boolean otpPaymentNotification;
    
    @Column(name = "firebase_check_payment_notification")
    private Integer firebaseCheckPaymentNotification = 0;
    
    @Column(name = "otp_check_payment_notification")
    private Integer otpCheckPaymentNotification = 0;

}
