package com.heiya.mobileapi.customer.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heiya.mobileapi.communication.dto.response.SendSmsDTOResponse;
import com.heiya.mobileapi.communication.service.SmsService;
import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.customer.dto.request.CustomerChangePasswordDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerLoginDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerRegistrationDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerUpdateDTORequest;
import com.heiya.mobileapi.customer.dto.request.ForgotPasswordDTORequest;
import com.heiya.mobileapi.customer.dto.response.CustomerDetailDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerLoginDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerMobileVersionDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerNameDTOResponse;
import com.heiya.mobileapi.customer.model.Customer;
import com.heiya.mobileapi.customer.model.Token;
import com.heiya.mobileapi.customer.repository.CustomerRepository;
import com.heiya.mobileapi.customer.repository.TokenRepository;
import com.heiya.mobileapi.database.service.CRUDService;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.otp.service.OtpService;
import org.springframework.mobile.device.Device;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    @Qualifier("paymentRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private SmsService smsService;

    @Autowired
    public OtpService otpService;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private CRUDService crudService;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public CustomerLoginDTOResponse performCustomerLogin(CustomerLoginDTORequest request) throws Exception {
        CustomerLoginDTOResponse response = new CustomerLoginDTOResponse();
        LOGGER.info("======== START CustomerServiceImpl.performCustomerLogin() with request : " + mapper.writeValueAsString(request));

        String mobileNo = request.getMobileNo();
        if (mobileNo.startsWith("+62")) {
            mobileNo = mobileNo.replace("+62", "0");
        }
        Customer customer = customerRepo.findByMobileNo(mobileNo);

        if (customer != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean isPwdMatched = passwordEncoder.matches(request.getPassword(), customer.getPassword());
            if (isPwdMatched) {
                Token token = tokenRepository.findByIdTokenAndIdCustomer(request.getIdToken(), customer.getId());
                //update login status
//                customer.setIsLoggedIn("Y");
                customerRepo.save(customer);

                if (token == null) {
                    LOGGER.info("======== CustomerServiceImpl.performCustomerLogin() - Get New Token : " + request.getIdToken());
                    Token newToken = new Token();
                    newToken.setIdCustomer(customer.getId());
                    newToken.setIdToken(request.getIdToken());
                    newToken.setMobileNo(request.getMobileNo());
                    newToken.setIsLoggedin(true);
                    tokenRepository.save(newToken);
                } else{
                    LOGGER.info("======== CustomerServiceImpl.performCustomerLogin() - Set Loggedin : " + true);
                    token.setIsLoggedin(true);
                    tokenRepository.save(token);
                }
                
//                System.out.println(token.getIsLoggedin());
//                
//                if (token = null && token.getIsLoggedin() == false) {
//                    LOGGER.info("======== CustomerServiceImpl.performCustomerLogin() - Set Loggedin:" + token.getIsLoggedin());
//                    Token newToken = new Token();
//                    newToken.setIsLoggedin(true);
//                    tokenRepository.save(newToken);
//                }

                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Successfully logged-in");
                response.setCustomerId(customer.getId());
                response.setFirstName(customer.getFirstName());
                response.setLastName(customer.getLastName());
                response.setMobileNo(customer.getMobileNo());
//                response.setIsLoggedIn(customer.getIsLoggedIn());
                LOGGER.info("======== CustomerServiceImpl.performCustomerLogin() - Login success for customer mobile no:" + customer.getMobileNo());
            } else {
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("Wrong/invalid password");
                response.setIsLoggedIn("N");
                LOGGER.info("======== CustomerServiceImpl.performCustomerLogin() - Login failed due to invalid password");
            }
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("Mobile phone no is not registered");
            response.setIsLoggedIn("N");
            LOGGER.info("======== CustomerServiceImpl.performCustomerLogin() - Login failed since mobile no " + request.getMobileNo() + " could not be found");
        }
        return response;
    }

    @Override
    public CustomerLoginDTOResponse performCustomerLogout(String mobileNo, String idToken) throws Exception {
        CustomerLoginDTOResponse response = new CustomerLoginDTOResponse();
        LOGGER.info("======== START CustomerServiceImpl.performCustomerLogout() with mobile no " + mobileNo);

        if (mobileNo.startsWith("+62")) {
            mobileNo = mobileNo.replace("+62", "0");
        }

        Customer customer = customerRepo.findByMobileNo(mobileNo);
        Token token = tokenRepository.findByIdTokenAndIdCustomer(idToken, customer.getId());

        if (customer != null) {
            //update login status
            token.setIsLoggedin(false);
            customerRepo.save(customer);

            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully logged-out");
            response.setFirstName(customer.getFirstName());
            response.setLastName(customer.getLastName());
            response.setMobileNo(customer.getMobileNo());
//            response.setIsLoggedIn(customer.getIsLoggedIn());
            LOGGER.info("======== END CustomerServiceImpl.performCustomerLogout() - Logout mobile no " + mobileNo + " success");
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("Logout failed. Mobile phone could not be found");
            response.setIsLoggedIn("N");
            LOGGER.info("======== END CustomerServiceImpl.performCustomerLogout() - Logout mobile no " + mobileNo + " failed");
        }
        return response;
    }

    @Override
    public BaseResponse performCustomerRegistration(CustomerRegistrationDTORequest request) {
        BaseResponse response = new BaseResponse();

        try {
            LOGGER.info("======== START CustomerServiceImpl.performCustomerRegistration() with request : " + mapper.writeValueAsString(request));

            //validation
            Customer customerVal1 = customerRepo.findByMobileNo(request.getMobileNo());
            if (customerVal1 != null) {
                //response
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("Mobile no you entered is already registered");
                return response;
            }

            Pattern pattern = Pattern.compile("^(\\d){10,13}$");
            Matcher m = pattern.matcher(request.getMobileNo());
            if (!m.matches()) {
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("Mobile no you entered is invalid. Should be number only (10-13 characters)");
                return response;
            }

            Customer customerVal2 = customerRepo.findByEmail(request.getEmail());
            if (customerVal2 != null) {
                //response
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("Email address you entered is already registered");
                return response;
            }

            //Save the request to DB
            Customer customer = new Customer();
            customer.setFirstName(request.getFirstName());
            customer.setLastName(request.getLastName());
            customer.setGender(request.getGender());
            customer.setDob(request.getDob());
            customer.setMobileNo(request.getMobileNo());
            customer.setEmail(request.getEmail());
            customer.setAddress(request.getAddress());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
            customer.setIsActive(request.getIsActive());
//            customer.setIsLoggedIn(request.getIsLoggedIn());
            customerRepo.save(customer);

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Registration success");
            LOGGER.info("======== CustomerServiceImpl.performCustomerRegistration() - Registration with email " + customer.getEmail() + " success");
        } catch (Exception e) {
            e.printStackTrace();
            //response
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Registration failed : " + e.getMessage());
        }

        return response;
    }

    @Override
    public CustomerDetailDTOResponse performCustomerInquiry(Long customerId) throws Exception {
        CustomerDetailDTOResponse response = new CustomerDetailDTOResponse();
        LOGGER.info("======== START CustomerServiceImpl.performCustomerInquiry() with cust ID : " + customerId);

        Optional<Customer> customer = customerRepo.findById(customerId);

        if (customer != null) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrievd customer data");
            response.setCustomerId(customer.get().getId());
            response.setFirstName(customer.get().getFirstName());
            response.setLastName(customer.get().getLastName());
            response.setGender(customer.get().getGender());
            response.setDob(customer.get().getDob());
            response.setMobileNo(customer.get().getMobileNo());
            response.setEmail(customer.get().getEmail());
            response.setAddress(customer.get().getAddress());
            response.setIsActive(customer.get().getIsActive());
//            response.setIsLoggedIn(customer.get().getIsLoggedIn());
            LOGGER.info("======== CustomerServiceImpl.performCustomerInquiry() - Customer found :" + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("Customer not found");
            LOGGER.info("======== CustomerServiceImpl.performCustomerInquiry() - Customer not found");
        }
        return response;
    }

    @Override
    public BaseResponse performCustomerUpdate(CustomerUpdateDTORequest request) {
        BaseResponse response = new BaseResponse();

        try {
            LOGGER.info("======== START CustomerServiceImpl.performCustomerUpdate() with request : " + mapper.writeValueAsString(request));

            //validation
            Pattern pattern = Pattern.compile("^(\\d){10,13}$");
            Matcher m = pattern.matcher(request.getMobileNo());
            if (!m.matches()) {
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("Mobile no you entered is invalid. Should be number only (10-13 characters)");
                return response;
            }

            //Save the request to DB
            Optional<Customer> customer = customerRepo.findById(request.getCustomerId());
            customer.get().setFirstName(request.getFirstName());
            customer.get().setLastName(request.getLastName());
            customer.get().setGender(request.getGender());
            customer.get().setDob(request.getDob());
            customer.get().setMobileNo(request.getMobileNo());
            customer.get().setEmail(request.getEmail());
            customer.get().setAddress(request.getAddress());
            customerRepo.save(customer.get());

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully updated");
            LOGGER.info("======== CustomerServiceImpl.performCustomerUpdate() - Update customer " + customer.get().getEmail() + " success");
        } catch (Exception e) {
            e.printStackTrace();
            //response
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Failed to update customer: " + e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse changeCustomerPassword(CustomerChangePasswordDTORequest request) {
        BaseResponse response = new BaseResponse();

        try {
            LOGGER.info("======== START CustomerServiceImpl.changeCustomerPassword() with request : " + mapper.writeValueAsString(request));

            //validation
            Optional<Customer> customer = customerRepo.findById(request.getCustomerId());

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean isPwdMatched = passwordEncoder.matches(request.getOldPassword(), customer.get().getPassword());

            if (!isPwdMatched) {
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("You entered wrong old password.");
                return response;
            }

            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                response.setSuccess(false);
                response.setResultCode("413");
                response.setResultMsg("New Password & Confirm Password does not match.");
                return response;
            }

            //Save the request to DB
            customer.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
            customerRepo.save(customer.get());

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully updated");
            LOGGER.info("======== CustomerServiceImpl.changeCustomerPassword() - Update Password success");
        } catch (Exception e) {
            e.printStackTrace();
            //response
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Failed to update customer password: " + e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse requestForgotPassword(String phoneNo) {
        BaseResponse response = new BaseResponse();

        try {
            LOGGER.info("======== START CustomerServiceImpl.requestForgotPassword() for phone no: " + phoneNo);

            //validation
            Pattern pattern = Pattern.compile("^(\\d){10,15}$");
            Matcher m = pattern.matcher(phoneNo);
            if (!m.matches()) {
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("Mobile no you entered is invalid. Should be number only (10-15 characters)");
                return response;
            }

            if (phoneNo.startsWith("62")) {
                phoneNo = phoneNo.replace("62", "0");
            }
            if (phoneNo.startsWith("+62")) {
                phoneNo = phoneNo.replace("+62", "0");
            }
            Customer customer = customerRepo.findByMobileNo(phoneNo);
            if (customer == null) {
                response.setSuccess(false);
                response.setResultCode("404");
                response.setResultMsg("The phone number is not registered yet as a customer");
                return response;
            }
            //End Validation

            //Start sending SMS OTP
            if (phoneNo.startsWith("0")) {
                phoneNo = "62".concat(phoneNo.substring(1));
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            int otp = otpService.generateEmailOTP(username);

            LOGGER.info("\n======== Forgot Password OTP : " + otp);
            String message = "Your Forgot Password OTP code is " + otp + ". Please keep it confidential.";
            SendSmsDTOResponse smsResponse = this.smsService.sendSMS(phoneNo, message);

            if (smsResponse.isSuccess()) {
                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("SMS OTP for forgot password has been sent.");
                LOGGER.info("======== CustomerServiceImpl.requestForgotPassword() - Forgot password OTP sent");
                return response;
            } else {
                response.setSuccess(false);
                response.setResultCode("413");
                response.setResultMsg("Failed to send forgot password OTP");
                LOGGER.info("======== CustomerServiceImpl.requestForgotPassword() - Failed to send forgot password OTP");
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //response
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Failed to request forgot password: " + e.getMessage());
        }

        return response;
    }

    @Override
    public BaseResponse executeForgotPassword(ForgotPasswordDTORequest request) {
        BaseResponse response = new BaseResponse();

        try {
            LOGGER.info("======== START CustomerServiceImpl.executeForgotPassword() with request : " + mapper.writeValueAsString(request));

            //validation
            String phoneNo = request.getPhoneNo();
            if (phoneNo.startsWith("62")) {
                phoneNo = phoneNo.replace("62", "0");
            }
            if (phoneNo.startsWith("+62")) {
                phoneNo = phoneNo.replace("+62", "0");
            }

            Customer customer = customerRepo.findByMobileNo(phoneNo);
            if (customer == null) {
                response.setSuccess(false);
                response.setResultCode("404");
                response.setResultMsg("The phone number is not registered yet as a customer");
                return response;
            }

            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                response.setSuccess(false);
                response.setResultCode("412");
                response.setResultMsg("New Password & Confirm Password does not match");
                return response;
            }
            //End validation

            //Save the request to DB
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
            customerRepo.save(customer);

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("New password successfully updated");
            LOGGER.info("======== CustomerServiceImpl.executeForgotPassword() - Update Password success");
        } catch (Exception e) {
            e.printStackTrace();
            //response
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Failed to execute forgot password: " + e.getMessage());
        }

        return response;
    }

    @Override
    public CustomerNameDTOResponse performCustomerInquiryByPhone(String mobileNo) throws Exception {
        CustomerNameDTOResponse response = new CustomerNameDTOResponse();
        LOGGER.info("======== START CustomerServiceImpl.performCustomerInquiryByPhone() with Phone Number : " + mobileNo);

        if (mobileNo.startsWith("+62")) {
            mobileNo = mobileNo.replace("+62", "0");
        }
        Customer customer = customerRepo.findByMobileNo(mobileNo);

        if (customer != null) {
            if (customer.getIsActive().equals("Y")) {
                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg("Successfully retrievd customer data");
                response.setCustomerId(customer.getId());
                response.setFirstName(customer.getFirstName());
                response.setLastName(customer.getLastName());
                response.setEmail(customer.getEmail());
                response.setMobileNo(customer.getMobileNo());
                LOGGER.info("======== CustomerServiceImpl.performCustomerInquiryByPhone() - Customer found :" + mapper.writeValueAsString(response));
            } else {
                response.setSuccess(false);
                response.setResultCode("404");
                response.setResultMsg("Customer not actived");
                LOGGER.info("======== CustomerServiceImpl.performCustomerInquiryByPhone() - Customer not actived");
            }

        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("Customer not found");
            LOGGER.info("======== CustomerServiceImpl.performCustomerInquiryByPhone() - Customer not found");
        }
        return response;
    }

    @Override
    public CustomerMobileVersionDTOResponse performCheckMobileVersion(Device device, String version) throws Exception {
        CustomerMobileVersionDTOResponse response = new CustomerMobileVersionDTOResponse();
        String onDevice = device.getDevicePlatform().name();
        String androidVersion = crudService.getGlobalConfigParamByKey(GlobalConstants.VERSION_MOBILE_ANDROID);
        String iosVersion = crudService.getGlobalConfigParamByKey(GlobalConstants.VERSION_MOBILE_IOS);
//        String onDevice = "ANDROID";
//        String onDevice = "IOS";
        LOGGER.info("======== START CustomerServiceImpl.performCheckMobileVersion() with aplication version : " + version + " on " + mapper.writeValueAsString(device));
        switch (onDevice) {
            case "ANDROID":
                if (version.equals(androidVersion)) {
                    response.setDevicePlatform(onDevice);
                    response.setMobileVersion(version);
                    response.setExistMobileVersion("Android Version : " + androidVersion);
                    response.setSuccess(true);
                    response.setResultCode("200");
                    response.setResultMsg("Successfully update HEIYA version to " + androidVersion);
                    LOGGER.info("======== COMPLETE CustomerServiceImpl.performCheckMobileVersion() with aplication version : " + version + " on " + onDevice);
                } else {
                    response.setDevicePlatform(onDevice);
                    response.setMobileVersion(version);
                    response.setExistMobileVersion("Android Version : " + androidVersion);
                    response.setSuccess(false);
                    response.setResultCode("404");
                    response.setResultMsg("Please update HEIYA version to " + androidVersion);
                    LOGGER.warn("======== COMPLETE CustomerServiceImpl.performCheckMobileVersion() with aplication version : " + version + " on " + onDevice + " update to version " + androidVersion);
                }
                break;
            case "IOS":
                if (version.equals(iosVersion)) {
                    response.setDevicePlatform(onDevice);
                    response.setMobileVersion(version);
                    response.setExistMobileVersion("IOS Version : " + iosVersion);
                    response.setSuccess(true);
                    response.setResultCode("200");
                    response.setResultMsg("Successfully update HEIYA version to " + iosVersion);
                    LOGGER.info("======== COMPLETE CustomerServiceImpl.performCheckMobileVersion() with aplication version : " + version + " on " + onDevice);
                } else {
                    response.setDevicePlatform(onDevice);
                    response.setMobileVersion(version);
                    response.setExistMobileVersion("IOS Version : " + iosVersion);
                    response.setSuccess(false);
                    response.setResultCode("404");
                    response.setResultMsg("Please update HEIYA version to " + iosVersion);
                    LOGGER.warn("======== COMPLETE CustomerServiceImpl.performCheckMobileVersion() with aplication version : " + version + " on " + onDevice + " update to version " + iosVersion);
                }
                break;
            default:
                String desktopVersion = "Desktop User";
                response.setDevicePlatform(onDevice);
                response.setMobileVersion(null);
                response.setExistMobileVersion("Android Version : " + androidVersion + " and " + "IOS Version : " + iosVersion);
                response.setSuccess(true);
                response.setResultCode("200");
                response.setResultMsg(desktopVersion);
                LOGGER.info("======== COMPLETE CustomerServiceImpl.performCheckMobileVersion() with desktop version");
                break;
        }

        return response;
    }
}
