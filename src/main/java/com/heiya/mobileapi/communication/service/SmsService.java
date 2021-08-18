package com.heiya.mobileapi.communication.service;

import com.heiya.mobileapi.communication.dto.response.SendSmsDTOResponse;

/**
 * @author Dian Krisnanjaya
 *
 */

public interface SmsService {
	
	public SendSmsDTOResponse sendSMS(String phoneNo, String message) throws Exception;
}
