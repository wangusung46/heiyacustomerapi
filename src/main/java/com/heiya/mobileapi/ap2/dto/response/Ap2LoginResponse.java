package com.heiya.mobileapi.ap2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Khanza
 *
 * {
 * "status": true, "message": "Success login", "user": { "fullname": "{Tenant
 * Name}", "store": [ { "store_id": "{Store Id[1]}", "airport_code":
 * "{Airport}", "store_name": "{Store Name}", "store_reference": null }, {
 * "store_id": "{Store Id[2]}", "airport_code": "{Airport}", "store_name":
 * "{Store Name}", "store_reference": null } ], "created_at": "2022-02-07
 * 18:38:53", "updated_at": null }, "token": "{token access}" }
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ap2LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private Ap2UserResponse ap2UserRequest;

}
