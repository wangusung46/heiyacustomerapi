package com.heiya.mobileapi.util;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.database.service.CRUDService;

@Component("xenditConnectUtil")
public class XenditConnectUtil {

    @Autowired
    private CRUDService crudService;

    public String getEncodedServerKey() {
        String formattedServerKey = "";
        try {
            String xenditServerKey = crudService.getGlobalConfigParamByKey(GlobalConstants.PAY_GW_XENDIT_KEY).concat(":");
            String encodedServerKey = Base64.getEncoder().encodeToString(xenditServerKey.getBytes());
            formattedServerKey = "Basic".concat(" ").concat(encodedServerKey);
        } catch (Exception e) {
        }
        return formattedServerKey;
    }
}
