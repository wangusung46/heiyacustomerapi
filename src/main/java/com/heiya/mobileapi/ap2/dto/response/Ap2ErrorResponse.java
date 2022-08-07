package com.heiya.mobileapi.ap2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Khanza
 *
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
 ]
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ap2ErrorResponse {

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("message")
    private String message;
}
