package com.example.ticket.api.dao;

public interface DAOFactory {
	SeatDAO createSeatDAO() throws DAOException;
	SeatHoldDAO createSeatHoldDAO() throws DAOException;
}
