package br.ufma.lsdi.inserscity.resourceadaptor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResourceAdaptorService {

	public ResourceDto registerNewResource(ResourceDto resource) {
		String url = "http://cidadesinteligentes.lsdi.ufma.br/adaptor/resources";
		RestTemplate rest = new RestTemplate();
		
		ResponseEntity<ResourceDto> response = rest.postForEntity(url, resource, ResourceDto.class);
		if(response.getStatusCode().value() != 201) 
			return null;
		
		return response.getBody();
	}
}