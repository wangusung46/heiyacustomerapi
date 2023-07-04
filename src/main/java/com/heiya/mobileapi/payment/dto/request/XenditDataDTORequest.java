package com.heiya.mobileapi.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heiya.mobileapi.payment.dto.response.XenditActions;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Dian Krisnanjaya
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class XenditDataDTORequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("business_id")
    private String businessId;

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("charge_amount")
    private BigDecimal chargeAmount;

    @JsonProperty("capture_amount")
    private BigDecimal captureAmount;

    @JsonProperty("checkout_method")
    private String checkoutMethod;

    @JsonProperty("channel_code")
    private String channelCode;

    @JsonProperty("channel_properties")
    private XenditChannelProperties channelProperties;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("actions")
    private XenditActions xenditActions;

    @JsonProperty("is_redirect_required")
    private Boolean isRedirectRequired; //only for DANA

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("created")
    private String created;

    @JsonProperty("updated")
    private String updated;

    @JsonProperty("voided_at")
    private String voidedAt;

    @JsonProperty("capture_now")
    private String captureNow;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("payment_method_id")
    private String paymentMethodId;

    @JsonProperty("failure_code")
    private String failureCode;

    @JsonProperty("basket")
    private String basket;

    @JsonProperty("metadata")
    private XenditMetadata metadata;
}
