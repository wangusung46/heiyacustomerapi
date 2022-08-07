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
public class Ap2GetTransactionResponse {

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("total_data")
    private Integer totalData;

    @JsonProperty("data")
    private List<Ap2SuccessDataResponse> successDataResponse;
}
