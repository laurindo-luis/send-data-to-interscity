package br.ufma.lsdi.smartbins;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.ufma.lsdi.inserscity.resource.ResourceDto;
import br.ufma.lsdi.inserscity.resource.adaptor.ResourceAdaptorService;

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
					
					String[] latLon = smartBin.getLatLong().split(",");
					Double lat = Double.valueOf(latLon[0]);
					Double lon = Double.valueOf(latLon[1]);
					
					ResourceDto resource = new ResourceDto.Builder()
							.setDescription("A public smart bin")
							.addCapabilite("current_fill_level")
							.addCapabilite("battery_health")
							.setStatus("active")
							.setLatLon(lat, lon)
							.build();
					
					ResourceDto response = resourceAdaptorService.registerNewResource(resource);	
					if(nonNull(response)) {
						//Salvar agora no mysql a referência do recurso com Id do InterSCity
						smartBinsLevelEntity = new SmartBinsLevelEntity();
						smartBinsLevelEntity.setUuid(response.getData().getUuid());
						smartBinsLevelEntity.setBinId(smartBin.getBinId());
						save(smartBinsLevelEntity);
					}
				}
				
				if(nonNull(smartBinsLevelEntity)) {
					//Cadastrando dados de contexto
					ResourceDto resource = new ResourceDto.Builder()
							.addContextData(
									smartBin.getCurrentFillLevel(), 
									smartBin.getBatteryHealth(),
									dateFormatTime(smartBin.getTimesTamp())
							).build();
					String uuid = smartBinsLevelEntity.getUuid();
					resourceAdaptorService.saveContextData(uuid, resource);
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
	
	private String dateFormatTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String data = format.format(date);
		return data.replace(" ", "T");
	}
	
}
