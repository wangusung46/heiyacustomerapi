/**
 * 
 */
package com.heiya.mobileapi.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.customer.model.Customer;
import com.heiya.mobileapi.product.model.Banner;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
	
}
