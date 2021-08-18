/**
 * 
 */
package com.heiya.mobileapi.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.payment.model.StoreTransactionDetail;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface StoreTransactionDetailRepository extends JpaRepository<StoreTransactionDetail, Long> {
	
	public StoreTransactionDetail findByOrderNo(String orderNo);
}
