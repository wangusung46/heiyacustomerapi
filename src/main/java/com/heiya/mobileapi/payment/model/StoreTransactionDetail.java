/**
 * 
 */
package com.heiya.mobileapi.payment.model;

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
@Table(name="store_transaction_detail")
public class StoreTransactionDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="order_no")
	private String orderNo;
	
	@Column(name="taste_id")
	private String tasteId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="good_id")
	private String goodId;
	
	@Column(name="order_goods_no")
	private String orderGoodsNo;
	
	@Column(name="brewing_code")
	private String brewingCode;
	
	@Column(name="order_price")
	private BigDecimal orderPrice;
	
}
