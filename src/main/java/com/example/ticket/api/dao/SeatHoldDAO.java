package com.example.ticket.api.dao;

import java.util.Collection;

import com.example.ticket.api.models.SeatHold;
/**
 * Interface for SeatHoldDAO.
 * 
 * @author satish
 *
 */
public interface SeatHoldDAO {
	/**
	 * This method queries the backend for seat holds and returns all the seat holds.
	 * 
	 * @return all seat holds.
	 */
	Collection<SeatHold> findAll();
	
	/**
	 * This method queries the backend for seat holds. This method first removes the 
	 * expired seat holds which has been on hold for more than timeToLiveSec seconds. 
	 * Then returns all un-expired seat holds.
	 * 
	 * @return all seat holds.
	 */
	Collection<SeatHold> findAllButRemoveExpired();
	
	/**
	 * Saves the supplied seatHold in the backend.
	 * 
	 * @param seatHold
	 * @throws DAOException
	 */
	void save(SeatHold seatHold) throws DAOException;
	
	/**
	 * Query the backend for seat hold for a specific SeatHoldKey.
	 * 
	 * @param seatHoldKey
	 * @return SeatHold - 
	 */
	SeatHold findBySeatHoldIdAndCustomerEmail(SeatHold.SeatHoldKey seatHoldKey);

}
