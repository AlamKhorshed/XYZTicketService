package com.xyz.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IN-Memory SeatPlan Seats are kept in this SeatPlan that maps the SKU of the
 * seat to seat object. SeatPlan object provides basic operations like adding a
 * seat, updating (holding, unholding, reserving). It also provides additional
 * functionality like getEmptySeatCount() and getNextAvailableSeat().
 * 
 * <p>
 * A SeatPlan can be also be used to display seats and to look them up by SKU so
 * they may be placed in a hold cart. A real system would likely be backed by a
 * relational database
 * </p>
 *
 *
 * @author mkalam
 *
 */
public class SeatPlan {
	/**
	 * 
	 */
	private final String[] level = { "ORCHESTRA", "MAIN", "BALCONY_1",
			"BALCONY_2" };
	/**
	 * 
	 */
	private final Map<String, Seat> seatPlan;

	/**
	 * 
	 */
	public SeatPlan() {
		this.seatPlan = new ConcurrentHashMap<String, Seat>();
	}

	/**
	 * 
	 * @param sku
	 * @return
	 */
	public Seat getProductBySKU(String sku) {
		synchronized (this.seatPlan) {
			return this.seatPlan.get(sku);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Iterator<Seat> getSeats() {
		synchronized (this.seatPlan) {
			return new ArrayList<Seat>(this.seatPlan.values()).iterator();
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getSeatCount() {
		synchronized (this.seatPlan) {
			return this.seatPlan.size();
		}
	}

	/**
	 * 
	 * @param venueLevel
	 * @return
	 */
	public int getEmptySeatCount(Optional<Integer> venueLevel) {
		int count = 0;
		synchronized (this.seatPlan) {
			if (venueLevel == null || !venueLevel.isPresent()) {
				for (Seat value : seatPlan.values()) {
					if (value.getReservationStatus() == null
							|| value.getReservationStatus().equalsIgnoreCase(
									ReservationStatus.EMPTY.toString()))
						count++;
				}
			} else {
				for (Seat value : seatPlan.values()) {
					if (value.getSKU().startsWith(
							level[venueLevel.get().intValue() - 1])
							&& (value.getReservationStatus() == null || value
									.getReservationStatus().equalsIgnoreCase(
											ReservationStatus.EMPTY.toString())))
						count++;
				}
			}

		}

		return count;
	}

	/**
	 * 
	 * @param venueLevel
	 * @return
	 */
	public Seat getNextAvailableSeat(Optional<Integer> venueLevel) {

		synchronized (this.seatPlan) {
			if (venueLevel == null || !venueLevel.isPresent()) {
				for (Seat value : seatPlan.values()) {
					if (value.getReservationStatus() == null
							|| value.getReservationStatus().equalsIgnoreCase(
									ReservationStatus.EMPTY.toString())) {
						value.setReservationStatus("HOLD");
						return value;
					}
				}
			} else {
				for (Seat value : seatPlan.values()) {
					if (value.getSKU().startsWith(
							level[venueLevel.get().intValue() - 1])
							&& (value.getReservationStatus() == null || value
									.getReservationStatus().equalsIgnoreCase(
											ReservationStatus.EMPTY.toString())))
						return value;
				}
			}

		}

		return null;
	}

	/**
	 * 
	 * @param seat
	 */
	public void putSeat(Seat seat) {
		synchronized (this.seatPlan) {
			this.seatPlan.put(seat.getSKU(), seat);
		}
	}

	/**
	 * 
	 * @param seat
	 */
	public void holdSeat(Seat seat) {
		synchronized (this.seatPlan) {
			seat.setReservationStatus("HOLD");
			this.seatPlan.put(seat.getSKU(), seat);
		}
	}

	/**
	 * 
	 * @param seat
	 */
	public void reserveSeat(Seat seat) {
		synchronized (this.seatPlan) {
			seat.setReservationStatus("RESERVED");
			this.seatPlan.put(seat.getSKU(), seat);
		}
	}

	/**
	 * 
	 * @param seat
	 */
	public void unhold(Seat seat) {
		synchronized (this.seatPlan) {
			seat.setReservationStatus(ReservationStatus.EMPTY.toString());
			this.seatPlan.put(seat.getSKU(), seat);
		}

	}

}
