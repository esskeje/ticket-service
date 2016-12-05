package com.example.ticket.impl.dao.inmemory;

import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;

import com.example.ticket.api.Config;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.api.dao.SeatDAO;
import com.example.ticket.api.dao.SeatHoldDAO;
import com.example.ticket.api.models.Venue;

public class InMemoryDAOFactoryImplTest {
	@Test
	public void testCreateSeatDAO() throws Exception {
		DAOFactory fac= new InMemoryDAOFactoryImpl();
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getVenue()).andReturn(new Venue()).anyTimes();
		EasyMock.replay(config);
		SeatDAO seatDAO1 = fac.createSeatDAO(config);
		SeatDAO seatDAO2 = fac.createSeatDAO(config);
		assertTrue("should create an object of class InMemorySeatDAOImpl", seatDAO1 instanceof InMemorySeatDAOImpl);
		assertTrue("should create an object of class InMemorySeatDAOImpl", seatDAO2 instanceof InMemorySeatDAOImpl);
		assertTrue("should create a unique object every createSeatDAO() call", seatDAO1!=seatDAO2);
		EasyMock.verify(config);
	}
	
	@Test
	public void testCreateSeatHoldDAO() throws Exception {
		DAOFactory fac= new InMemoryDAOFactoryImpl();
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.replay(config);
		SeatHoldDAO seatHoldDAO1 = fac.createSeatHoldDAO(config);
		SeatHoldDAO seatHoldDAO2 = fac.createSeatHoldDAO(config);
		assertTrue("should create an object of class InMemorySeatDAOImpl", seatHoldDAO1 instanceof InMemorySeatHoldDAOImpl);
		assertTrue("should create an object of class InMemorySeatDAOImpl", seatHoldDAO2 instanceof InMemorySeatHoldDAOImpl);
		assertTrue("should create a unique object every createSeatDAO() call", seatHoldDAO1!=seatHoldDAO2);
		EasyMock.verify(config);
		
	}

}
