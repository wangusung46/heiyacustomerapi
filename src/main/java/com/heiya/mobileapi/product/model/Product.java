/**
 * 
 */
package com.heiya.mobileapi.product.model;

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
@Setter
@Getter
@Entity
@Table(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="operator_id")
	private String operatorId;
	
	@Column(name="model_id")
	private int modelId;
	
	@Column(name="goods_id")
	private int goodsId;
	
	@Column(name="product_name")
	private String productName;
	
	@Column(name="goods_url")
	private String goodsUrl;
	
	@Column(name="goods_protocol")
	private int goodsProtocol;
	
	@Column(name="product_desc")
	private String productDesc;
	
	@Column(name="taste_id")
	private int tasteId;
	
	@Column(name="taste_name")
	private String tasteName;
	
	@Column(name="taste_url")
	private String tasteUrl;
	
	@Column(name="price")
	private BigDecimal price;
	
	/*@Column(name="discount")
	private int discount;*/
	
	@Column(name="is_active")
	private String isActive;
}
