package com.example.ticket.api;

import java.util.List;

import com.example.ticket.api.models.Seat;
/**
 * Interface/contract to plugin various BestSeatStrategy.
 * 
 * @author satish
 *
 */
public interface BestSeatStrategy {
	/**
	 * Return best seats.
	 * 
	 * @param seats
	 * @param count
	 * @return best seats
	 */
	List<Seat> findBest(List<Seat> seats, int count);

}
