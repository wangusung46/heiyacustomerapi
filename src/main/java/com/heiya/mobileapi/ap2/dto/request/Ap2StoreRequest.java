package com.heiya.mobileapi.ap2.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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
public class Ap2StoreRequest {

    @JsonProperty("store_id")
    private String storeId;

    @JsonProperty("transactions")
    private List<Ap2TransactionRequest> transactionRequest;
}
