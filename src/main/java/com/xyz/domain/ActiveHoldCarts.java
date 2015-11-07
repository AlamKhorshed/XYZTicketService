package com.xyz.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Used to store Active Shopping Carts/SeatHold objects in Memory.
 * 
 * <p>
 * The CleanupThread iterates through the carts and UNHOLD seats if the cart is
 * expired.
 * </p>
 * <p>
 * <p>
 * Available methods: addHoldCart(SeatHold cart), deleteHoldCart(SeatHold cart),
 * getActiveShoppingCarts()
 * </p>
 * 
 * 
 * @author mkalam
 *
 */
public class ActiveHoldCarts {
	private final List<SeatHold> activeHoldCarts = new LinkedList<SeatHold>();

	public void addHoldCart(SeatHold cart) {
		synchronized (activeHoldCarts) {
			this.activeHoldCarts.add(cart);
		}
	}

	public void deleteHoldCart(SeatHold cart) {
		synchronized (activeHoldCarts) {
			this.activeHoldCarts.remove(cart);
		}
	}

	public List<SeatHold> getActiveShoppingCarts() {
		synchronized (this.activeHoldCarts) {
			List<SeatHold> carts = new LinkedList<SeatHold>(
					this.activeHoldCarts);
			return carts;
		}
	}

}
