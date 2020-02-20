package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {
	public List<Reservation> getAllReservationsBySiteId(int siteId);

	public Reservation createReservation(Reservation newRes);

}
