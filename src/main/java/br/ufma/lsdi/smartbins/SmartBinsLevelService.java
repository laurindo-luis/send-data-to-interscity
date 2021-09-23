package br.ufma.lsdi.smartbins;

import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.ufma.lsdi.inserscity.resourceadaptor.ResourceAdaptorService;
import br.ufma.lsdi.inserscity.resourceadaptor.ResourceDto;

@Service
public class SmartBinsLevelService {
	
	@Autowired
	private ResourceAdaptorService resourceAdaptorService;
	
	@Autowired
	private SmartBinsLevelEntityRepository smartBinsLevelEntityRepository;
	
	public List<SmartBinsLevelField> getSmartBinsLevel() {
		RestTemplate rest = new RestTemplate();
		ResponseEntity<SmartBinsLevelDto> response = rest.getForEntity("https://data.randwick.nsw.gov.au/api/"
				+ "records/1.0/search/?dataset=smart-bins-current-level&q=&rows=100&sort=timestamp&"
				+ "timezone=America/Argentina/Buenos_Aires&facet=bin_id&facet=bin_status", 
				SmartBinsLevelDto.class);
		
		SmartBinsLevelDto smartBinsLevelDto = response.getBody();
		return smartBinsLevelDto.getRecords().stream()
				.map(record -> SmartBinsLevelField.create(record.getFields()))
				.collect(Collectors.toList());
	}
	
	public void sendDataToInterSCity() {
		List<SmartBinsLevelField> smartBinsLevelFields = getSmartBinsLevel();
		smartBinsLevelFields.forEach(smartBin -> {
			
			if(nonNull(smartBin.getLatLong())) {
				SmartBinsLevelEntity smartBinsLevelEntity = findByBinId(smartBin.getBinId());
				if(isNull(smartBinsLevelEntity)) {
					
					List<String> capabilities = Arrays.asList("current_fill_level", "battery_health");
					String[] latLon = smartBin.getLatLong().split(",");
					Double lat = Double.valueOf(latLon[0]);
					Double lon = Double.valueOf(latLon[1]);
					
					ResourceDto resource = new ResourceDto("A public smart bin", capabilities, "active", 
							lat, lon);
					ResourceDto response = resourceAdaptorService.registerNewResource(resource);	
					
					//Salvar agora no mysql a referência do recurso com Id do InterSCity
					smartBinsLevelEntity = new SmartBinsLevelEntity();
					smartBinsLevelEntity.setUuid(response.getData().getUuid());
					smartBinsLevelEntity.setBinId(smartBin.getBinId());
					save(smartBinsLevelEntity);
				
				}
			}
			
		});
	}
	
	public void save(SmartBinsLevelEntity smartBinsLevelEntity) {
		smartBinsLevelEntityRepository.save(smartBinsLevelEntity);
	}
	
	public SmartBinsLevelEntity findByBinId(String binId) {
		return smartBinsLevelEntityRepository.findByBinId(binId);
	}
	
}
