package model;

import java.io.Serializable;

public class DataModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private int value;
	private String longitude;
	private String latitude;
		
	public DataModel(String id, int value) {
		super();
		this.id = id;
		this.value = value;
	}
	
	
	
	public DataModel(String id, int value, String longitude, String latitude) {
		super();
		this.id = id;
		this.value = value;
		this.longitude = longitude;
		this.latitude = latitude;
	}



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	

}
