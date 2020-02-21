package com.techelevator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;
import javax.swing.text.DateFormatter;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.projects.view.Menu;

public class CampgroundCLI {

	private static final String SELECT_PARK = "Select a park for further details";
	private static final String SELECT_A_COMMAND = "Select a command";
	private static final String VIEW_CAMPGROUNDS = "View campgrounds :) ";
	private static final String SEARCH_FOR_RESERVATIONS = "Search for reservations";
	private static final String RETURN_TO_PREVIOUS_SCREEN = "Return to previous screen";
	private static final String SEARCH_FOR_AVAILABLE_RESERVATIONS = "Search for available reservation";
	private static final String WHICH_CAMPGROUND = "Which campground ?(enter 0 to cancel)";
	private static final String WHAT_IS_THE_ARRIVAL_DATE = "What is the arrival date?(YYYY-MM-DD)";
	private static final String WHAT_IS_THE_DEPARTURE_DATE = "What is the departure date?(YYYY-MM-DD)";

	private static final String[] PARK_SUB_MENU = new String[] { VIEW_CAMPGROUNDS, SEARCH_FOR_RESERVATIONS,
			RETURN_TO_PREVIOUS_SCREEN };
	private static final String[] CAMPGROUND_SUB_MENU = new String[] { SEARCH_FOR_AVAILABLE_RESERVATIONS,
			RETURN_TO_PREVIOUS_SCREEN };
	private static final String[] NUMS_TO_MONTHS = new String[] { "January", "February", "March", "April", "May",
			"June", "July", "August", "Septmember", "October", "November", "December" };

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/YYYY");

	private JDBCSiteDAO site;
	private JDBCParkDAO park;
	private JDBCReservationDAO reservation;
	private JDBCCampgroundDAO campground;
	private Menu menu;
	private Scanner input;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campgrounds");
		dataSource.setUsername(System.getenv("DB_USERNAME"));
		dataSource.setPassword(System.getenv("DB_PASSWORD"));
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		site = new JDBCSiteDAO(datasource);
		park = new JDBCParkDAO(datasource);
		campground = new JDBCCampgroundDAO(datasource);
		reservation = new JDBCReservationDAO(datasource);
		menu = new Menu(System.in, System.out);
		input = new Scanner(System.in);
	}

	public void run() {
		while (true) {
			Object choice = handlePrintOptionsForParks(park.getAllParks().toArray());
			if (choice.equals("Q")) {
				System.exit(0);
			} else {
				handleDisplayParkInfo((Park) choice);
			}
		}
	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	private Object handlePrintOptionsForParks(Object[] object) {
		Object choice = null;
		printHeading(SELECT_PARK);
		for (int i = 0; i < object.length; i++) {
			System.out.format("%d) %s\n", i + 1, object[i]);

		}
		System.out.println("Q) quit");
		String userInput = input.nextLine();
		while (choice == null) {
			try {
				if (userInput.toUpperCase().equals("Q")) {
					choice = userInput.toUpperCase();
				} else if (Integer.parseInt(userInput) > 0 && Integer.parseInt(userInput) <= object.length) {
					choice = object[Integer.parseInt(userInput) - 1];

				}
			} catch (NumberFormatException e) {
// eat the exception, an error message will be displayed below since the choice will be null. 
			}
			if (choice == null) {
				System.out.println("\n*** " + userInput + " is not a valid option ***\n");
			}

		}
		return choice;

	}

	private void handleDisplayParkInfo(Park park) {
		System.out.println("\n" + park.getName());
		System.out.format("%-20s%-20s\n", "Location:", park.getLocation());
		System.out.format("%-20s%-20s\n", "Established:", DATE_FORMATTER.format(park.getEstablishDate()));
		System.out.format("%-20s%-20s\n", "Area:", park.getArea() + " sq km");
		System.out.format("%-20s%-20d\n", "Annual Visitors:", park.getVisitors());
		System.out.println("\n" + park.getDescription());
		handleParkSubMenu(park);
	}

	private void handleParkSubMenu(Park parks) {
		System.out.print("\n" + SELECT_A_COMMAND);
		String choice = (String) menu.getChoiceFromOptions(PARK_SUB_MENU);
		if (choice.equals(VIEW_CAMPGROUNDS)) {
			handleDisplayCampgroundInfo(parks);
			handleCampgroundSubMenu(parks);

		} else if (choice.equals(SEARCH_FOR_RESERVATIONS)) {

		} else if (choice.equals(RETURN_TO_PREVIOUS_SCREEN)) {
			// While loop will kick us to main menu if selected
		}

	}

	private void handleDisplayCampgroundInfo(Park newPark) {
		int i = 1;
		List<Campground> parkCampgrounds = campground.getAllCampgroundsByParkId(newPark);
		System.out.format("%10s %-35s %-15s %-15s %-15s \n", "", "Name", "Open", "Close", "Daily Fee");
		for (Campground c : parkCampgrounds) {
			System.out.format("%-10s %-35s %-15s %-15s %-15s \n", "#" + i, c.getName(),
					NUMS_TO_MONTHS[Integer.parseInt(c.getOpenFromMM()) - 1],
					NUMS_TO_MONTHS[Integer.parseInt(c.getOpenToMM()) - 1], "$" + c.getDailyFee());
			i++;

		}

	}

	private void handleCampgroundSubMenu(Park park) {
		Object keepGoing = null;
		Reservation search = new Reservation();
		System.out.print("\n" + SELECT_A_COMMAND);
		String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_SUB_MENU);
		if (choice.equals(SEARCH_FOR_AVAILABLE_RESERVATIONS)) {
			
			handleDisplayCampgroundInfo(park);
			keepGoing = handleWhichCampground(park);
			search = handleInAndOut(keepGoing);
			if (keepGoing.equals("Cancel")) {
				//goes back to main menu
			}else {
				handleDisplaySiteAvailability(search,(Campground)keepGoing);
			}
		
		
		} else if (choice.equals(RETURN_TO_PREVIOUS_SCREEN)) {
			handleDisplayParkInfo(park);

		}

	}

	private Object handleWhichCampground(Park park) {
		Object choice = null;
		List<Campground> parkCampgrounds = campground.getAllCampgroundsByParkId(park);
		while (choice == null) {
			System.out.print(WHICH_CAMPGROUND);
			String userInput = input.nextLine();
			try {
				int selectedOption = Integer.valueOf(userInput);
				if (selectedOption == 0) {
					choice = "Cancel";

				} else if (selectedOption > 0 && selectedOption <= parkCampgrounds.size()) {
					choice = parkCampgrounds.get(selectedOption - 1);

				}
			} catch (NumberFormatException n) {
				// exception will be handled in the line below ..
			}
			if (choice == null) {
				System.out.println("\n*** " + userInput + " is not a valid option ***\n");
			}
		}

		return choice;
	}

	private Reservation handleInAndOut(Object object) {
		Reservation smokiesVIP = new Reservation();

		if (object.equals("Cancel")) {
			// do not continue with any menus
		} else {
			boolean canParse = false;
			while (canParse == false) {
				System.out.print(WHAT_IS_THE_ARRIVAL_DATE);
				String userInput = input.nextLine();
				canParse = canInputParseDate(userInput);
				if (canParse == true) {
					smokiesVIP.setStartOfRes(LocalDate.parse(userInput));
				}

			}
			canParse = false;
			while (canParse == false) {
				System.out.print(WHAT_IS_THE_DEPARTURE_DATE);
				String userInput = input.nextLine();
				canParse = canInputParseDate(userInput);
				if (canParse == true) {
					smokiesVIP.setEndDate(LocalDate.parse(userInput));
				}

			}
		}
		return smokiesVIP;
	}

	private boolean canInputParseDate(String userInput) {
		boolean result = false;
		try {
			LocalDate.parse(userInput);
			result = true;
		} catch (DateTimeParseException e) {
			// exception handled below
		}
		if (result == false) {
			System.out.println("\n*** " + userInput + " is not a valid option ***\n");
		}
		return result;
	}

	private void handleDisplaySiteAvailability(Reservation campWithSmokey, Campground smokeysPlayhouse) {

		Period days = Period.between(campWithSmokey.getStartOfRes(), campWithSmokey.getEndDate());
		BigDecimal duration = new BigDecimal(days.getDays());
		List<Site> smokeysFavoriteSites = site.listTopFiveAvailableBySiteId(smokeysPlayhouse.getId(),
				campWithSmokey.getStartOfRes(), campWithSmokey.getEndDate());
		System.out.format("%15s %-15s %-15s %-15s %-15s %-15s \n", "Site No.", "Max Occup.", "Accessible?",
				"Max RV Length", "Utility", "Cost");
		for (Site site : smokeysFavoriteSites) {
			System.out.format("%15d %-15d %-15s %-15d %-15s %-15d \n", site.getId(), site.getMaxOccupancy(),
					convertBoolToString(site.isAccessible()), convertRvLengthToString(site.getMaxRvLength()),
					convertBoolToString(site.isUtilities()), smokeysPlayhouse.getDailyFee().multiply(duration));

		}

	}

	private String convertBoolToString(boolean smokeySays) {
		String result = "yes";
		if (smokeySays == false) {
			result = "no";
		}
		return result;
	}

	private String convertRvLengthToString(int rvLength) {
		String result = "" + rvLength;
		if (rvLength == 0) {
			result = "N/A";

		}
		return result;
	}
}
