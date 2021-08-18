package com.heiya.mobileapi.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.product.dto.request.BannerDTORequest;
import com.heiya.mobileapi.product.dto.request.ProductDTORequest;
import com.heiya.mobileapi.product.dto.request.ProductImagesDTORequest;
import com.heiya.mobileapi.product.dto.response.BannerListDTOResponse;
import com.heiya.mobileapi.product.dto.response.DiscountDTOResponse;
import com.heiya.mobileapi.product.dto.response.MachineDetailListDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductDetailDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductListDTOResponse;
import com.heiya.mobileapi.product.dto.response.TasteListDTOResponse;

/**
 * @author Dian Krisnanjaya
 *
 */
public interface ProductService {

    public BannerListDTOResponse getCurrentPromotionBanner() throws Exception;

    public BaseResponse saveBanner(Long bannerId, BannerDTORequest request) throws JsonProcessingException;

    public BaseResponse deleteBanner(String bannerId) throws JsonProcessingException;

    public ProductListDTOResponse getAllProducts() throws Exception;

    public ProductDetailDTOResponse getProductById(Long productId) throws Exception;

    public BaseResponse saveProduct(Long productId, ProductDTORequest request) throws JsonProcessingException;

    public BaseResponse deleteProduct(String productId) throws JsonProcessingException;

    public BaseResponse saveNewProductImage(ProductImagesDTORequest request) throws JsonProcessingException;

    public MachineDetailListDTOResponse getMachineLocationByProductId(Long productId) throws Exception;
    
    public MachineDetailListDTOResponse getMachineLocationByStatus() throws Exception;

    public MachineDetailListDTOResponse getActiveMachineList() throws Exception;

    public BaseResponse syncMiniInformation() throws JsonProcessingException;

    public TasteListDTOResponse getTasteByProductId(int goodsId) throws Exception;

    public DiscountDTOResponse getProductDiscount(int goodsId) throws Exception;
}
