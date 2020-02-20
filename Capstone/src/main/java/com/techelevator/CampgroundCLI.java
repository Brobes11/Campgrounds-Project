package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.projects.view.Menu;

public class CampgroundCLI {
	
	private JDBCSiteDAO site;
	private JDBCParkDAO park;
	private JDBCReservationDAO reservation;
	private JDBCCampgroundDAO campground;
	private Menu menu;
	
	
	

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername(System.getenv("DB_USERNAME"));
		dataSource.setPassword(System.getenv("DB_PASSWORD"));
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		site= new JDBCSiteDAO(datasource);
		park= new JDBCParkDAO(datasource);
		campground= new JDBCCampgroundDAO(datasource);
		reservation= new JDBCReservationDAO(datasource);
		menu= new Menu(System.in, System.out);
		
		
		
		
		
	}

	public void run() {

	}
}
