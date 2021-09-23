package br.ufma.lsdi.smartbins;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmartBinsLevelEntityRepository extends JpaRepository<SmartBinsLevelEntity, Long> {
	
	public SmartBinsLevelEntity findByBinId(String binId);
}
