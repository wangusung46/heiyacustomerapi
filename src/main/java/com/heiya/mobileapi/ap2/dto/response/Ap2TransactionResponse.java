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
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ap2TransactionResponse {

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("success_insert")
    private Integer successInsert;

    @JsonProperty("failed_insert")
    private Integer failedInsert;

    @JsonProperty("success_data")
    private List<Ap2SuccessDataResponse> successDataResponse;

    @JsonProperty("failed_data")
    private List<Ap2FailedDataResponse> failedData;
    
    @JsonProperty("response_failed")
    private List<String> responseFailed;
}
