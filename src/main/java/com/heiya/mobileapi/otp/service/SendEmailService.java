package com.heiya.mobileapi.otp.service;

public interface SendEmailService {

    public void sendSimpleMessage(String from, String text) throws Exception;

}
