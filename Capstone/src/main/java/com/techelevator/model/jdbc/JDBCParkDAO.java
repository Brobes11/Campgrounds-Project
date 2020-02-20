package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> getAllParks() {
		List<Park> allParks = new ArrayList<Park>();
		String sql = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park ORDER BY name;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			allParks.add(mapRowToPark(results));
		}
		return allParks;
	}

	@Override
	public Park findParkByParkId(int parkId) {
		Park p = new Park();
		String sql = "SELECT park_id, name, location, establish_date, area, visitors, description "
				+ "FROM park WHERE park_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			p = mapRowToPark(results);
		}
		return p;
	}

	private Park mapRowToPark(SqlRowSet rows) {
		Park p = new Park();
		p.setId(rows.getInt("park_id"));
		p.setName(rows.getString("name"));
		p.setLocation(rows.getString("location"));
		p.setEstablishDate(rows.getDate("establish_date").toLocalDate());
		p.setArea(rows.getInt("area"));
		p.setVisitors(rows.getInt("visitors"));
		p.setDescription(rows.getString("description"));

		return p;

	}

}
