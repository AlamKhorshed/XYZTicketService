Ticket Service Homework
=======================

Overview:
=========
Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.
Serves following three methods:

1 Find the number of seats available within the venue, optionally by seating level
2 Find and hold the best available seats on behalf of a customer, potentially limited to specific levels
3 Reserve and commit a specific group of held seats for a customer


Assumptions
===========
The code is bundled in a JAR file. A Main class is configured to run automatically if the JAR is loadded in the JVM.
In-memory SeatPlan Representation. No persistence.
SeatID expression: {LEVEL-row-column}
Can be configured for high availibility.
Single threaded
Log4J is set in INFO level
Configuration properties are located at config.properties file
A CleanUp Thread runs behind the scene
The scheduler is set to run in every 30 seconds
HOLD expiration time is set 300 seconds
 
Building the example
====================
Download the project
Run simple maven build
mvn clean install
 

Running the Example:
====================
Note: Windows users please use Batch scripts (.bat) 

->User Interactive sessions for Testing purposes
->Driven through the command line
->There are three operations:
 
 Operation 1:
 ***********
 1--numSeatsAvailable(Optional<Integer> venueLevel)
 Example:
	1 (show total seats available)
	1 1 (show available for level 1)
	1 2 (show available for level 2)
	1 3 (show available for level 3)
	1 4 (show available for level 4)
	
Operation 2:
************
2--findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail)
 Example:
	 2 1 alam@yahoo.com (hold 1 seat from any level)
	 2 1 1 alam@yahoo.com (hold 1 seat from level 1)
	 2 1 2 alam@yahoo.com (hold 1 seat from level 2)
	 2 5 2 3 alam@yahoo.com (hold 5 seats from level 2 to 5)

Operation 3:
************
3-- reserveSeats(int seatHoldId, String customerEmail)
 Example:
	 3 555 alam@yahoo.com (reserve seats for 555 hold ID) 


	 
	 Sample Interactive session
==========================

1
# of available seats: 6250
1 1
# of available seats: 1250
1 3
# of available seats: 1500
2 1 2 alam@gmail.com
Seat Hold ID: 6
Seat Number MAIN-5-19 HOLD
2 3 3 4 alam@yahoo.com
Seat Hold ID: 388
Seat Number BALCONY_1-11-7 HOLD
Seat Number BALCONY_1-11-6 HOLD
Seat Number BALCONY_1-11-9 HOLD
3 388 alam@yahoo.com
Confirmation Code: 55807
1
# of available seats: 6246
1 3
# of available seats: 1497