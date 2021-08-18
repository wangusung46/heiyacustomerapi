/**
 * 
 */
package com.heiya.mobileapi.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.heiya.mobileapi.database.model.GlobalConfigs;

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface GlobalConfigsRepository extends JpaRepository<GlobalConfigs, Long> {
	
	@Query(value = "SELECT g FROM GlobalConfigs g WHERE g.configKey = (:configKey)")
	public GlobalConfigs findParamByKey(@Param("configKey") String configKey);
	
}
