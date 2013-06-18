package com.example.datatypes;


public class Record {

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isDriver() {
		return Driver;
	}
	public void setDriver(boolean driver) {
		Driver = driver;
	}
	public boolean isRider() {
		return Rider;
	}
	public void setRider(boolean rider) {
		Rider = rider;
	}
	public double getSrcLat() {
		return srcLat;
	}
	public void setSrcLat(double srcLat) {
		this.srcLat = srcLat;
	}
	public double getSrcLong() {
		return srcLong;
	}
	public void setSrcLong(double srcLong) {
		this.srcLong = srcLong;
	}
	public double getDestLat() {
		return destLat;
	}
	public void setDestLat(double destLat) {
		this.destLat = destLat;
	}
	public double getDestlong() {
		return destlong;
	}
	public void setDestlong(double destlong) {
		this.destlong = destlong;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getCurrentLat() {
		return currentLat;
	}
	public void setCurrentLat(double currentLat) {
		this.currentLat = currentLat;
	}
	public double getCurrentLong() {
		return currentLong;
	}
	public void setCurrentLong(double currentLong) {
		this.currentLong = currentLong;
	}
	/**
	 * @param args
	 */
	String userId;
	boolean Driver;
	boolean Rider;
	double srcLat;
	double srcLong;
	double destLat;
	double destlong;
	double time; 
	double currentLat;
	double currentLong;
}