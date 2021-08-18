/**
 *
 */
package com.heiya.mobileapi.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.customer.model.Customer;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findByMobileNo(String mobileNo);

    public Customer findByEmail(String email);

    @Query(value = "SELECT c FROM Customer c WHERE c.mobileNo = (:mobileNo)")
    public Customer getCustomerPhoneAndPassword(@Param("mobileNo") String mobileNo);
}
