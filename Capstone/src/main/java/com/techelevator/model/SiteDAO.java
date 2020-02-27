package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	
public List<Site> campsitesByCampgroundId(int campgroundId);

public List<Site> listTopFiveAvailableByCampgroundId(int campgroundId, LocalDate startDate, LocalDate endDate);

}
