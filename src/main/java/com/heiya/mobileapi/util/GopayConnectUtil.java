package com.heiya.mobileapi.util;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.database.service.CRUDService;

@Component("gopayConnectUtil")
public class GopayConnectUtil {
	
	//@Value("${api.gopay.server.key}")
	//private String gopayServerKey;
	
	@Autowired
	private CRUDService crudService;
	
	public String getEncodedServerKey() {
		String formattedServerKey = "";
		try {
			String gopayServerKey = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_KEY1);
			String encodedServerKey = new String(Base64.getEncoder().encodeToString(gopayServerKey.getBytes()));
			formattedServerKey = "Basic".concat(" ").concat(encodedServerKey);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return formattedServerKey;
	}
}
