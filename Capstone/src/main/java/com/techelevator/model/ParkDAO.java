package com.techelevator.model;

import java.util.List;

public interface ParkDAO {
	
	public List<Park> getAllParks();
	
	public Park findParkByParkId(int parkId);

}
