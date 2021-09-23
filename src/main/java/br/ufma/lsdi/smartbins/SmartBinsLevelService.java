package br.ufma.lsdi.smartbins;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmartBinsLevelService {
		
	public List<SmartBinsLevelField> getSmartBins() {
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
}
