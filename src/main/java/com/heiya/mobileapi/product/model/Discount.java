/**
 * 
 */
package com.heiya.mobileapi.product.model;

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
@Table(name="discount")
public class Discount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="goods_id")
	private Integer goodsId;
	
	@Column(name="discount")
	private Integer discount; //in percent
}
