package com.heiya.mobileapi.communication.service;

/**
 * @author Dian Krisnanjaya
 *
 */

public interface EmailService {
	
	public void sendMessage(String to, String subject, String message) throws Exception;
}
