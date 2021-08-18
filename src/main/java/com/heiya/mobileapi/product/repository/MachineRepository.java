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

/**
 * @author Dian Krisnanjaya
 *
 */
@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
	
	public Machine findByMachineCode(String machineCode);
	
	@Query(value = "SELECT m FROM Machine m WHERE m.online = 1 AND m.status = 1")
	public List<Machine> findMachineByStatus();
}
