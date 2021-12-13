package br.ufma.lsdi.smartbins;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.ufma.lsdi.inserscity.resource.ResourceAdaptorService;
import br.ufma.lsdi.inserscity.resource.ResourceDto;

@Service
public class SmartBinsLevelService {
	
	@Autowired
	private ResourceAdaptorService resourceAdaptorService;
	
	@Autowired
	private SmartBinsLevelEntityRepository smartBinsLevelEntityRepository;
	
	public List<SmartBinLevelField> getSmartBinsLevel() {
		/*
		RestTemplate rest = new RestTemplate();
		ResponseEntity<SmartBinsLevelDto> response = rest.getForEntity("https://data.randwick.nsw.gov.au/api/"
				+ "records/1.0/search/?dataset=smart-bins-current-level&q=&rows=100&sort=timestamp&"
				+ "timezone=America/Argentina/Buenos_Aires&facet=bin_id&facet=bin_status", 
				SmartBinsLevelDto.class);
		
		SmartBinsLevelDto smartBinsLevelDto = response.getBody();
		return smartBinsLevelDto.getRecords().stream()
				.map(record -> SmartBinLevelField.create(record.getFields()))
				.collect(Collectors.toList()); */
				
		return getSmartBinsLevelCSV();
	}
	
	private List<SmartBinLevelField> getSmartBinsLevelCSV() {
		List<SmartBinLevelField> smartBinsLevel = new ArrayList<>();
		try {
			FileReader file = new FileReader("smart_bins.csv");
			BufferedReader bufferReader = new BufferedReader(file);
			
			String line = bufferReader.readLine();
			while(nonNull(line = bufferReader.readLine())) {
				String[] values = line.split(";");
				
				SmartBinLevelField smartBinsLevelField = new SmartBinLevelField();
				smartBinsLevelField.setBatteryHealth(Double.valueOf(values[0]));
				smartBinsLevelField.setBinId(values[1]);
				smartBinsLevelField.setBinStatus(values[2]);
				smartBinsLevelField.setCurrentFillLevel(Double.valueOf(values[3]));
				smartBinsLevelField.setLatLong(values[4]);
				smartBinsLevelField.setTimesTamp(dateFormartTime(values[5]));
				
				smartBinsLevel.add(smartBinsLevelField);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return smartBinsLevel;
	}
	
	public Boolean sendDataToInterSCity() {	
		Boolean flag = false;
		List<SmartBinLevelField> smartBinsLevelFields = getSmartBinsLevel();
		for(SmartBinLevelField smartBin : smartBinsLevelFields) {
			if(nonNull(smartBin.getLatLong())) {
				if(!isRegisteredInterSCity(smartBin)) {
					registerResourceInterSCity(smartBin);					
				}
				
				String uuid = getUuidSmartBin(smartBin.getBinId());
				if(nonNull(uuid))
					flag = saveContextData(uuid, smartBin);
			}
		}
		return flag;
	}
	
	private Boolean saveContextData(String uuid, SmartBinLevelField smartBin) {		
		//Cadastrando dados de contexto
		ResourceDto resource = new ResourceDto.Builder()
				.addContextData(
						smartBin.getCurrentFillLevel(), 
						smartBin.getBatteryHealth(),
						dateFormatTime(smartBin.getTimesTamp())
				).build();
		Boolean status = resourceAdaptorService.saveContextData(uuid, resource);
		if(!status) 
			return false;
		return true;
	}
	
	private String getUuidSmartBin(String binId) {
		SmartBinsLevelEntity smartBinsLevelEntity = findByBinId(binId);
		return isNull(smartBinsLevelEntity) ? null : smartBinsLevelEntity.getUuid();
	}
	
	private ResourceDto registerResourceInterSCity(SmartBinLevelField smartBin) {
		Double[] latLon = getLatLon(smartBin);
		ResourceDto resource = new ResourceDto.Builder()
				.setDescription("A public smart bin")
				.addCapabilite("current_fill_level")
				.addCapabilite("battery_health")
				.setStatus("active")
				.setLatLon(latLon[0], latLon[1])
				.build();
		
		ResourceDto response = resourceAdaptorService.registerNewResource(resource);
		if(nonNull(response))
			//Salvar agora no mysql a referência do recurso com Id do InterSCity
			saveSmartBinReference(response.getData().getUuid(), smartBin.getBinId());
		return response;
	}
	
	private void saveSmartBinReference(String uuidResource, String idSmartBin) {
		SmartBinsLevelEntity smartBinsLevelEntity = new SmartBinsLevelEntity();
		smartBinsLevelEntity.setUuid(uuidResource);
		smartBinsLevelEntity.setBinId(idSmartBin);
		save(smartBinsLevelEntity);
	}
	
	private Double[] getLatLon(SmartBinLevelField smartBin) {
		String[] values = smartBin.getLatLong().split(",");
	
		Double lat = Double.valueOf(values[0].trim());
		Double lon = Double.valueOf(values[1].trim());
		
		Double[] latLon = {lat, lon};
		return latLon;
	}
	
	private Boolean isRegisteredInterSCity(SmartBinLevelField smartBin) {
		SmartBinsLevelEntity smartBinsLevelEntity = findByBinId(smartBin.getBinId());
		return nonNull(smartBinsLevelEntity) ? true :  false;
	}
	
	public void save(SmartBinsLevelEntity smartBinsLevelEntity) {
		smartBinsLevelEntityRepository.save(smartBinsLevelEntity);
	}
	
	public SmartBinsLevelEntity findByBinId(String binId) {
		return smartBinsLevelEntityRepository.findByBinId(binId);
	}
	
	public Boolean isHasBinsSaved() {
		return smartBinsLevelEntityRepository.findAll().size() > 0 ? true : false;
	}
	
	private String dateFormatTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String data = format.format(date);
		return data.replace(" ", "T");
	}
	
	private Date dateFormartTime(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
