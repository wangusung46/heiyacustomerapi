package com.heiya.mobileapi.ap2.service;

import com.heiya.mobileapi.ap2.dto.response.Ap2GetTransactionResponse;
import com.heiya.mobileapi.ap2.dto.response.Ap2LoginResponse;
import org.springframework.web.client.ResourceAccessException;

public interface AP2Service {

    public Ap2LoginResponse loginRequest() throws Exception;

    public Ap2LoginResponse recover(ResourceAccessException e);

    public Ap2LoginResponse recover(RuntimeException e);

    public Ap2GetTransactionResponse getTransactionRequest(String date) throws Exception;
}
