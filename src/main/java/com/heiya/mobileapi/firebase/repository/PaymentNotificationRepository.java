/**
 * 
 */
package com.heiya.mobileapi.firebase.repository;

import com.heiya.mobileapi.firebase.model.PaymentNotification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.payment.model.Transaction;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface PaymentNotificationRepository extends JpaRepository<PaymentNotification, Long> {

	
	@Query(value = "SELECT p FROM PaymentNotification p WHERE p.orderNo = (:orderNo)")
	public PaymentNotification findPaymentNotificationByOrderNo(@Param("orderNo") String orderNo);
	
}
