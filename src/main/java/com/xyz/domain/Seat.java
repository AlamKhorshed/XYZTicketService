package com.xyz.domain;

/**
 * Interface definitions for a seat object
 * 
 * <p>
 * Implementing class: SeatImpl
 * </p>
 * @author mkalam
 *
 */
public interface Seat {
	/**
	 * The stockkeeping unit. Seats are kept in a SeatPlan that maps the SKU of
	 * the seat to seat object.
	 *
	 * @return the SKU of a seat
	 */
	public String getSKU();

	/**
	 * 
	 * @return the reservation status of a seat {null, EMPTY, HOLD, RESERVED}
	 */
	public String getReservationStatus();

	/**
	 * 
	 * @param the
	 *            reservation status of a seat {null, EMPTY, HOLD, RESERVED}
	 */
	public void setReservationStatus(String status);

	/**
	 * Randomly generated INT.
	 * 
	 * @return the reservation confirmation number.
	 */
	public int getReservationConfirmationNumber();

	/**
	 * Randomly generated INT
	 * 
	 * @param reservationConfirmationNumber
	 */
	public void setReservationConfirmationNumber(
			int reservationConfirmationNumber);

	/**
	 * 
	 * @param customerEmailAddress
	 */
	public void setCustomerEmailAddress(String customerEmailAddress);
}
