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
public class Ap2ListStoreRequest {

    @JsonProperty("store")
    private List<Ap2StoreRequest> storeRequests;
}
