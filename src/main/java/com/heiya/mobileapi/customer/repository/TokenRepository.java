/**
 *
 */
package com.heiya.mobileapi.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.customer.model.Customer;
import com.heiya.mobileapi.customer.model.Token;
import java.util.List;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    public Token findByIdTokenAndIdCustomer(String idToken, Long idCustomer);
    
    public List<Token> findByIdCustomer(Long idCustomer);

//    public Customer findByEmail(String email);
//
//    @Query(value = "SELECT c FROM Customer c WHERE c.mobileNo = (:mobileNo)")
//    public Customer getCustomerPhoneAndPassword(@Param("mobileNo") String mobileNo);
}
