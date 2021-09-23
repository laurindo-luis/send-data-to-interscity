package br.ufma.lsdi.smartbins;

import java.util.Date;

public class Record {
	
	private String datasetid;
	private String recordid;
	private Field fields;
	private Date record_timestamp;
	
	public String getDatasetid() {
		return datasetid;
	}
	
	public void setDatasetid(String datasetid) {
		this.datasetid = datasetid;
	}
	public String getRecordid() {
		return recordid;
	}
	
	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}
	
	public Field getFields() {
		return fields;
	}
	
	public void setFields(Field fields) {
		this.fields = fields;
	}
	
	public Date getRecord_timestamp() {
		return record_timestamp;
	}
	
	public void setRecord_timestamp(Date record_timestamp) {
		this.record_timestamp = record_timestamp;
	}
}
