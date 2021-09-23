package br.ufma.lsdi.smartbins;

import java.util.Date;
import java.util.List;

public class Field {
	
	private String bin_id;
	private Date timestamp;
	private Double current_fill_level;
	private List<Double> latlong;
	private Double battery_health;
	private String bin_status;
	
	public String getBin_id() {
		return bin_id;
	}
	
	public void setBin_id(String bin_id) {
		this.bin_id = bin_id;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public Double getCurrent_fill_level() {
		return current_fill_level;
	}
	
	public void setCurrent_fill_level(Double current_fill_level) {
		this.current_fill_level = current_fill_level;
	}
	
	public List<Double> getLatlong() {
		return latlong;
	}
	
	public void setLatlong(List<Double> latlong) {
		this.latlong = latlong;
	}
	
	public Double getBattery_health() {
		return battery_health;
	}
	
	public void setBattery_health(Double battery_health) {
		this.battery_health = battery_health;
	}
	
	public String getBin_status() {
		return bin_status;
	}
	
	public void setBin_status(String bin_status) {
		this.bin_status = bin_status;
	}
}
