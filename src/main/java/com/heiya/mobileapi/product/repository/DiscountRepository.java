/**
 * 
 */
package com.heiya.mobileapi.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.product.model.Discount;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
	
	public Discount findByGoodsId(int goodsId);
}
