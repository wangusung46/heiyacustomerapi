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
@Table(name="product_images")
public class ProductImages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="product_id")
	private long productId;
	
	@Column(name="image_path")
	private String imagePath;
	
	@Column(name="is_main")
	private String isMain;
}
