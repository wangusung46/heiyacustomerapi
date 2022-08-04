package com.heiya.mobileapi.payment.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class XenditMetadata {

    /* Xendit request body */
    @JsonProperty("branch_area")
    private String branchArea;

    @JsonProperty("branch_city")
    private String branchCity;
    
    @JsonProperty("branch_code")
    private String branchCode;

}
