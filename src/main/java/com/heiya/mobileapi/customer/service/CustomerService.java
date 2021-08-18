package com.heiya.mobileapi.customer.service;

import com.heiya.mobileapi.customer.dto.request.CustomerChangePasswordDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerLoginDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerRegistrationDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerUpdateDTORequest;
import com.heiya.mobileapi.customer.dto.request.ForgotPasswordDTORequest;
import com.heiya.mobileapi.customer.dto.response.CustomerDetailDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerLoginDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerMobileVersionDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerNameDTOResponse;
import com.heiya.mobileapi.dto.response.BaseResponse;
import org.springframework.mobile.device.Device;

/**
 * @author Dian Krisnanjaya
 *
 */
public interface CustomerService {

    public CustomerLoginDTOResponse performCustomerLogin(CustomerLoginDTORequest request) throws Exception;

    public CustomerLoginDTOResponse performCustomerLogout(String mobileNo, String idToken) throws Exception;

    public BaseResponse performCustomerRegistration(CustomerRegistrationDTORequest request) throws Exception;

    public CustomerDetailDTOResponse performCustomerInquiry(Long customerId) throws Exception;

    public BaseResponse performCustomerUpdate(CustomerUpdateDTORequest request) throws Exception;

    public BaseResponse changeCustomerPassword(CustomerChangePasswordDTORequest request) throws Exception;

    public BaseResponse requestForgotPassword(String phoneNo) throws Exception;

    public BaseResponse executeForgotPassword(ForgotPasswordDTORequest request) throws Exception;

    public CustomerNameDTOResponse performCustomerInquiryByPhone(String mobileNo) throws Exception;

    public CustomerMobileVersionDTOResponse performCheckMobileVersion(Device device, String androidVersion) throws Exception;
}
