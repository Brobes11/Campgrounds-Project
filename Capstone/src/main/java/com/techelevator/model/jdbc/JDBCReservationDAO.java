package com.techelevator.model.jdbc;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO{
	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Reservation> getAllReservationsBySiteId(int siteId) {
		List<Reservation> siteReservations = new ArrayList<Reservation>();
		String sql = "SELECT reservation_id, site_id, name, start_date, start_date+num_days AS end_date, create_date,numDays "
				+ "FROM reservation WHERE site_id ;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, siteId);
		while (results.next()) {
			siteReservations.add(mapRowToReservation(results));
		}
		
		return siteReservations;
	}

	@Override
	public Reservation createReservation(Reservation newRes) {
		String sql = " INSERT INTO reservation(site_id,name,start_date,num_days)"
				+" VALUES(?,?,?,?) RETURNING create_date,reservation_id;";
		SqlRowSet reservations = jdbcTemplate.queryForRowSet(sql,newRes.getSiteId(),newRes.getName(),
				newRes.getStartOfRes(),newRes.getDuration());
		if(reservations.next()) {
			newRes.setCreateDate(reservations.getDate("create_date").toLocalDate());
			newRes.setId(reservations.getInt("reservation_id"));
		}
		return newRes;
	}
	
	private Reservation mapRowToReservation(SqlRowSet rows) {
		Reservation r = new Reservation();
		r.setId(rows.getInt("reservation_id"));
		r.setSiteId(rows.getInt("site_id"));
		r.setName(rows.getString("name"));
		r.setStartOfRes(rows.getDate("start_date").toLocalDate());
		r.setEndDate(rows.getDate("end_date").toLocalDate());
		r.setCreateDate(rows.getDate("create_date").toLocalDate());
		r.setDuration(rows.getInt("num_days"));
		
		return r;
	}

}
