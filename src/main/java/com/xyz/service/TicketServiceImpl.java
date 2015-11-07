package com.xyz.service;

import java.util.Optional;
import java.util.Random;

import com.xyz.domain.ActiveHoldCarts;
import com.xyz.domain.Seat;
import com.xyz.domain.SeatHold;
import com.xyz.domain.SeatHoldImpl;
import com.xyz.domain.SeatPlan;

/**
 * Implementation class for the TicketService interface
 * Input objects SeatPlan and ActiveHoldCarts
 * <p>
 * 
 * </p> 
 * @author mkalam
 *
 */
public class TicketServiceImpl implements TicketService {

	SeatPlan seatPlan;
	ActiveHoldCarts activeCarts;

	/**
	 * 
	 * @param seatPlan
	 * @param activeCarts
	 */
	public TicketServiceImpl(SeatPlan seatPlan, ActiveHoldCarts activeCarts) {
		this.seatPlan = seatPlan;
		this.activeCarts = activeCarts;
	}

	/**
	 * 
	 */
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		return seatPlan.getEmptySeatCount(venueLevel);

	}

	/**
	 * 
	 * @return
	 */
	public int numSeatsAvailable() {
		return this.seatPlan.getEmptySeatCount(null);

	}

	/**
	 * 
	 */
	public String reserveSeats(int seatHoldId, String customerEmail) {
		Random generator = new Random();		
		int reservationConfirmationNumber=generator.nextInt(100000);
		for(SeatHold cart: activeCarts.getActiveShoppingCarts()){
			if(cart.getSeatHoldId()==seatHoldId){
				for (int i=0; i<cart.getSize(); i++) {
					cart.getSeats().get(i).setCustomerEmailAddress(customerEmail);
					cart.getSeats().get(i).setReservationConfirmationNumber(reservationConfirmationNumber);   
					seatPlan.reserveSeat(cart.getSeats().get(i));					
			    }
				activeCarts.deleteHoldCart(cart);
				return String.valueOf(reservationConfirmationNumber);	
			}
		}
		return null;	
		
	}

	/**
	 * 
	 */
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel,
			Optional<Integer> maxLevel, String customerEmail) {

		SeatHold seatHold = new SeatHoldImpl();

		for (int i = 0; i < numSeats; i++) {
			Seat seat = seatPlan.getNextAvailableSeat(minLevel);
			if (seat != null) {
				seatHold.addProduct(seat);
				seatPlan.holdSeat(seat);
			}
		}
		for (int i = seatHold.getSize(); i < numSeats; i++) {
			Seat seat = seatPlan.getNextAvailableSeat(maxLevel);
			if (seat != null) {
				seatHold.addProduct(seat);
				seatPlan.holdSeat(seat);
			}
		}
		if (seatHold == null || seatHold.getSize() != numSeats) {
			for (int i = 0; i < seatHold.getSize(); i++) {
				seatPlan.unhold(seatHold.getSeats().get(i));
			}
			return null;
		}
		activeCarts.addHoldCart(seatHold);
		return seatHold;
	}

	/**
	 * 
	 * @param numSeats
	 * @param minLevel
	 * @param customerEmail
	 * @return
	 */
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel,
			String customerEmail) {
		SeatHold seatHold = new SeatHoldImpl();

		for (int i = 0; i < numSeats; i++) {
			Seat seat = seatPlan.getNextAvailableSeat(minLevel);
			if (seat != null) {
				seatHold.addProduct(seat);
				seatPlan.holdSeat(seat);
			}
		}
		if (seatHold == null || seatHold.getSize() != numSeats) {
			for (int i = 0; i < seatHold.getSize(); i++) {
				seatPlan.unhold(seatHold.getSeats().get(i));
			}
			return null;
		}
		activeCarts.addHoldCart(seatHold);
		return seatHold;
	}

	/**
	 * 
	 * @param numSeats
	 * @param customerEmail
	 * @return
	 */
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		SeatHold seatHold = new SeatHoldImpl();
		for (int i = seatHold.getSize(); i < numSeats; i++) {
			Seat seat = seatPlan.getNextAvailableSeat(null);
			if (seat != null) {
				seatHold.addProduct(seat);
				seatPlan.holdSeat(seat);
			}
		}
		if (seatHold == null || seatHold.getSize() != numSeats) {
			for (int i = 0; i < seatHold.getSize(); i++) {
				seatPlan.unhold(seatHold.getSeats().get(i));
			}
			return null;
		}
		activeCarts.addHoldCart(seatHold);
		return seatHold;
	}

}
