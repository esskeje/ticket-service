package com.example.ticket.impl.dao.inmemory;

import com.example.ticket.api.dao.DAOException;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.api.dao.SeatDAO;
import com.example.ticket.api.dao.SeatHoldDAO;

public class InMemoryDAOFactoryImpl implements DAOFactory {

	@Override
	public SeatDAO createSeatDAO() throws DAOException {
		return new InMemorySeatDAOImpl();
	}

	@Override
	public SeatHoldDAO createSeatHoldDAO() {
		return new InMemorySeatHoldDAOImpl();
	}

}
