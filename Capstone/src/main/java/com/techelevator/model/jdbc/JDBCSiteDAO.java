package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> campsitesByCampgroundId(int campgroundId) {
		List<Site> sitesByCampground = new ArrayList<Site>();
		String sql = "SELECT site_id, site_number, max_occupancy, accessible, max_rv_length,"
				+ "utilities, campground_id FROM site WHERE campground_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campgroundId);
		while (results.next()) {
			sitesByCampground.add(mapRowToSite(results));
		}
		return sitesByCampground;
	}

	@Override
	public List<Site> listTopFiveAvailableBySiteId(int campgroundId, LocalDate startDate, LocalDate endDate) {
		List<Site> smokeysTop5Available = new ArrayList<>();
		String sql = "SELECT site_id FROM site WHERE campground_id = ? AND site_id NOT IN (SELECT site_id FROM reservation "
				+ "WHERE (start_date, start_date + num_days) OVERLAPS (?, ?));";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campgroundId, startDate, endDate);
		while (results.next()) {
			smokeysTop5Available.add(mapRowToSite(results));

		}

		return smokeysTop5Available;
	}

	private Site mapRowToSite(SqlRowSet rows) {
		Site s = new Site();
		s.setId(rows.getInt("site_id"));
		s.setSiteNumber(rows.getInt("site_number"));
		s.setMaxOccupancy(rows.getInt("max_occupancy"));
		s.setAccessible(rows.getBoolean("accessible"));
		s.setMaxRvLength(rows.getInt("max_rv_length"));
		s.setUtilities(rows.getBoolean("utilities"));
		s.setCampgroundId(rows.getInt("campground_id"));

		return s;
	}

}
