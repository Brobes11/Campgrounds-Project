package com.techelevator.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {
	private int id;
	private int siteId;
	private String name;
	private LocalDate startOfRes;
	private LocalDate endDate;
	private LocalDate createDate;
	private BigDecimal totalcost;
	private int duration;
	
	
	public int getDuration() {
		
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public BigDecimal getTotalcost() {
		
		return totalcost;
	}
	public void setTotalcost(BigDecimal totalcost) {
		this.totalcost = totalcost;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getStartOfRes() {
		return startOfRes;
	}
	public void setStartOfRes(LocalDate startOfRes) {
		this.startOfRes = startOfRes;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public LocalDate getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	

}
