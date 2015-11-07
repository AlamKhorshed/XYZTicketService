package com.xyz.domain;

import java.util.List;

/**
 * Interface of a SeatHold/CART object.
 * <p>
 * A SeatHold class contains a list of seats that a customer has reserved
 * through the findAndHoldSeats operation *
 * </p>
 * <p>
 * Implementing class: SeatHoldImpl
 * </p>
 * 
 * @author mkalam
 *
 */
public interface SeatHold {
	/**
	 * 
	 * @param product
	 */
	public void addProduct(final Seat product);

	/**
	 * 
	 * @return
	 */
	public int getSize();

	/**
	 * 
	 * @return
	 */
	public List<Seat> getSeats();

	/**
	 * 
	 * @return
	 */
	public int getSeatHoldId();

	/**
	 * 
	 * @return
	 */
	public long getCreatedTs();

}
