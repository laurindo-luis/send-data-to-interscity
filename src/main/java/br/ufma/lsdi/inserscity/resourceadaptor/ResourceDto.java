package br.ufma.lsdi.inserscity.resourceadaptor;

import java.util.List;

import br.ufma.lsdi.inserscity.Data;

public class ResourceDto {
    private Data data;
    
    public ResourceDto(String description, List<String> capabilites, String status, 
    		Double lat, Double lon) {
    	data = new Data();
    	data.setDescription(description);
    	data.setCapabilities(capabilites);
    	data.setStatus(status);
    	data.setLat(lat);
    	data.setLon(lon);
    }

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
