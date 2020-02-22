package com.techelevator.model;

import java.math.BigDecimal;

public class Campground {
private int id;
private String name;
private int parkId;
private String openFromMM;
private String openToMM;
private BigDecimal dailyFee;

public int getId() {
	return id;
}
public String getName() {
	return name;
}
public int getParkId() {
	return parkId;
}
public String getOpenFromMM() {
	return openFromMM;
}
public String getOpenToMM() {
	return openToMM;
}
public BigDecimal getDailyFee() {
	return dailyFee.setScale(2);
}
public void setId(int id) {
	this.id = id;
}
public void setName(String name) {
	this.name = name;
}
public void setParkId(int parkId) {
	this.parkId = parkId;
}
public void setOpenFromMM(String openFromMM) {
	this.openFromMM = openFromMM;
}
public void setOpenToMM(String openToMM) {
	this.openToMM = openToMM;
}
public void setDailyFee(BigDecimal dailyFee) {
	this.dailyFee = dailyFee.setScale(2);
}


}
