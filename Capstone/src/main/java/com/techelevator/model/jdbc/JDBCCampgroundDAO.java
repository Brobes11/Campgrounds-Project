package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;

public class JDBCCampgroundDAO implements CampgroundDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}

	@Override
	public List<Campground> getAllCampgrounds() {
		List<Campground> allCampgrounds = new ArrayList<Campground>();
		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee "
				+ "FROM campground ORDER BY name;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			allCampgrounds.add(mapRowToCampground(results));
		}
		return allCampgrounds;
	}
	@Override
	public List<Campground> getAllCampgroundsByParkId(Park park) {
		List<Campground> allCampgrounds = new ArrayList<Campground>();
		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee "
				+ "FROM campground WHERE park_id =? ORDER BY name;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql,park.getId());
		while (results.next()) {
			allCampgrounds.add(mapRowToCampground(results));
		}
		return allCampgrounds;
	}
	
	private Campground mapRowToCampground(SqlRowSet rows) {
		Campground c = new Campground();
		c.setParkId(rows.getInt("park_id"));
		c.setName(rows.getString("name"));
		c.setId(rows.getInt("campground_id"));
		c.setOpenFromMM(rows.getString("open_from_mm"));
		c.setOpenToMM(rows.getString("open_to_mm"));
		c.setDailyFee(rows.getBigDecimal("daily_fee"));

		return c;

	}

}
