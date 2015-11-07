package com.xyz.domain;

/**
 * Implementation of Seat interface
 * 
 * @author mkalam
 *
 */

public class SeatImpl implements Seat {

	/**
	 * The stockkeeping unit. 
	 */
	private String sku;
	@SuppressWarnings("unused")
	private double price;
	private String reservationStatus;
	private int reservationConfirmationNumber;
	private String customerEmailAddress;

	/**
	 * 
	 * @return
	 */
	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	/**
	 * 
	 */

	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}

	/**
	 * 
	 */

	public int getReservationConfirmationNumber() {
		return reservationConfirmationNumber;
	}

	/**
	 * 
	 */

	public void setReservationConfirmationNumber(
			int reservationConfirmationNumber) {
		this.reservationConfirmationNumber = reservationConfirmationNumber;
	}

	/**
	 * 
	 */

	public void setReservationStatus(String reservationStatus) {
		this.reservationStatus = reservationStatus;
	}

	/**
	 * 
	 * @param emailAddress
	 */

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
 * 
 */
	@SuppressWarnings("unused")
	private String emailAddress;

	/**
	 * 
	 * @param sku
	 * @param price
	 */
	public SeatImpl(String sku, double price) {
		this.sku = sku;
		this.price = price;

	}

	/**
	 * 
	 */
	public String getReservationStatus() {
		return this.reservationStatus;
	}

	/**
 * 
 */
	public String getSKU() {
		return this.sku;
	}

}
