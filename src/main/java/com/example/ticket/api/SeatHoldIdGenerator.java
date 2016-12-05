package com.example.ticket.api;
/**
 * Interface/contract to generate unique seatHoldId.
 * 
 * @author satish
 *
 */
public interface SeatHoldIdGenerator {
	/**
	 * 
	 * @return unique integer value
	 */
	public int getNextSeatHoldId() ;
}
