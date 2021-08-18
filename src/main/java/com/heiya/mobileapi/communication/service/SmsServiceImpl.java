package com.heiya.mobileapi.communication.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heiya.mobileapi.communication.dto.response.SendSmsDTOResponse;
import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.database.service.CRUDService;

@Service
public class SmsServiceImpl implements SmsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);
	
	@Autowired
	private CRUDService crudService;
	
	@Override
	public SendSmsDTOResponse sendSMS(String phoneNo, String message) throws GeneralSecurityException {
		LOGGER.info("======== START SmsServiceImpl.sendSMS() to : " + phoneNo + " and message : " + message);
		SendSmsDTOResponse smsResponse = new SendSmsDTOResponse();
		
		try {
			/* Define URL & headers */
			String providerBaseURL = crudService.getGlobalConfigParamByKey(GlobalConstants.SMS_GW_MITRACOMM_URL);
			String url = providerBaseURL.concat("/sendsms");
			
			//Generate REFERENCE NO
			String last4digit = phoneNo.substring(phoneNo.length() - 4);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS'Z'");
			String refNo = "heiya".concat(last4digit).concat(formatter.format(new Date()));
			
			//Perform HTTP POST Request
			HttpPost post = new HttpPost(url);
			
			// add request parameter, form parameters
	        List<NameValuePair> urlParameters = new ArrayList<>();
	        urlParameters.add(new BasicNameValuePair("userid", crudService.getGlobalConfigParamByKey(GlobalConstants.SMS_GW_MITRACOMM_USERID)));
	        urlParameters.add(new BasicNameValuePair("password", crudService.getGlobalConfigParamByKey(GlobalConstants.SMS_GW_MITRACOMM_PASSWORD)));
	        urlParameters.add(new BasicNameValuePair("msisdn", phoneNo));
	        urlParameters.add(new BasicNameValuePair("message", message));
	        urlParameters.add(new BasicNameValuePair("refno", refNo));
	        urlParameters.add(new BasicNameValuePair("masking", crudService.getGlobalConfigParamByKey(GlobalConstants.SMS_GW_MITRACOMM_MASKING)));
	        urlParameters.add(new BasicNameValuePair("app_code", crudService.getGlobalConfigParamByKey(GlobalConstants.SMS_GW_MITRACOMM_APPCODE)));
	        urlParameters.add(new BasicNameValuePair("type", crudService.getGlobalConfigParamByKey(GlobalConstants.SMS_GW_MITRACOMM_TYPE)));
	        
	        LOGGER.debug("======== RESPONSE SmsServiceImpl.sendSMS() SmsRequestParameters: " + urlParameters);
	        
	        post.setEntity(new UrlEncodedFormEntity(urlParameters));

	        HttpEntity responseEntity = null;
	        try (CloseableHttpClient httpClient = HttpClients.createDefault(); 
	        		CloseableHttpResponse response = httpClient.execute(post)) {
	        	
	        	try {
		        	responseEntity = response.getEntity();
		        	
		        	if (responseEntity != null && response.getStatusLine().getStatusCode() == 200) {
			        	smsResponse.setSuccess(true);
						smsResponse.setResultCode(String.valueOf(response.getStatusLine().getStatusCode())); 
						smsResponse.setResultMsg("SMS sent successfully");
						BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent(), "ISO-8859-1"));
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) // Read line by line
						  sb.append(line + "\n");
	
						String resString = sb.toString();
						smsResponse.setSmsResponse(resString);
						responseEntity.getContent().close();
						LOGGER.debug("======== RESPONSE SmsServiceImpl.sendSMS() success - SmsResponse: " + responseEntity);
			        } 
			        else {
			        	smsResponse.setSuccess(false);
						smsResponse.setResultCode(String.valueOf(response.getStatusLine().getStatusCode()));
						smsResponse.setResultMsg("Failed to send SMS : " + response.getStatusLine().getReasonPhrase());
						LOGGER.debug("======== RESPONSE SmsServiceImpl.sendSMS() failed - reason: " + response.getStatusLine().getReasonPhrase());
			        }
	        	}
	        	finally {
	        		response.close();
	        		httpClient.close();
	        	}
	        }
		} 
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			smsResponse.setSuccess(false);
			smsResponse.setResultCode("400");
			smsResponse.setResultMsg(e.getMessage());
		}
		
		return smsResponse;
	}
}
