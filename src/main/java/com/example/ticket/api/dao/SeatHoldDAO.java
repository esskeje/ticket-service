package com.example.ticket.api.dao;

import java.util.Collection;

import com.example.ticket.api.models.SeatHold;

public interface SeatHoldDAO {
	Collection<SeatHold> findAll();
	Collection<SeatHold> findAndRemoveAllExpired(long timeToLiveSec);
	void save(SeatHold seatHold) throws DAOException;
	SeatHold findBySeatHoldIdAndCustomerEmail(SeatHold.SeatHoldKey seatHoldKey);

}
