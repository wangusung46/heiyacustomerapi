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
@Table(name="product_machine_mapping")
public class ProductMachineMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="product_id")
	private String productId;
	
	@Column(name="machine_id")
	private String machineId;
}
