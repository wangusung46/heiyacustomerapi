package com.heiya.mobileapi.ap2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heiya.mobileapi.ap2.dto.response.Ap2ErrorResponse;
import com.heiya.mobileapi.ap2.service.AP2Service;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ap2")
public class AP2Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(AP2Controller.class);

    @Autowired
    private AP2Service aP2Service;

    @ApiOperation("Login to AP2")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doAp2Login() throws JsonProcessingException {
        LOGGER.info("\n\n======== START AP2Controller.doAp2Login");
        try {
            LOGGER.info("======== COMPLETED AP2Controller.doAp2Login");
            return ResponseEntity.ok(aP2Service.loginRequest());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok(getGenericBadResponse(e.getMessage()));
        }
    }

    @ApiOperation("Get item from AP2")
    @GetMapping(value = "/get-product/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doAp2GetItem(@PathVariable("date") String date) throws JsonProcessingException {
        LOGGER.info("\n\n======== START AP2Controller.doAp2GetItem");
        try {
            LOGGER.info("======== COMPLETED AP2Controller.doAp2GetItem");
            return ResponseEntity.ok(aP2Service.getTransactionRequest(date));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.ok(getGenericBadResponse(e.getMessage()));
        }
    }

    public Ap2ErrorResponse getGenericBadResponse(String errorMsg) {
        Ap2ErrorResponse response = new Ap2ErrorResponse();
        response.setStatus(false);
        response.setMessage("Error");
        return response;
    }
}
