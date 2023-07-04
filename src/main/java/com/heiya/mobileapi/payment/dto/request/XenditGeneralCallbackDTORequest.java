package com.heiya.mobileapi.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heiya.mobileapi.payment.dto.response.XenditActions;
import java.math.BigDecimal;
import java.util.Date;
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
public class XenditGeneralCallbackDTORequest {

    @JsonProperty("data")
    private XenditDataDTORequest xenditDataDTORequest;

    @JsonProperty("event")
    private String event;

    @JsonProperty("created")
    private String created;

    @JsonProperty("business_id")
    private String businessId;

}
