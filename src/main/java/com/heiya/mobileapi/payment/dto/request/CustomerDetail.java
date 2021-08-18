/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heiya.mobileapi.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author User
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomerDetail {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("Last_name")
    private String LastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;
}
