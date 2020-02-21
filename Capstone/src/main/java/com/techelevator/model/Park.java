package com.techelevator.model;

import java.time.LocalDate;

public class Park {
	private int id;
	private String name;
	private String location;
	private LocalDate establishDate;
	private int area;
	private int visitors;
	private String description;

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setEstablishDate(LocalDate establishDate) {
		this.establishDate = establishDate;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public LocalDate getEstablishDate() {
		return establishDate;
	}

	public int getArea() {
		return area;
	}

	public int getVisitors() {
		return visitors;
	}

	public String getDescription() {
		return description;
	}
	@Override
	public String toString() {
		return name;
	}

}
