/**
 * 
 */
package com.heiya.mobileapi.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Dian Krisnanjaya
 *
 */
@Component("remoteClientLogger")
public class RemoteClientLogger extends HandlerInterceptorAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClientLogger.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (!request.getRemoteAddr().equals("127.0.0.1") && !request.getRemoteAddr().equals("37.44.244.137")) {
			LOGGER.info(">>>> API Request from IP Address : " + request.getRemoteAddr());
		}
		return true;
	}
}
