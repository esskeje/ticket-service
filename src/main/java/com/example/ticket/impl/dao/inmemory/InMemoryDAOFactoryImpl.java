package com.example.ticket.impl.dao.inmemory;

import com.example.ticket.api.Config;
import com.example.ticket.api.dao.DAOException;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.api.dao.SeatDAO;
import com.example.ticket.api.dao.SeatHoldDAO;
/**
 * This class implements in-memory implementation of DAOFactory.
 * 
 * @author satish
 *
 */
public class InMemoryDAOFactoryImpl implements DAOFactory {

	@Override
	public SeatDAO createSeatDAO(Config config) throws DAOException {
		return new InMemorySeatDAOImpl(config);
	}

	@Override
	public SeatHoldDAO createSeatHoldDAO(Config config) {
		return new InMemorySeatHoldDAOImpl(config);
	}

}
