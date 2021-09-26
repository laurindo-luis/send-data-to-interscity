package br.ufma.lsdi.inserscity.resource.adaptor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.ufma.lsdi.inserscity.resource.ResourceDto;

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
	
	public Boolean saveContextData(String uuid, ResourceDto resource) {
		String url = String.format("http://cidadesinteligentes.lsdi.ufma.br/adaptor/resources"
				+ "/%s/data", uuid);
		RestTemplate rest = new RestTemplate();
		ResponseEntity<Void> response = rest.postForEntity(url, resource, Void.class);
		return response.getStatusCode().value() == 201 ? true : false;
	}
}