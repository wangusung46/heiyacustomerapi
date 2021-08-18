/**
 * 
 */
package com.heiya.mobileapi.payment.model;

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
@Table(name="payment_provider")
public class PaymentProvider {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="provider_name")
	private String providerName;
	
	@Column(name="api_endpoint")
	private String apiEndpoint;
	
	@Column(name="is_active")
	private String isActive;
}
