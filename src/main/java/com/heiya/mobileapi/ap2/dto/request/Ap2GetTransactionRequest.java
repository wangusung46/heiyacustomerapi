package com.heiya.mobileapi.ap2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Khanza
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ap2GetTransactionRequest {

    @JsonProperty("store_id")
    private String storeId;

    @JsonProperty("date")
    private String date;
}
