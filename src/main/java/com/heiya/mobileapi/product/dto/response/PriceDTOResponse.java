/**
 *
 */
package com.heiya.mobileapi.product.dto.response;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dian Krisnanjaya
 *
 */
//@Setter
//@Getter
public interface PriceDTOResponse {
    
    Long getId();
    
    Integer getGoodsId();
    
    String getProductName();
    
    String getGoodsUrl();
    
    String getProductDesc();

    BigDecimal getPriceMin();
    
    BigDecimal getPriceMax();
    
    String getModelId();

}
