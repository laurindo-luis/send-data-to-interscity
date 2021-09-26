package br.ufma.lsdi.inserscity.resource;

import java.util.ArrayList;


import static java.util.Objects.isNull;

public class ResourceDto {
    private Data data;
    
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public static class Builder {
		private ResourceDto resource;
		
		public Builder() {
			resource = new ResourceDto();
			resource.setData(new Data());
		}
		
		public Builder setDescription(String description) {
			this.resource.getData().setDescription(description);
			return this;
		}
		
		public Builder addCapabilite(String capabilite) {
			if(isNull(this.resource.getData().getCapabilities()))
				this.resource.getData().setCapabilities(new ArrayList<>());
			
			this.resource.getData().getCapabilities().add(capabilite);
			return this;
		}
		
		public Builder setStatus(String status) {
			this.resource.getData().setStatus(status);
			return this;
		}
		
		public Builder setLatLon(Double lat, Double lon) {
			this.resource.getData().setLat(lat);
			this.resource.getData().setLon(lon);
			return this;
		}
				
		public Builder addContextData(Double current_fill_level, Double battery_health,
				String timestamp) {
			ContextData contextData = new ContextData();
			contextData.setCurrentFillLevel(""+current_fill_level);
			contextData.setBatteryHealth(""+battery_health);
			contextData.setTimesTamp(timestamp);
			
			if(isNull(resource.getData().getEnvironmentMonitoring()))
				resource.getData().setEnvironmentMonitoring(new ArrayList<>());
			
			resource.getData().getEnvironmentMonitoring().add(contextData);
			
			return this;
		}
		
		public ResourceDto build() {
			return this.resource;
		}
	}
}
