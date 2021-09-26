package br.ufma.lsdi.inserscity.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContextData {
	
	@JsonProperty("current_fill_level")
	private String currentFillLevel;
	@JsonProperty("battery_health")
	private String batteryHealth;
	@JsonProperty("timestamp")
	private String timesTamp;
	
	public String getCurrentFillLevel() {
		return currentFillLevel;
	}
	
	public void setCurrentFillLevel(String currentFillLevel) {
		this.currentFillLevel = currentFillLevel;
	}
	
	public String getBatteryHealth() {
		return batteryHealth;
	}
	
	public void setBatteryHealth(String batteryHealth) {
		this.batteryHealth = batteryHealth;
	}
	
	public String getTimesTamp() {
		return timesTamp;
	}
	
	public void setTimesTamp(String timesTamp) {
		this.timesTamp = timesTamp;
	}
}
