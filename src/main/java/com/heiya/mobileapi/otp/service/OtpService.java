package com.heiya.mobileapi.otp.service;

/**
 * @author Dian Krisnanjaya
 *
 */

public interface OtpService {
	
	public int generateEmailOTP(String key) throws Exception;
	public int getEmailOtp(String key) throws Exception;
	public void clearEmailOtp(String key) throws Exception;
}
