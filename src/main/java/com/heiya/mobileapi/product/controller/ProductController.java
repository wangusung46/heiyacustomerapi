package com.heiya.mobileapi.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.product.dto.request.BannerDTORequest;
import com.heiya.mobileapi.product.dto.request.ProductDTORequest;
import com.heiya.mobileapi.product.dto.request.ProductImagesDTORequest;
import com.heiya.mobileapi.product.dto.response.BannerListDTOResponse;
import com.heiya.mobileapi.product.dto.response.MachineDetailListDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductDetailDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductListDTOResponse;
import com.heiya.mobileapi.product.dto.response.TasteListDTOResponse;
import com.heiya.mobileapi.product.service.ProductService;

import io.swagger.annotations.ApiOperation;

/**
 * @author Dian Krisnanjaya This controller will handle all of product query and
 * maintenance
 *
 */
@RestController
@RequestMapping("/v1/product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService prodService;

    @ApiOperation("Get List of Product Banner & Promotion - Provide list of current promotion/banner images for mobile app to be displayed on home screen")
    @GetMapping(value = "/promotions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doListPromo() throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doListPromo");
        BannerListDTOResponse response = null;

        try {
            response = prodService.getCurrentPromotionBanner();
            LOGGER.info("======== COMPLETED ProductController.doListPromo");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Add New Product Banner & Promotion - Save new banner/promotion image for mobile app to be displayed on home screen")
    @PostMapping(value = "/addPromo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doAddNewPromo(@RequestBody BannerDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doAddNewPromo");
        BaseResponse response = null;

        try {
            response = prodService.saveBanner(null, request); //no need to pass bannerId for saving
            LOGGER.info("======== COMPLETED CustomerController.doAddNewPromo");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Update Product Banner & Promotion - Update existing banner/promotion image for mobile app to be displayed on home screen")
    @PutMapping(value = "/updatePromo/{bannerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doUpdatePromo(@PathVariable Long bannerId, @RequestBody BannerDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doUpdatePromo");
        BaseResponse response = null;

        try {
            response = prodService.saveBanner(bannerId, request);
            LOGGER.info("======== COMPLETED CustomerController.doUpdatePromo");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Delete Product Banner & Promotion - Delete banner/promotion image")
    @DeleteMapping(value = "/deletePromo/{bannerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doDeletePromo(@PathVariable String bannerId) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doDeletePromo with bannerId : " + bannerId);
        BaseResponse response = null;

        try {
            response = prodService.deleteBanner(bannerId);
            LOGGER.info("======== COMPLETED CustomerController.doDeletePromo with bannerId : " + bannerId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Get List of Product - Provide list of all products for mobile app to be displayed on home screen")
    @GetMapping(value = "/allProducts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doListProducts() throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doListProducts");
        ProductListDTOResponse response = null;

        try {
            response = prodService.getAllProducts();
            LOGGER.info("======== COMPLETED ProductController.doListProducts");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Get Product by ID - Provide detail info of a product for mobile app to be displayed on order screen")
    @GetMapping(value = "/query/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doGetProductById(@PathVariable Long productId) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doGetProductById");
        ProductDetailDTOResponse response = null;

        try {
            response = prodService.getProductById(productId);
            LOGGER.info("======== COMPLETED ProductController.doGetProductById");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Add New Product - Save new product for mobile app to be displayed on home screen")
    @PostMapping(value = "/addProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doAddProduct(@RequestBody ProductDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doAddProduct");
        BaseResponse response = null;

        try {
            response = prodService.saveProduct(null, request); //no need to pass productId for saving
            LOGGER.info("======== COMPLETED CustomerController.doAddProduct");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Update Product - Update product for mobile app to be displayed on home screen")
    @PutMapping(value = "/updateProduct/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doUpdateProduct(@PathVariable Long productId, @RequestBody ProductDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doUpdateProduct");
        BaseResponse response = null;

        try {
            response = prodService.saveProduct(productId, request); //need to pass productId for updating
            LOGGER.info("======== COMPLETED CustomerController.doUpdateProduct");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Delete Product - Delete a product from catalogue")
    @DeleteMapping(value = "/deleteProduct/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doDeleteProduct(@PathVariable String productId) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doDeleteProduct with productId : " + productId);
        BaseResponse response = null;

        try {
            response = prodService.deleteProduct(productId);
            LOGGER.info("======== COMPLETED CustomerController.doDeleteProduct with productId : " + productId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Add New Product Images - Save new images for a product")
    @PostMapping(value = "/addProductImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doAddProductImage(@RequestBody ProductImagesDTORequest request) throws JsonProcessingException {
        LOGGER.info("\n\n======== START CustomerController.doAddProductImage");
        BaseResponse response = null;

        try {
            response = prodService.saveNewProductImage(request);
            LOGGER.info("======== COMPLETED CustomerController.doAddProductImage");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Get Machine Detail by Product - Query all machine locations by product ID to be displayed on product detail screen.")
    @GetMapping(value = "/query/machine/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doGetMachineLocationByProductId(@PathVariable Long productId) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doGetMachineLocationByProductId");
        MachineDetailListDTOResponse response = null;

        try {
            //response = prodService.getMachineLocationByProductId(productId); //no need to filter by product id
            response = prodService.getActiveMachineList(); //find by machine status
            LOGGER.info("======== COMPLETED ProductController.doGetMachineLocationByProductId");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @ApiOperation("Get Machine Detail by Status - Query all machine locations by status to be displayed on product detail screen.")
    @GetMapping(value = "/query/machine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doGetMachineLocationByStatus() throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doGetMachineLocationByProductId");
        MachineDetailListDTOResponse response = new MachineDetailListDTOResponse();

        try {
            //response = prodService.getMachineLocationByProductId(productId); //no need to filter by product id
            response = prodService.getMachineLocationByStatus(); //find by machine status
            LOGGER.info("======== COMPLETED ProductController.doGetMachineLocationByProductId");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Sync Product, Machine, Pattern (Mini Info) - Perform syncronization with HW Server for all master data related to machine & goods.")
    @PutMapping(value = "/information/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doSyncMiniInfo() throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doSyncMiniInfo");
        BaseResponse response = null;

        try {
            response = prodService.syncMiniInformation();
            LOGGER.info("======== COMPLETED ProductController.doSyncMiniInfo");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation("Get List of Taste by Product - It will return tastes of product for customer to choose in order detail.")
    @GetMapping(value = "/query/taste/{goodsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doTasteByProductId(@PathVariable int goodsId) throws JsonProcessingException {
        LOGGER.info("\n\n======== START ProductController.doTasteByProductId");
        TasteListDTOResponse response = null;

        try {
            response = prodService.getTasteByProductId(goodsId);
            LOGGER.info("======== COMPLETED ProductController.doTasteByProductId");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
