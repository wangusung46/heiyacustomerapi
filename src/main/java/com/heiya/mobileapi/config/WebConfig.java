/**
 * 
 */
package com.heiya.mobileapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.heiya.mobileapi.util.RemoteClientLogger;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * @author Dian Krisnanjaya
 *
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RemoteClientLogger());
                registry.addInterceptor(deviceResolverHandlerInterceptor());
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addRedirectViewController("/v2/api-docs", "/v2/api-docs");
	    registry.addRedirectViewController("/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
	    registry.addRedirectViewController("/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
	    registry.addRedirectViewController("/swagger-resources", "/swagger-resources");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
	    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
        
        @Bean
public DeviceResolverHandlerInterceptor 
        deviceResolverHandlerInterceptor() {
    return new DeviceResolverHandlerInterceptor();
}

@Bean
public DeviceHandlerMethodArgumentResolver 
        deviceHandlerMethodArgumentResolver() {
    return new DeviceHandlerMethodArgumentResolver();
}

//@Override
//public void addInterceptors(InterceptorRegistry registry) {
//    registry.addInterceptor(deviceResolverHandlerInterceptor());
//}

@Override
public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(deviceHandlerMethodArgumentResolver());
}
}
