/**
 *
 */
package com.heiya.mobileapi.payment.model;

import java.math.BigDecimal;
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
@Table(name = "qr_transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "machine_code")
    private String machineCode;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "goods_code")
    private String goodsCode;

    @Column(name = "goods_protocol")
    private String goodsProtocol;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "taste_id")
    private int tasteId;

    @Column(name = "transaction_touchpoint")
    private String transactionTouchpoint;

    @Column(name = "channel_type")
    private String channelType;

    @Column(name = "ewallet_type")
    private String ewalletType;

    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "customer_change")
    private String customerChange;

    @Column(name = "channel_transaction_id")
    private String channelTransactionId;

    @Column(name = "channel_order_id")
    private String channelOrderId;

    @Column(name = "channel_currency")
    private String channelCurrency;

    @Column(name = "channel_transaction_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date channelTransactionTime;

//    @Column(name = "notification")
//    private Integer notification = 0;

    @Column(name = "channel_fraud_status")
    private String channelFraudStatus;

    @Column(name = "channel_qr_code")
    private String channelQrCode;

    @Column(name = "channel_deeplink")
    private String channelDeeplink;

    @Column(name = "qr_string")
    private String qrString;

    @Column(name = "payment_status")
    private String paymentStatus;
    
//    @Column(name = "callback")
//    private Boolean callback = false;

    @Column(name = "settlement_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementTime;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "sys_update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysUpdateTime;

}
