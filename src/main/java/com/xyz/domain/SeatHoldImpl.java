package com.xyz.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of the SeatHold/CART interface.
 * 
 * 
 * @author mkalam
 *
 */
public class SeatHoldImpl implements SeatHold {

	private int seatHoldId;

	private long createdTs;

	public long getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(long createdTs) {
		this.createdTs = createdTs;
	}

	public SeatHoldImpl() {
		this.createdTs = System.currentTimeMillis();
		Random generator = new Random();
		this.seatHoldId = generator.nextInt(500);
	}

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	private List<Seat> seats = new LinkedList<Seat>();

	public List<Seat> getSeats() {
		return seats;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String id;

	/**
	 * Added thread safety in this method
	 */
	public void addProduct(final Seat seat) {
		synchronized (seats) {
			this.seats.add(seat);
		}
	}

	public int getSize() {
		return this.seats.size();
	}

}
