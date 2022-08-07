package com.heiya.mobileapi.ap2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Khanza
 *
 "user": {
    "fullname": "{Tenant Name}",
    "store": [
        {
        "store_id": "{Store Id[1]}",
        "airport_code": "{Airport}",
        "store_name": "{Store Name}",
        "store_reference": null
        },
        {
        "store_id": "{Store Id[2]}",
        "airport_code": "{Airport}",
        "store_name": "{Store Name}",
        "store_reference": null
        }
    ],
    "created_at": "2022-02-07 18:38:53",
    "updated_at": null
 }
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ap2UserResponse {

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("store")
    private List<Ap2StoreResponse> ap2StoreRequests;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}
