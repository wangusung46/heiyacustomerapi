package com.heiya.mobileapi.payment.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class XenditBasket {

    /* Xendit request body */
    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("price")
    private BigInteger price;
    
    @JsonProperty("quantity")
    private BigInteger quantity;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("sub_category")
    private String subCategory;

}
