package com.heiya.mobileapi.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heiya.mobileapi.customer.dto.request.CustomerChangePasswordDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerLoginDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerRegistrationDTORequest;
import com.heiya.mobileapi.customer.dto.request.CustomerUpdateDTORequest;
import com.heiya.mobileapi.customer.dto.request.DeleteAccountDTORequest;
import com.heiya.mobileapi.customer.dto.request.ForgotPasswordDTORequest;
import com.heiya.mobileapi.customer.dto.response.CustomerDetailDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerLoginDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerMobileVersionDTOResponse;
import com.heiya.mobileapi.customer.dto.response.CustomerNameDTOResponse;
import com.heiya.mobileapi.customer.service.CustomerService;
import com.heiya.mobileapi.dto.response.BaseResponse;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dian Krisnanjaya This controller will handle all of customer inquiry
 * and management
 *
 */
@RestController
@RequestMapping("/v1/customer")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService custService;

    @ApiOperation("Customer Login API - Handle customer sign-in process to the app using mobile no & password")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doLogin(@RequestBody CustomerLoginDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doLogin");
        try {
            CustomerLoginDTOResponse response = custService.performCustomerLogin(request);
            LOGGER.info("======== COMPLETED CustomerController.doLogin");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Customer Logout API - Handle customer sign-out process from the app")
    @PostMapping(value = "/logout/{mobileNo}/{idToken}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doLogout(@PathVariable String mobileNo, @PathVariable String idToken) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doLogout mobile no " + mobileNo);
        try {
            CustomerLoginDTOResponse response = custService.performCustomerLogout(mobileNo, idToken);
            LOGGER.info("======== COMPLETED CustomerController.doLogout mobile no " + mobileNo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Customer Registration API - Handle customer registration process before they can use the app")
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doRegister(@RequestBody CustomerRegistrationDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doRegistration");
        try {
            BaseResponse response = custService.performCustomerRegistration(request);
            LOGGER.info("======== COMPLETED CustomerController.doRegistration");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Customer Detail Inquiry - Get customer information by Customer ID")
    @GetMapping(value = "/inquiry/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doInquiryByID(@PathVariable("customerId") Long customerId) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doInquiryByID");
        try {
            CustomerDetailDTOResponse response = custService.performCustomerInquiry(customerId);
            LOGGER.info("======== COMPLETED CustomerController.doInquiryByID");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Update/Save Customer Data - Perform data saving/update of existing customer")
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doUpdateAndSaveCustomer(@RequestBody CustomerUpdateDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doUpdateAndSaveCustomer");
        try {
            BaseResponse response = custService.performCustomerUpdate(request);
            LOGGER.info("======== COMPLETED CustomerController.doUpdateAndSaveCustomer");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Change Password of customer")
    @PostMapping(value = "/changepassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doChangePassword(@RequestBody CustomerChangePasswordDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doChangePassword");
        try {
            BaseResponse response = custService.changeCustomerPassword(request);
            LOGGER.info("======== COMPLETED CustomerController.doChangePassword");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Request Forgot Password with OTP validation")
    @PostMapping(value = "/requestforgotpassword/{phoneNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doRequestForgotPassword(@PathVariable("phoneNo") String phoneNo) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doRequestForgotPassword");
        try {
            BaseResponse response = custService.requestForgotPassword(phoneNo);
            LOGGER.info("======== COMPLETED CustomerController.doRequestForgotPassword");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Execute Forgot Password (save new password)")
    @PostMapping(value = "/executeforgotpassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doExecForgotPassword(@RequestBody ForgotPasswordDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doExecForgotPassword");
        try {
            BaseResponse response = custService.executeForgotPassword(request);
            LOGGER.info("======== COMPLETED CustomerController.doExecForgotPassword");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Customer Detail Inquiry - Get customer information by Phone Number")
    @GetMapping(value = "/inquiryphoneno/{mobileNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doInquiryByPhone(@PathVariable("mobileNo") String mobileNo) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doInquiryByPhone");
        try {
            CustomerNameDTOResponse response = custService.performCustomerInquiryByPhone(mobileNo);
            LOGGER.info("======== COMPLETED CustomerController.doInquiryByPhone");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Mobile Inquiry - Get mobile platform and version information by platform and version")
    @GetMapping(value = "/inquiryversion/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doInquiryMobile(Device device, @PathVariable("version") String version) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doInquiryMobile : platform " + device.getDevicePlatform().name() + " and version " + version);
        try {
            CustomerMobileVersionDTOResponse response = custService.performCheckMobileVersion(device, version);
            LOGGER.info("======== COMPLETED CustomerController.doInquiryMobile : platform " + device.getDevicePlatform().name() + " and version " + version);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Delete Customer")
    @PostMapping(value = "/delete-customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doDeleteCustomer(@RequestBody DeleteAccountDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doDeleteCustomer");
        try {
            BaseResponse response = custService.performDeleteAccount(request.getMobileNo());
            LOGGER.info("======== COMPLETED CustomerController.doDeleteCustomer");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @ApiOperation("Delete Customer")
    @PostMapping(value = "/undelete-customer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doUnDeleteCustomer(@RequestBody DeleteAccountDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doDeleteCustomer");
        try {
            BaseResponse response = custService.performUnDeleteAccount(request.getMobileNo());
            LOGGER.info("======== COMPLETED CustomerController.doDeleteCustomer");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
