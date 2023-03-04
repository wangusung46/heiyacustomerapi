package com.heiya.mobileapi.otp.service;

import com.heiya.mobileapi.util.ReportUtils;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReportUtils reportUtils;

    @Override
    public Object export(String orderNo) throws Exception {
        LOGGER.info("======== START ReportServiceImpl.export");
        Map<String, Object> param = new HashMap<>();
        param.put("ORDER_NO", orderNo);

        LOGGER.info("======== COMPLETE ReportServiceImpl.export");
        return reportUtils.exportHtml("HEIYA UNDELIVER.jrxml", param);
    }

}
