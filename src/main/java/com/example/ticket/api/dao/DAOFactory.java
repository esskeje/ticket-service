package com.example.ticket.api.dao;

import com.example.ticket.api.Config;

/**
 * DAOFactory interface for SeatDAO and SeatHoldDAO.
 * 
 * @author satish
 *
 */
public interface DAOFactory {
	/**
	 * 
	 * @return SeatDAO
	 * @throws DAOException
	 */
	SeatDAO createSeatDAO(Config config) throws DAOException;
	
	/**
	 * 
	 * @return SeatHoldDAO
	 * @throws DAOException
	 */
	SeatHoldDAO createSeatHoldDAO(Config config) throws DAOException;
}
