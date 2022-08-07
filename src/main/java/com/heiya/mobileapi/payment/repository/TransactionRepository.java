/**
 * 
 */
package com.heiya.mobileapi.payment.repository;

import java.util.Date;
import java.util.List;

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
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	@Query(value = "SELECT p FROM Transaction p WHERE p.paymentStatus = (:paymentStatus) and p.transactionTouchpoint = 'mobile' and p.channelTransactionTime >= (:channelTransactionTime)")
	public List<Transaction> findPaymentListByStatusAndTime(@Param("paymentStatus") String paymentStatus, 
			@Param("channelTransactionTime") Date channelTransactionTime);
	
	@Query(value = "SELECT p FROM Transaction p WHERE p.orderNo = (:orderNo)")
	public Transaction findPaymentByOrderNo(@Param("orderNo") String orderNo);
	
	@Query(value = "SELECT p FROM Transaction p WHERE p.customerId = (:customerId) AND p.paymentStatus IN ('completed','settlement','pickup_expired') ORDER BY p.channelTransactionTime DESC")
	public List<Transaction> findOrderList(@Param("customerId") long customerId);
	
	@Query(value = "SELECT p FROM Transaction p WHERE p.customerId = (:customerId) AND p.paymentStatus = (:status) ORDER BY p.channelTransactionTime DESC")
	public List<Transaction> findOrderListByStatus(@Param("customerId") long customerId, @Param("status") String status);
	
	@Query(value = "SELECT p FROM Transaction p WHERE p.paymentStatus = (:status) AND p.transactionTouchpoint = (:touchpoint)")
	public List<Transaction> findPaymentByStatusAndTouchpoint(@Param("status") String status, @Param("touchpoint") String touchpoint);
        
        @Query(value = "SELECT p FROM Transaction p WHERE p.channelTransactionTime >= (:channelTransactionTime) and p.transactionTouchpoint = 'mobile'")
	public List<Transaction> findPaymentByTime(@Param("channelTransactionTime") Date channelTransactionTime);
        
        @Query(value = "SELECT p FROM Transaction p LEFT JOIN Machine m ON p.machineCode = m.machineCode WHERE m.ap2 = TRUE and p.paymentStatus = (:paymentStatus) and p.settlementTime >= (:channelTransactionTime) and (p.ap2 = (:ap2) or p.ap2 = null)")
	public List<Transaction> findPaymentListByStatusAndAP2(@Param("paymentStatus") String paymentStatus, @Param("channelTransactionTime") Date channelTransactionTime, @Param("ap2") Boolean ap2);
}
