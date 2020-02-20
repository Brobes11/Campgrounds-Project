package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	
public List<Site> campsitesByCampgroundId(int campgroundId);

public List<Site> listTopFiveAvailableBySiteId(int campgroundId, LocalDate startDate, LocalDate endDate);

}
