package com.xyz.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xyz.domain.ActiveHoldCarts;
import com.xyz.domain.Seat;
import com.xyz.domain.SeatHold;
import com.xyz.domain.SeatImpl;
import com.xyz.domain.SeatPlan;
/**
 * Main Test Class
 * This class is configured in the JAR file as auto-run
 * 
 * @author mkalam
 *
 */
public class Main {

	final static Logger logger = Logger.getLogger(Main.class);
	private static SeatPlan seatPlan;
	private static ActiveHoldCarts activeCarts;
	private static Properties prop = new Properties();
	private static TicketServiceImpl ticketService;

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		seatPlan = new SeatPlan();
		activeCarts = new ActiveHoldCarts();
		ticketService = new TicketServiceImpl(seatPlan, activeCarts);
		Main self = new Main();
		self.loadProperties();
		self.populateSeatPlan();
		Thread cleanupThread = new Thread(new CleanupThread("XYZHoldCleanup",
				seatPlan, activeCarts, Long.parseLong(prop
						.getProperty("cleanup.schedule.interval")),
				Long.parseLong(prop.getProperty("hold.expiration.time"))));
		cleanupThread.setDaemon(true);
		cleanupThread.start();
		self.useInterActiveSession();
	}

	/**
	 * User Interactive sessions for Testing purposes
	 * Driven through the command line
	 * <p>
	 * There are three operations:
	 * Operation 1:
	 * 1--numSeatsAvailable(Optional<Integer> venueLevel)
	 * Example:
	 * 1 (show total seats available)
	 * 1 1 (show available for level 1)
	 * 1 2 (show available for level 2)
	 * 1 3 (show available for level 3)
	 * 1 4 (show available for level 4)
	 * </p>
	 * <p>
	 * Operation 2:
	 * 2--findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail)
	 * Example:
	 * 2 1 alam@yahoo.com (hold 1 seat from any level)
	 * 2 1 1 alam@yahoo.com (hold 1 seat from level 1)
	 * 2 1 2 alam@yahoo.com (hold 1 seat from level 2)
	 * 2 5 2 3 alam@yahoo.com (hold 5 seats from level 2 to 5)
	 * </p>
	 * <p>
	 * Operation 3:
	 * 3-- reserveSeats(int seatHoldId, String customerEmail)
	 * Example:
	 * 3 555 alam@yahoo.com (reserve seats for 555 hold ID) 
	 * </p>
	 * 
	 * @throws IOException
	 */
	private void useInterActiveSession() throws IOException {
		logger.debug("User Interactive session below. Please input in the following format:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = br.readLine();
		while (!line.equalsIgnoreCase("END")) {
			String[] strTokens = line.split(" ");

			switch (Integer.parseInt(strTokens[0].toString())) {
			case 1:
				int numOfSeatAvailable = 0;
				if (strTokens.length == 1) {
					numOfSeatAvailable = ticketService.numSeatsAvailable();
				} else if (strTokens.length == 2) {
					numOfSeatAvailable = ticketService
							.numSeatsAvailable(Optional.of(Integer
									.parseInt(strTokens[1].toString())));
				} else {
					logger.debug("Invalid Input for getting the number of available seats");
					break;
				}
				logger.info("# of available seats: " + numOfSeatAvailable);
				break;
			case 2:
				SeatHold hold;
				if (strTokens.length == 3) {
					hold = ticketService.findAndHoldSeats(
							Integer.parseInt(strTokens[1].toString()),
							strTokens[2].toString());
				} else if (strTokens.length == 4) {
					hold = ticketService.findAndHoldSeats(Integer
							.parseInt(strTokens[1].toString()), Optional
							.of(Integer.parseInt(strTokens[2].toString())),
							strTokens[3].toString());
				} else if (strTokens.length == 5) {
					hold = ticketService.findAndHoldSeats(Integer
							.parseInt(strTokens[1].toString()), Optional
							.of(Integer.parseInt(strTokens[2].toString())),
							Optional.of(Integer.parseInt(strTokens[3]
									.toString())), strTokens[4].toString());
				} else {
					logger.debug("Invalid Input for holding seats");
					break;
				}
				logger.info("Seat Hold ID: " + hold.getSeatHoldId());
				for (Seat seat : hold.getSeats()) {
					logger.info("Seat Number " + seat.getSKU() + " "
							+ seat.getReservationStatus());
				}
				break;

			case 3:
				String confirmationCode;
				if (strTokens.length == 3) {
					confirmationCode = ticketService.reserveSeats(
							Integer.parseInt(strTokens[1].toString()),
							strTokens[2]);
				} else {
					logger.debug("Invalid Input for holding seats");
					break;
				}
				logger.info("Confirmation Code: " + confirmationCode);
				break;
			}
			line = br.readLine();
		}

	}

	/**
	 * Load config.properties file
	 */
	public void loadProperties() {
		InputStream input = null;
		logger.debug("Loading the configuration properties...");
		try {
			input = this.getClass().getClassLoader()
					.getResourceAsStream("config.properties");
			if (input == null) {
				logger.debug("Sorry, unable to find " + "config.properties");
				return;
			}
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Done.");
		logger.debug("Generating the SeatPlan..");

	}

	/**
	 * Populates In-Memory SeatPlan from the seat dimensions provided in the property file  
	 * A real system would likely be backed by a relational database
	 */
	private void populateSeatPlan() {

		logger.debug("Generating the Level1 (Orchestrea) seats..");
		int noOfRows = Integer.parseInt(prop.getProperty("level1.noOfRows"));
		int seatInEachRow = Integer.parseInt(prop
				.getProperty("level1.noOfColumns"));
		double price = Double
				.parseDouble(prop.getProperty("level1.seat.price"));
		for (int i = 1; i <= noOfRows; i++) {
			for (int j = 1; j <= seatInEachRow; j++) {
				seatPlan.putSeat(new SeatImpl("ORCHESTRA-" + i + "-" + j, price));
			}
		}
		logger.debug("Done.");
		logger.debug("Generating the Level2 (Main) seats..");
		noOfRows = Integer.parseInt(prop.getProperty("level2.noOfRows"));
		seatInEachRow = Integer
				.parseInt(prop.getProperty("level2.noOfColumns"));
		price = Double.parseDouble(prop.getProperty("level2.seat.price"));
		for (int i = 1; i <= noOfRows; i++) {
			for (int j = 1; j <= seatInEachRow; j++) {
				seatPlan.putSeat(new SeatImpl("MAIN-" + i + "-" + j, price));
			}
		}
		logger.debug("Done.");
		logger.debug("Generating the Level3 (Balcony_1) seats..");
		noOfRows = Integer.parseInt(prop.getProperty("level3.noOfRows"));
		seatInEachRow = Integer
				.parseInt(prop.getProperty("level3.noOfColumns"));
		price = Double.parseDouble(prop.getProperty("level3.seat.price"));
		for (int i = 1; i <= noOfRows; i++) {
			for (int j = 1; j <= seatInEachRow; j++) {
				seatPlan.putSeat(new SeatImpl("BALCONY_1-" + i + "-" + j, price));
			}
		}
		logger.debug("Done.");
		logger.debug("Generating the Level4 (Balcony_2) seats..");
		noOfRows = Integer.parseInt(prop.getProperty("level4.noOfRows"));
		seatInEachRow = Integer
				.parseInt(prop.getProperty("level4.noOfColumns"));
		price = Double.parseDouble(prop.getProperty("level4.seat.price"));
		for (int i = 1; i <= noOfRows; i++) {
			for (int j = 1; j <= seatInEachRow; j++) {
				seatPlan.putSeat(new SeatImpl("BALCONY_2-" + i + "-" + j, price));
			}
		}
		logger.debug("Done.");

	}
}
