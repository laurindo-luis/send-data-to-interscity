package br.ufma.lsdi.inserscity.resource;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ResourceAdaptorService {

	public ResourceDto registerNewResource(ResourceDto resource) {
		String url = "http://cidadesinteligentes.lsdi.ufma.br/adaptor/resources";
		RestTemplate rest = new RestTemplate();
		
		try {
			ResponseEntity<ResourceDto> response = rest.postForEntity(url, resource, ResourceDto.class);
			return response.getStatusCode().value() == 201 ? response.getBody() : null;
		} catch(RestClientException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Boolean saveContextData(String uuid, ResourceDto resource) {
		String url = String.format("http://cidadesinteligentes.lsdi.ufma.br/adaptor/resources"
				+ "/%s/data", uuid);
		RestTemplate rest = new RestTemplate();
		try {
			ResponseEntity<Void> response = rest.postForEntity(url, resource, Void.class);
			return response.getStatusCode().value() == 201 ? true : false;
		} catch(RestClientException e) {
			e.printStackTrace();
			return false;
		}
	}
}