package com.xyz.test;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;

import org.junit.Test;

import com.xyz.domain.ActiveHoldCarts;
import com.xyz.domain.Seat;
import com.xyz.domain.SeatHold;

import com.xyz.domain.SeatImpl;
import com.xyz.domain.SeatPlan;
import com.xyz.service.TicketServiceImpl;

public class XYZTicketServiceTests {
	private static SeatPlan seatPlan;
	private static ActiveHoldCarts activeCarts;
	private static TicketServiceImpl ticketService;
	
	
	
	
	@Before
	public void setup(){		
		seatPlan=new SeatPlan();
		activeCarts=new ActiveHoldCarts();	
		ticketService = new TicketServiceImpl(seatPlan, activeCarts);
		Seat seat1 = new SeatImpl("MAIN-01-01", 100.00);
		Seat seat2 = new SeatImpl("ORCHESTRA-01-02", 100.00);		
		Seat seat3 = new SeatImpl("MAIN-01-02", 100.00);		
		Seat seat4 = new SeatImpl("MAIN-01-03", 100.00);
		
		seatPlan.putSeat(seat1);
		seatPlan.putSeat(seat2);
		seatPlan.putSeat(seat3);
		seatPlan.putSeat(seat4);
	}
	
	/**
	 * Empty seatCount without parameter
	 */
	@Test
	
	public  void testEmptyCollection(){
	 assertEquals(4, seatPlan.getEmptySeatCount(null));
	}
	
	@Test
	public  void testSeatCount(){
	 assertEquals(4, seatPlan.getSeatCount());
	}
	
	/**
	 * Empty seatCount with parameter
	 */
	@Test
	public  void testEmptySeatCount(){
	 assertEquals(3, seatPlan.getEmptySeatCount(Optional.of(Integer.valueOf(2))));
	}
	
	/**
	 * Test NextAvailableSeat with level preference
	 */
	@Test
	public  void testNextAvailableSeat(){
		Seat seat=seatPlan.getNextAvailableSeat(Optional.of(Integer.valueOf(1)));
		assertEquals("ORCHESTRA-01-02", seat.getSKU());
	}
	
	/**
	 * Test NextAvailableSeat without level preference
	 */
	@Test
	public  void testNextAvailableSeatWithoutPreference(){
		Seat seat=seatPlan.getNextAvailableSeat(null);
		assertEquals("MAIN-01-01", seat.getSKU());
	}
	
	/**
	 * Test TicketServiceImpl
	 */
	@Test
	public  void testTicketServiceImplFunction1WithoutParameter(){
		assertEquals(4, ticketService.numSeatsAvailable());
	}
	
	@Test
	public  void testTicketServiceImplFunction1WithParameter(){
		assertEquals(3, ticketService.numSeatsAvailable(Optional.of(Integer.valueOf(2))));
	}
	@Test
	public  void testfindAndHoldSeats(){
		SeatHold hold=ticketService.findAndHoldSeats(1, "malam7@gmail.com");
		assertNotNull(hold);
	}
	@Test
	public  void testfindAndHoldSeatsWithOption1(){
		SeatHold hold=ticketService.findAndHoldSeats(1, Optional.of(Integer.valueOf(2)), "malam7@gmail.com");
		assertNotNull(hold);
	}
	
}
