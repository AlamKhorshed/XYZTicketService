package com.xyz.service;

import org.apache.log4j.Logger;

import com.xyz.domain.ActiveHoldCarts;
import com.xyz.domain.SeatHold;
import com.xyz.domain.SeatPlan;

/**
 * The CleanUp Thread. It cleans up the SetHold/CART objects from the activeHoldCart collections in-memory which are expired/elapsed a threshold.
 * 
 *  <p>
 *  The schedule interval (sleep-time) is set in the properties file
 *  The holdCart expiration/elasped time is also set in the properties file. 
 *  Both are set in Milliseconds
 *  </p>
 * @author mkalam
 *
 */
public class CleanupThread implements Runnable {
	final static Logger logger = Logger.getLogger(CleanupThread.class);
	private String threadName;
	SeatPlan seatPlan;
	ActiveHoldCarts activeCarts;
	long sleepTime;
	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	long expirationTime;

	/**
	 * 
	 * @param name
	 * @param seatPlan
	 * @param activeCarts
	 * @param sleepTime
	 * @param expirationTime
	 */
	public CleanupThread(String name, SeatPlan seatPlan,
			ActiveHoldCarts activeCarts, long sleepTime, long expirationTime) {
		this.threadName = name;
		this.seatPlan = seatPlan;
		this.activeCarts = activeCarts;
		this.sleepTime=sleepTime;
		this.expirationTime=expirationTime;
	}
	
	/**
	 * 
	 */

	public void run() {
		logger.debug("Thread: " + threadName + " kicked off..");
		while (true) {
			for (SeatHold cart : activeCarts.getActiveShoppingCarts()) {
				if ((System.currentTimeMillis() - cart.getCreatedTs()) / 1000 > expirationTime) {
					logger.debug("Cart # " + cart.getSeatHoldId() + "Expired");
					for (int i = 0; i < cart.getSize(); i++) {
						logger.debug("Unholding seat: "
								+ cart.getSeats().get(i).getSKU());
						seatPlan.unhold(cart.getSeats().get(i));
					}
					logger.debug("Deleting Cart: " + cart.getSeatHoldId());
					activeCarts.deleteHoldCart(cart);
				}

			}
			try {
				Thread.sleep(sleepTime*1000);
			} catch (InterruptedException e) {
				logger.debug("Thread" + threadName + "Interruped");
			}
		}

	}

}
