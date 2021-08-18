/**
 * 
 */
package com.heiya.mobileapi.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.product.model.ProductImages;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {
	
	public List<ProductImages> findByProductId(Long productId);
}
