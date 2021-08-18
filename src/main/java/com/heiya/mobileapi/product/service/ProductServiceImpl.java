package com.heiya.mobileapi.product.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heiya.mobileapi.constants.GlobalConstants;
import com.heiya.mobileapi.database.service.CRUDService;
import com.heiya.mobileapi.dto.response.BaseResponse;
import com.heiya.mobileapi.product.dto.request.BannerDTORequest;
import com.heiya.mobileapi.product.dto.request.ProductDTORequest;
import com.heiya.mobileapi.product.dto.request.ProductImagesDTORequest;
import com.heiya.mobileapi.product.dto.response.BannerDTOResponse;
import com.heiya.mobileapi.product.dto.response.BannerListDTOResponse;
import com.heiya.mobileapi.product.dto.response.CoffeeDTOResponse;
import com.heiya.mobileapi.product.dto.response.DiscountDTOResponse;
import com.heiya.mobileapi.product.dto.response.HWProductDTOResponse;
import com.heiya.mobileapi.product.dto.response.MachineDetailDTOResponse;
import com.heiya.mobileapi.product.dto.response.MachineDetailListDTOResponse;
import com.heiya.mobileapi.product.dto.response.MachineInfoDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductDetailDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductImageDTOResponse;
import com.heiya.mobileapi.product.dto.response.ProductListDTOResponse;
import com.heiya.mobileapi.product.dto.response.TasteDTOResponse;
import com.heiya.mobileapi.product.dto.response.TasteListDTOResponse;
import com.heiya.mobileapi.product.model.Banner;
import com.heiya.mobileapi.product.model.Discount;
import com.heiya.mobileapi.product.model.Machine;
import com.heiya.mobileapi.product.model.Product;
import com.heiya.mobileapi.product.model.ProductImages;
import com.heiya.mobileapi.product.repository.BannerRepository;
import com.heiya.mobileapi.product.repository.DiscountRepository;
import com.heiya.mobileapi.product.repository.MachineRepository;
import com.heiya.mobileapi.product.repository.ProductImagesRepository;
import com.heiya.mobileapi.product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    @Qualifier("paymentRestTemplate")
    private RestTemplate restTemplate;

    @Value("${api.hw.api.baseurl2}")
    private String hwApiBaseUrl2;

    @Autowired
    private CRUDService crudService;

    @Autowired
    private BannerRepository bannerRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private MachineRepository machineRepo;

    @Autowired
    private ProductImagesRepository productImagesRepo;

    @Autowired
    private DiscountRepository discountRepo;

    private ObjectMapper mapper = new ObjectMapper();

    /*
	 * PROMOTION BANNER PORTION
     */
    @Override
    public BannerListDTOResponse getCurrentPromotionBanner() throws Exception {
        BannerListDTOResponse response = new BannerListDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.getCurrentPromotionBanner()");
        List<Banner> bannerList = bannerRepo.findAll();

        if (bannerList != null && !bannerList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<BannerDTOResponse> bannerResList = new ArrayList<BannerDTOResponse>();
            for (Banner banner : bannerList) {
                BannerDTOResponse bannerRes = new BannerDTOResponse();
                bannerRes.setBannerId(banner.getId());
                bannerRes.setBannerName(banner.getBannerName());
                bannerRes.setBannerPath(banner.getBannerPath());
                bannerResList.add(bannerRes);
            }
            response.setBannerList(bannerResList);
            LOGGER.info("======== END ProductServiceImpl.getCurrentPromotionBanner() - Banner response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("There are no any banners/promotions right now");
            LOGGER.info("======== END ProductServiceImpl.getCurrentPromotionBanner() - No banner/promotions");
        }
        return response;
    }

    @Override
    public BaseResponse saveBanner(Long bannerId, BannerDTORequest request) throws JsonProcessingException {
        BaseResponse response = new BaseResponse();
        LOGGER.info("======== START ProductServiceImpl.saveNewBanner() with request : " + mapper.writeValueAsString(request));

        try {
            //save or update banner
            Banner banner = new Banner();
            if (bannerId != null) { //means this is update
                banner.setId(bannerId);
            }
            banner.setBannerName(request.getBannerName());
            banner.setBannerPath(request.getBannerPath());
            bannerRepo.save(banner);

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            if (bannerId != null) {
                response.setResultMsg("Banner updated successfully");
            } else {
                response.setResultMsg("Banner saved Successfully");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Banner saving failed");
            LOGGER.info("======== END ProductServiceImpl.saveNewBanner() failed due to : " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public BaseResponse deleteBanner(String bannerId) throws JsonProcessingException {
        BaseResponse response = new BaseResponse();
        LOGGER.info("======== START ProductServiceImpl.deleteBanner() with bannerId : " + bannerId);

        try {
            //delete banner
            bannerRepo.deleteById(Long.valueOf(bannerId));

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully deleted");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Banner failed to be deleted");
            LOGGER.info("======== END ProductServiceImpl.deleteBanner() failed due to : " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    /*
	 * PRODUCT/GOODS PORTION
     */
    @Override
    public ProductListDTOResponse getAllProducts() throws Exception {
        ProductListDTOResponse response = new ProductListDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.getAllProducts()");
        List<Product> productList = productRepo.findAvailableProducts();

        if (productList != null && !productList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<ProductDTOResponse> productResList = new ArrayList<ProductDTOResponse>();
            for (Product product : productList) {
                ProductDTOResponse productRes = new ProductDTOResponse();
                productRes.setProductId(product.getId());
                productRes.setGoodsId(product.getGoodsId());
                productRes.setProductName(product.getProductName());
                productRes.setProductDesc(product.getProductDesc());
                productRes.setPrice(product.getPrice());

                //Set discount
                DiscountDTOResponse discRes = this.getProductDiscount(product.getGoodsId());
                if (discRes != null && discRes.getResultCode().equals("200")) {
                    productRes.setDiscount(String.valueOf(discRes.getDiscount()).concat("%"));
                } else {
                    productRes.setDiscount("0");
                }

                productRes.setIsActive(product.getIsActive());

                List<ProductImages> productImageList = productImagesRepo.findByProductId(product.getId()); //get product image list first based on the id
                if (!productImageList.isEmpty()) {
                    for (ProductImages prodImg : productImageList) {
                        if (prodImg.getIsMain() != null && "Y".equals(prodImg.getIsMain())) {
                            productRes.setProductImagePath(prodImg.getImagePath());
                        } else {
                            productRes.setProductImagePath(productImageList.get(0).getImagePath());
                        }
                    }
                } else {
                    productRes.setProductImagePath(product.getGoodsUrl()); //use default image from Goods URL field
                }

                productResList.add(productRes);
            }
            response.setProductList(productResList);
            LOGGER.info("======== END ProductServiceImpl.getAllProducts() - Product response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("There are no any products available right now");
            LOGGER.info("======== END ProductServiceImpl.getAllProducts() - No products available");
        }
        return response;
    }

    @Override
    public ProductDetailDTOResponse getProductById(Long productId) throws Exception {
        ProductDetailDTOResponse response = new ProductDetailDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.getProductById()");
        Optional<Product> product = productRepo.findById(productId);

        if (product != null) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            response.setProductId(product.get().getId());
            response.setGoodsId(product.get().getGoodsId());
            response.setProductName(product.get().getProductName());
            response.setProductDesc(product.get().getProductDesc());
            response.setPrice(product.get().getPrice());

            //Set discount
            DiscountDTOResponse discRes = this.getProductDiscount(product.get().getGoodsId());
            if (discRes != null && discRes.getResultCode().equals("200")) {
                BigDecimal oriAmount = product.get().getPrice();
                BigDecimal discInPercent = new BigDecimal(discRes.getDiscount());
                BigDecimal discAmount = (oriAmount.multiply(discInPercent)).divide(new BigDecimal(100));
                response.setDiscount(discAmount);
            } else {
                response.setDiscount(new BigDecimal(0));
            }

            response.setIsActive(product.get().getIsActive());

            List<ProductImageDTOResponse> productImageResList = new ArrayList<ProductImageDTOResponse>();
            List<ProductImages> productImageList = productImagesRepo.findByProductId(product.get().getId()); //get product image list first based on the id
            if (productImageList != null && !productImageList.isEmpty()) {
                for (ProductImages prodImages : productImageList) {
                    ProductImageDTOResponse productImageRes = new ProductImageDTOResponse();
                    productImageRes.setProductId(prodImages.getProductId());
                    productImageRes.setImagePath(prodImages.getImagePath());
                    productImageRes.setIsMain(prodImages.getIsMain());
                    productImageResList.add(productImageRes);
                }
            } else { //just for dummy to set images, in order to not be null
                ProductImageDTOResponse productImageRes = new ProductImageDTOResponse();
                productImageRes.setProductId(response.getProductId());
                productImageRes.setImagePath("http://45.130.229.168/appresources/images/products/01_Long_Black.jpg");
                productImageRes.setIsMain("Y");
                productImageResList.add(productImageRes);
            }
            response.setProductImageList(productImageResList);
            LOGGER.info("======== END ProductServiceImpl.getProductById() - Product response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("Product not found");
            LOGGER.info("======== END ProductServiceImpl.getProductById() - Product not found");
        }
        return response;
    }

    @Override
    public BaseResponse saveProduct(Long productId, ProductDTORequest request) throws JsonProcessingException {
        BaseResponse response = new BaseResponse();
        LOGGER.info("======== START ProductServiceImpl.saveNewProduct() with request : " + mapper.writeValueAsString(request));

        try {
            //save or update product
            Product product = new Product();
            if (productId != null) { //means this is update
                product.setId(productId);
            }
            product.setGoodsId(request.getGoodsId());
            product.setProductName(request.getProductName());
            product.setProductDesc(request.getProductDesc());
            product.setPrice(request.getPrice());
            product.setIsActive(request.getIsActive());
            productRepo.save(product);
            //note: product image need to be saved with separated API

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            if (productId != null) {
                response.setResultMsg("Product updated successfully");
            } else {
                response.setResultMsg("Product saved successfully");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Product saving failed");
            LOGGER.info("======== END ProductServiceImpl.saveNewProduct() failed due to : " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public BaseResponse deleteProduct(String productId) throws JsonProcessingException {
        BaseResponse response = new BaseResponse();
        LOGGER.info("======== START ProductServiceImpl.deleteProduct() with productId : " + productId);

        try {
            //delete banner
            productRepo.deleteById(Long.valueOf(productId));

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Product is successfully deleted");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Product failed to be deleted");
            LOGGER.info("======== END ProductServiceImpl.deleteProduct() failed due to : " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public BaseResponse syncMiniInformation() throws JsonProcessingException {
        BaseResponse response = new BaseResponse();
        HWProductDTOResponse productResponse = new HWProductDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.syncMiniInformation()");

        try {
            /* Define URL & headers */
            String hwApiBaseUrl2 = crudService.getGlobalConfigParamByKey(GlobalConstants.CLIENT_URL2);
            String operateCode = crudService.getGlobalConfigParamByKey(GlobalConstants.OPERATE_CODE);
            String url = hwApiBaseUrl2.concat("/miniInfo?operateCode=").concat(operateCode);

            /* Set header */
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            /* Build URI from URL */
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

            /* Call HW Service */
            ResponseEntity<HWProductDTOResponse> responseEntity;
            responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, HWProductDTOResponse.class);
            productResponse = responseEntity.getBody();
            LOGGER.info("======== RESPONSE ProductServiceImpl.syncMiniInformation() - HWProductDTOResponse: " + mapper.writeValueAsString(productResponse));

            /* Updating response from HW to Product table */
            //perlu clear table dlu, jadi selalu baru
            if (productResponse != null) {
                for (CoffeeDTOResponse coffeeRes : productResponse.getCoffee()) {
                    //Determine whether it should save as new record or update
                    //If a product is found in DB, then the ID will be set in order to enable update instead of save as new record
                    Product product = productRepo.findByGoodsIdAndGoodsProtocol(coffeeRes.getGoodsId(), coffeeRes.getGoodsProtocol(), coffeeRes.getModelId());
                    Boolean isNewProduct = false;
                    if (product == null) {
                        product = new Product();
                        product.setIsActive("N");
                        isNewProduct = true;
                    }

                    product.setOperatorId(coffeeRes.getOperatorId());
                    product.setModelId(coffeeRes.getModelId());
                    product.setGoodsId(coffeeRes.getGoodsId());
                    product.setProductName(coffeeRes.getGoodsName());

                    /* DONT NEED TO UPDATE IMAGE URL, SINCE IT WILL BE MANAGED BY WEB APP */
                    //String imageURL = crudService.getGlobalConfigParamByKey(GlobalConstants.PRODUCT_IMG_URL);
                    //product.setGoodsUrl(imageURL.concat(coffeeRes.getGoodsURL()));
                    product.setGoodsProtocol(coffeeRes.getGoodsProtocol());
                    product.setProductDesc(coffeeRes.getRemark());
                    product.setTasteId(coffeeRes.getTasteId());
                    product.setTasteName(coffeeRes.getTasteName());
                    product.setTasteUrl(coffeeRes.getTasteURL());
                    product.setPrice(coffeeRes.getPrice());

                    if (isNewProduct) {
                        if (coffeeRes.getPrice().compareTo(BigDecimal.ZERO) == 0) {
                            product.setIsActive("N");
                        }
                    } else {
                        if (coffeeRes.getPrice().compareTo(BigDecimal.ZERO) == 0) {
                            product.setIsActive("N");
                        } else {
                            product.setIsActive("Y");
                        }
                    }
                    /*else if (coffeeRes.getTasteName() != null) {
						if (coffeeRes.getTasteName().equals("") || coffeeRes.getTasteName().contains("?")) {
							product.setIsActive("N");
						}
					}
					else if (coffeeRes.getGoodsName() != null) {
						if (coffeeRes.getGoodsName().equals("") || coffeeRes.getGoodsName().contains("?")
								|| coffeeRes.getGoodsName().toUpperCase().contains("TESTING")) {
							product.setIsActive("N");
						}
					}*/

                    if (product.getGoodsProtocol() != 0) {
                        //only save/update product that has taste name (goods protocol is not 0)
                        productRepo.save(product);
                    }
                }
                for (MachineInfoDTOResponse machineRes : productResponse.getMachineInfo()) {
                    //Determine whether it should save as new record or update
                    //If a machine is found in DB, then the ID will be set in order to enable update instead of save as new record
                    Machine machine = machineRepo.findByMachineCode(machineRes.getMachineCode());
                    if (machine == null) {
                        machine = new Machine();
                    }

                    machine.setMachineCode(machineRes.getMachineCode());
                    machine.setLocation(machineRes.getAddress());
                    machine.setLatitude(machineRes.getLatitude());
                    machine.setLongitude(machineRes.getLongitude());
                    if (machine.getDescription() == null) {
                        machine.setDescription("");
                    }
                    machine.setOperate1(machineRes.getOperate1());
                    machine.setModelId(machineRes.getModelId());
                    if (machineRes.getOnlineTime() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        machine.setOnlineTime(sdf.parse(machineRes.getOnlineTime()));
                    }
                    machine.setOnline(machineRes.getOnline());
                    machine.setStatus(machineRes.getStatus());
                    //machine.setUserId(0); //ini perlu diset lewat backoffice
                    if (machine.getChannelType() == null) {
                        machine.setChannelType("xendit");
                    }

                    machineRepo.save(machine);
                }
            }

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("All products & machines are successfully syncronized");

            LOGGER.info("======== END ProductServiceImpl.syncMiniInformation() - Product sync success!!!");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Product sync failed");
            LOGGER.info("======== END ProductServiceImpl.syncMiniInformation() : " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public TasteListDTOResponse getTasteByProductId(int goodsId) throws Exception {
        TasteListDTOResponse response = new TasteListDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.getTasteProductId()");
        List<Product> productList = productRepo.findTasteListByGoodsId(goodsId);

        if (productList != null && !productList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<TasteDTOResponse> tasteResList = new ArrayList<TasteDTOResponse>();
            for (Product product : productList) {
                TasteDTOResponse tasteRes = new TasteDTOResponse();
                tasteRes.setTasteId(product.getTasteId());
                tasteRes.setTasteName(product.getTasteName());
                tasteRes.setGoodsProtocol(product.getGoodsProtocol());
                tasteResList.add(tasteRes);
            }
            response.setTasteList(tasteResList);
            LOGGER.info("======== END ProductServiceImpl.getTasteProductId() - Taste response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("The product doesn't has taste");
            LOGGER.info("======== END ProductServiceImpl.getTasteProductId() - The product doesn't has taste");
        }
        return response;
    }

    /* 
	 * PRODUCT IMAGE PORTION
     */
    @Override
    public BaseResponse saveNewProductImage(ProductImagesDTORequest request) throws JsonProcessingException {
        BaseResponse response = new BaseResponse();
        LOGGER.info("======== START ProductServiceImpl.saveNewProductImage() with request : " + mapper.writeValueAsString(request));

        try {
            //save product
            ProductImages productImg = new ProductImages();
            productImg.setProductId(request.getProductId());
            productImg.setImagePath(request.getImagePath());
            productImg.setIsMain(request.getIsMain());
            productImagesRepo.save(productImg);

            //response
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully saved");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResultCode("400");
            response.setResultMsg("Product image saving failed");
            LOGGER.info("======== END ProductServiceImpl.saveNewProductImage() failed due to : " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    /* 
	 * PRODUCT MACHINE MAPPING
     */
    @Override
    public MachineDetailListDTOResponse getMachineLocationByProductId(Long productId) throws Exception {
        MachineDetailListDTOResponse response = new MachineDetailListDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.getMachineLocationByProductId() with productId : " + productId);
        List<Machine> machineList = productRepo.findMachineDetailByProductId(productId);

        if (machineList != null && !machineList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<MachineDetailDTOResponse> machineResList = new ArrayList<MachineDetailDTOResponse>();
            for (Machine machine : machineList) {
                MachineDetailDTOResponse machineRes = new MachineDetailDTOResponse();
                machineRes.setId(machine.getId());
                machineRes.setMachineCode(machine.getMachineCode());
                machineRes.setLocation(machine.getLocation());
                machineRes.setDescription(machine.getDescription());
                machineRes.setUserId(machine.getUserId());
                machineRes.setChannelType(machine.getChannelType());

                machineResList.add(machineRes);
            }
            response.setMachineList(machineResList);
            LOGGER.info("======== END ProductServiceImpl.getMachineLocationByProductId() - Machine response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("There are no any machines available right now");
            LOGGER.info("======== END ProductServiceImpl.getMachineLocationByProductId() - No machines available");
        }
        return response;
    }
    
    @Override
    public MachineDetailListDTOResponse getMachineLocationByStatus() throws Exception {
        MachineDetailListDTOResponse response = new MachineDetailListDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.getMachineLocationByProductId() with status : Y");
        List<Machine> machineList = machineRepo.findMachineByStatus();

        if (machineList != null && !machineList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<MachineDetailDTOResponse> machineResList = new ArrayList<MachineDetailDTOResponse>();
            for (Machine machine : machineList) {
                MachineDetailDTOResponse machineRes = new MachineDetailDTOResponse();
                machineRes.setId(machine.getId());
                machineRes.setMachineCode(machine.getMachineCode());
                machineRes.setLocation(machine.getLocation());
                machineRes.setDescription(machine.getDescription());
                machineRes.setUserId(machine.getUserId());
                machineRes.setChannelType(machine.getChannelType());

                machineResList.add(machineRes);
            }
            response.setMachineList(machineResList);
            LOGGER.info("======== END ProductServiceImpl.getMachineLocationByProductId() - Machine response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("There are no any machines available right now");
            LOGGER.info("======== END ProductServiceImpl.getMachineLocationByProductId() - No machines available");
        }
        return response;
    }

    @Override
    public MachineDetailListDTOResponse getActiveMachineList() throws Exception {
        MachineDetailListDTOResponse response = new MachineDetailListDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.getActiveMachineList()");
        List<Machine> machineList = machineRepo.findMachineByStatus();

        if (machineList != null && !machineList.isEmpty()) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            List<MachineDetailDTOResponse> machineResList = new ArrayList<MachineDetailDTOResponse>();
            for (Machine machine : machineList) {
                MachineDetailDTOResponse machineRes = new MachineDetailDTOResponse();
                machineRes.setId(machine.getId());
                machineRes.setMachineCode(machine.getMachineCode());
                machineRes.setLatitude(machine.getLatitude());
                machineRes.setLongitude(machine.getLongitude());
                machineRes.setLocation(machine.getLocation());
                machineRes.setDescription(machine.getDescription());
                machineRes.setOperate1(machine.getOperate1());
                machineRes.setModelId(machine.getModelId());
                if (machine.getOnlineTime() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    machineRes.setOnlineTime(sdf.format(machine.getOnlineTime()));
                }
                machineRes.setOnline(machine.getOnline());
                machineRes.setStatus(machine.getStatus());
                machineRes.setUserId(machine.getUserId());
                machineRes.setChannelType(machine.getChannelType());

                machineResList.add(machineRes);
            }
            response.setMachineList(machineResList);
            LOGGER.info("======== END ProductServiceImpl.getActiveMachineList() - Machine response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("There are no any machines available right now");
            LOGGER.info("======== END ProductServiceImpl.getActiveMachineList() - No machines available");
        }
        return response;
    }

    @Override
    public DiscountDTOResponse getProductDiscount(int goodsId) throws Exception {
        DiscountDTOResponse response = new DiscountDTOResponse();
        LOGGER.info("======== START ProductServiceImpl.getProductDiscount()");
        Discount discount = discountRepo.findByGoodsId(goodsId);

        if (discount != null) {
            response.setSuccess(true);
            response.setResultCode("200");
            response.setResultMsg("Successfully retrieved");

            response.setId(discount.getId());
            response.setGoodsId(discount.getGoodsId());
            response.setDiscount(discount.getDiscount());
            LOGGER.info("======== END ProductServiceImpl.getProductDiscount() - Discount response : " + mapper.writeValueAsString(response));
        } else {
            response.setSuccess(false);
            response.setResultCode("404");
            response.setResultMsg("No discount for this product");
            LOGGER.info("======== END ProductServiceImpl.getProductDiscount() - No discount found for this product");
        }
        return response;
    }
}
