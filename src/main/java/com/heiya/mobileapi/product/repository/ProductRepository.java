/**
 * 
 */
package com.heiya.mobileapi.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.product.model.Machine;
import com.heiya.mobileapi.product.model.Product;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query(value = "SELECT m "
				 + "FROM ProductMachineMapping pm "
				 + "LEFT JOIN Product p ON pm.productId = p.id "
				 + "LEFT JOIN Machine m ON pm.machineId = m.id "
				 + "WHERE p.id = (:productId)")
	public List<Machine> findMachineDetailByProductId(@Param("productId") Long productId);
	
	@Query(value = "SELECT p FROM Product p WHERE p.goodsId = (:goodsId) AND p.goodsProtocol = (:goodsProtocol) AND p.modelId = (:modelId)")
	public Product findByGoodsIdAndGoodsProtocol(@Param("goodsId") int goodsId, @Param("goodsProtocol") int goodsProtocol, @Param("modelId") int modelId);
	
	@Query(value = "SELECT p FROM Product p WHERE p.isActive = 'Y' GROUP BY p.goodsId ORDER BY p.productName, p.tasteId")
	public List<Product> findAvailableProducts();
	
	@Query(value = "SELECT p FROM Product p WHERE p.isActive = 'Y' AND p.goodsId = (:goodsId) ORDER BY p.tasteId")
	public List<Product> findTasteListByGoodsId(@Param("goodsId") int goodsId);
	
	public Product findByGoodsId(int goodsId);
        
}
