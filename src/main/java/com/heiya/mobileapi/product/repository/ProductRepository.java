/**
 *
 */
package com.heiya.mobileapi.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.product.model.Product;
import java.util.Optional;
import com.heiya.mobileapi.product.dto.response.PriceDTOResponse;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p WHERE p.goodsId = (:goodsId) AND p.goodsProtocol = (:goodsProtocol) AND p.modelId = (:modelId)")
    public Product findByGoodsIdAndGoodsProtocol(@Param("goodsId") int goodsId, @Param("goodsProtocol") int goodsProtocol, @Param("modelId") int modelId);

    @Query(value = "SELECT p FROM Product p WHERE p.isActive = 'Y' GROUP BY p.goodsId ORDER BY p.productName, p.tasteId")
    public List<Product> findAvailableProducts();

    @Query(value = "SELECT p.id AS id, p.goodsId AS goodsId, p.productName AS productName, p.goodsUrl AS goodsUrl, p.productDesc AS productDesc, MIN(p.price) AS priceMin, MAX(p.price) AS priceMax, p.modelId AS modelId FROM Product p WHERE p.isActive = 'Y' GROUP BY p.goodsId")
    public List<PriceDTOResponse> findAvailableProductsv2();

    @Query(value = "SELECT p FROM Product p WHERE p.isActive = 'Y' AND p.goodsId = (:goodsId) ORDER BY p.tasteId")
    public List<Product> findTasteListByGoodsId(@Param("goodsId") int goodsId);

    @Query(value = "SELECT p "
            + "FROM Product p "
            + "LEFT JOIN Machine m "
            + "ON p.modelId = m.modelId "
            + "WHERE p.isActive = 'Y' "
            + "AND m.id = (:machineId) AND p.goodsId = (:goodsId)")
    public List<Product> findTasteListByGoodsIdv2(@Param("goodsId") Integer goodsId, @Param("machineId") Long machineId);

    public Product findByGoodsId(int goodsId);

    @Query(value = "SELECT p "
            + "FROM Product p "
            + "LEFT JOIN Machine m "
            + "ON p.modelId = m.modelId "
            + "WHERE p.isActive = 'Y' "
            + "AND m.id = (:machineId) AND p.goodsId = (:goodsId) GROUP BY p.goodsId")
    public Optional<Product> findByGoodsIdAndIdMachine(@Param("goodsId") Integer goodsId, @Param("machineId") Long machineId);

    @Query(value = "SELECT p "
            + "FROM Product p "
            + "LEFT JOIN Machine m "
            + "ON p.modelId = m.modelId "
            + "WHERE p.isActive = 'Y' "
            + "AND m.id = (:machineId) GROUP BY p.goodsId")
    public List<Product> findByIdMachine(@Param("machineId") Long machineId);

}
