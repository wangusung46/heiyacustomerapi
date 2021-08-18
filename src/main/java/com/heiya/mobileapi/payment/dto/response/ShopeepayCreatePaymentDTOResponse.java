package com.heiya.mobileapi.payment.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heiya.mobileapi.dto.response.BaseResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ShopeepayCreatePaymentDTOResponse extends BaseResponse {

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status_message")
    private String statusMessage;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("gross_amount")
    private String grossAmount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("transaction_time")
    private String transactionTime;

    @JsonProperty("transaction_status")
    private String transactionStatus;

    @JsonProperty("fraud_status")
    private String fraudStatus;

    @JsonProperty("actions")
    private List<Action> actions;
}
