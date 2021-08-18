/**
 * 
 */
package com.heiya.mobileapi.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.payment.model.PaymentProvider;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface PaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {
	
}
