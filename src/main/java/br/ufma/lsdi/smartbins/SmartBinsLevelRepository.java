package br.ufma.lsdi.smartbins;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmartBinsLevelRepository extends JpaRepository<SmartBinsLevelEntity, Long> {

}
