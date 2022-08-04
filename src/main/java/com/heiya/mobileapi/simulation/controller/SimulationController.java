package com.heiya.mobileapi.simulation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heiya.mobileapi.product.dto.response.MachineDetailDTOResponse;
import com.heiya.mobileapi.product.dto.response.MachineDetailListDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductDTOResponsev2;
import com.heiya.mobileapi.product.dto.response.ProductDetailDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductImageDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductListDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductListDTOResponsev2;
import com.heiya.mobileapi.product.service.ProductService;

import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * @author Dian Krisnanjaya This controller will handle all of product query and
 * maintenance
 *
 */
@Controller
@RequestMapping("/v1/simulation")
public class SimulationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationController.class);

    @Autowired
    private ProductService prodService;
    
    @GetMapping(value = "/")
    public String index() {
        LOGGER.info("\n\n======== START ProductController.index");
        try {
            LOGGER.info("======== COMPLETED ProductController.index");
            return "index";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "index";
        }
    }

    @ApiOperation("V2 - Get List of Product - Provide list of all products for mobile app to be displayed on home screen")
    @GetMapping(value = "products", produces = MediaType.APPLICATION_JSON_VALUE)
    public String doListProductsv2(Model model) {
        LOGGER.info("\n\n======== START ProductController.doListProductsv2");
        ProductListDTOResponsev2 responses = new ProductListDTOResponsev2();
        List<ProductDTOResponsev2> checkNotificationDTOResponses = new ArrayList<>();
        try {
            responses = prodService.getAllProductsv2();
            checkNotificationDTOResponses = responses.getProductList();
            model.addAttribute("products", checkNotificationDTOResponses);
            LOGGER.info("======== COMPLETED ProductController.doListProductsv2");
            return "p-general-product";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "p-general-product";
        }
    }

    @ApiOperation("V2 - Get Machine Detail by Status - Query all machine locations by status to be displayed on product detail screen.")
    @GetMapping(value = "products/{goodsId}/machines", produces = MediaType.APPLICATION_JSON_VALUE)
    public String doGetMachineLocationByGoodsId(@PathVariable Integer goodsId, Model model) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doGetMachineLocationByProductId");
        try {
            MachineDetailListDTOResponse machineDetailListDTOResponse = prodService.getMachineLocationByProductIdV2(goodsId); //no need to filter by product id
            List<MachineDetailDTOResponse> machineDetailDTOResponses = machineDetailListDTOResponse.getMachineList();
            model.addAttribute("machines", machineDetailDTOResponses);
            LOGGER.info("======== COMPLETED ProductController.doGetMachineLocationByProductId");
            return "p-machine-by-product";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "p-machine-by-product";
        }
    }

    @ApiOperation("V2 - Get Product by Product ID and Machine ID V2 - Provide detail info of a product for mobile app to be displayed on order screen")
    @GetMapping(value = "products/{goodsId}/machines/{machineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String doGetProductByProductIdAndMachineId(@PathVariable Integer goodsId, @PathVariable Long machineId, Model model) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doGetProductByProductIdAndMachineId");
        try {
            ProductDetailDTOResponse response = prodService.getProductByProductIdAndMachineId(goodsId, machineId);
            List<ProductImageDTOResponse> productImageList = response.getProductImageList();
            model.addAttribute("products", response);
            model.addAttribute("images", productImageList);
            LOGGER.info("======== COMPLETED ProductController.doGetProductByProductIdAndMachineId");
            return "p-select-product";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "p-select-product";
        }
    }

    // By Location
    @ApiOperation("V2 - Get Machine Detail by Product V2 - Query all machine locations by product ID to be displayed on product detail screen.")
    @GetMapping(value = "machines", produces = MediaType.APPLICATION_JSON_VALUE)
    public String doGetAllMachineLocation(Model model) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doGetMachineLocationByProductId");
        try {
            MachineDetailListDTOResponse response = prodService.getActiveMachineList(); //find by machine status
            List<MachineDetailDTOResponse> machineDetailDTOResponses = response.getMachineList();
            model.addAttribute("machines", machineDetailDTOResponses);
            LOGGER.info("======== COMPLETED ProductController.doGetMachineLocationByProductId");
            return "m-machine-all";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "m-machine-all";
        }
    }
    
    @ApiOperation("V2 - Get List of Product - Provide list of all products for mobile app to be displayed on home screen")
    @GetMapping(value = "machines/{machineId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public String doListProductsByMachineId(@PathVariable Long machineId, Model model) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doListProductsByMachineId");
        try {
            ProductListDTOResponse response = prodService.getAllProductsByMachineId(machineId);
            List<ProductDTOResponse> checkNotificationDTOResponses = response.getProductList();
            model.addAttribute("products", checkNotificationDTOResponses);
            LOGGER.info("======== COMPLETED ProductController.doListProductsByMachineId");
            return "m-general-product";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "m-general-product";
        }
    }
    
    @ApiOperation("V2 - Get Product by Product ID and Machine ID - Provide detail info of a product for mobile app to be displayed on order screen")
    @GetMapping(value = "machines/{machineId}/products/{goodsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String doGetProductByMachineIdAndProductId(@PathVariable Integer goodsId, @PathVariable Long machineId, Model model) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doGetProductByProductIdAndMachineId");
        try {
            ProductDetailDTOResponse response = prodService.getProductByProductIdAndMachineId(goodsId, machineId);
            List<ProductImageDTOResponse> productImageList = response.getProductImageList();
            model.addAttribute("products", response);
            model.addAttribute("images", productImageList);
            LOGGER.info("======== COMPLETED ProductController.doGetProductByProductIdAndMachineId");
            return "m-select-product";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "m-select-product";
        }
    }
}
