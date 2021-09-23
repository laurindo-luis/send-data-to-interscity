package br.ufma.lsdi.smartbins;

import java.util.Date;
import static java.util.Objects.nonNull;

public class SmartBinsLevelField {
	
	private Long id;
	private String binId;
	private Double currentFillLevel;
	private Double batteryHealth;
	private Date timesTamp;
	private String latLong;
	private String binStatus;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBinId() {
		return binId;
	}
	
	public void setBinId(String binId) {
		this.binId = binId;
	}
	
	public Double getCurrentFillLevel() {
		return currentFillLevel;
	}
	
	public void setCurrentFillLevel(Double currentFillLevel) {
		this.currentFillLevel = currentFillLevel;
	}
	
	public Double getBatteryHealth() {
		return batteryHealth;
	}
	
	public void setBatteryHealth(Double batteryHealth) {
		this.batteryHealth = batteryHealth;
	}
	
	public String getLatLong() {
		return latLong;
	}
	
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	
	public String getBinStatus() {
		return binStatus;
	}
	
	public void setBinStatus(String binStatus) {
		this.binStatus = binStatus;
	}
	
	public Date getTimesTamp() {
		return timesTamp;
	}

	public void setTimesTamp(Date timesTamp) {
		this.timesTamp = timesTamp;
	}

	public static SmartBinsLevelField create(Field field) {
		SmartBinsLevelField smartBinsLevel = new SmartBinsLevelField();
		smartBinsLevel.setBinId(field.getBin_id());
		smartBinsLevel.setCurrentFillLevel(field.getCurrent_fill_level());
		smartBinsLevel.setBatteryHealth(field.getBattery_health());
		smartBinsLevel.setTimesTamp(field.getTimestamp());
		if(nonNull(field.getLatlong())) 
			smartBinsLevel.setLatLong(String.format("%s, %s", 
					field.getLatlong().get(0), field.getLatlong().get(1)));
		smartBinsLevel.setBinStatus(field.getBin_status());
		return smartBinsLevel;
	}
}
