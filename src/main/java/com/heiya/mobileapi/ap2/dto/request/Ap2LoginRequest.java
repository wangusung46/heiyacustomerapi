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
public class Ap2LoginRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
