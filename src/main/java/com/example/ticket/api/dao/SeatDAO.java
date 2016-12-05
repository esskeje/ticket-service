package com.example.ticket.api.dao;

import java.util.List;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;
/**
 * Interface for SeatDAO
 * 
 * @author satish
 *
 */
public interface SeatDAO {
	/**
	 * It finds/returns the list of seats from the backend.
	 * It returns those seats that match supplied status.
	 * 
	 * @param status - status of the seats to be queried
	 * @return list of Seat
	 */
	List<Seat> findByStatus(Status status);
	
	/**
	 * Save/persist the supplied list of seats to backend.
	 * @param seats
	 */
	void save(List<Seat> seats);
	
	/**
	 * It returns/finds best number of seats (numSeats) from the underlying backend.
	 * While returning the seats it applied algorithm to find best seat based on the 
	 * supplied BestSeatStrategy implementation.
	 * 
	 * @param status
	 * @param bestStrategy
	 * @param numSeats
	 * @return
	 */
	List<Seat> findBestByStatus(Status status,BestSeatStrategy bestStrategy, int numSeats);

}
