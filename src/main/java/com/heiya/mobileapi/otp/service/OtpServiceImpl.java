package com.heiya.mobileapi.otp.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OtpServiceImpl implements OtpService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OtpServiceImpl.class);
	
	//cache based on username and OPT MAX 8 
	private static final Integer EXPIRE_MINS = 5;

	private LoadingCache<String, Integer> otpCache;
	
	
	public OtpServiceImpl() {
		super();
		otpCache = CacheBuilder.newBuilder().
				expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}
	
	
	@Override
	public int generateEmailOTP(String key) throws Exception {
		LOGGER.info("======== START OtpServiceImpl.generateEmailOTP() with Key : " + key);
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		otpCache.put(key, otp);
		LOGGER.info("======== OtpServiceImpl.generateEmailOTP() - OTP code : "+otp);
		return otp;
	}
	
	
	@Override
	public int getEmailOtp(String key) throws Exception {
		LOGGER.info("======== START OtpServiceImpl.getEmailOtp() with username " + key);
		try {
			LOGGER.info("======== END OtpServiceImpl.getEmailOtp() - for username "+key);
			return otpCache.get(key); 
		}
		catch (Exception e) {
			return 0; 
		}
	}
	
	
	@Override
	public void clearEmailOtp(String key) {
		LOGGER.info("======== START OtpServiceImpl.clearEmailOtp() with key : " + key);
		otpCache.invalidate(key);
		LOGGER.info("======== OtpServiceImpl.clearEmailOtp() - Otp for key " + key + " is cleared");
	}
}
