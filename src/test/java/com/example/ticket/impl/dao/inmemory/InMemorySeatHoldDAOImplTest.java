package com.example.ticket.impl.dao.inmemory;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.Config;
import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.SeatHold;
import com.example.ticket.api.models.Status;

public class InMemorySeatHoldDAOImplTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testFindAll() throws Exception {
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		EasyMock.replay(config);
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData1 = new SeatHold(111, "aaa@abc.com", heldSeats);
		List<Seat> availableSeats =getAvailableSeats();
		SeatHold seatHoldData2 = new SeatHold(222, "bbb@abc.com", availableSeats);
		
		InMemorySeatHoldDAOImpl dao = new InMemorySeatHoldDAOImpl(config);
		dao.getAllSeatHolds().put(seatHoldData1.getSeatHoldKey(), seatHoldData1);
		dao.getAllSeatHolds().put(seatHoldData2.getSeatHoldKey(), seatHoldData2);
		
		Collection<SeatHold> all = dao.findAll();
		assertEquals(2, all.size());
		EasyMock.verify(config);
	}
	
	@Test
	public void testSave() throws Exception {
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		EasyMock.replay(config);
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData1 = new SeatHold(111, "aaa@abc.com", heldSeats);
		List<Seat> availableSeats =getAvailableSeats();
		SeatHold seatHoldData2 = new SeatHold(222, "bbb@abc.com", availableSeats);
		
		InMemorySeatHoldDAOImpl dao = new InMemorySeatHoldDAOImpl(config);
		dao.getAllSeatHolds().put(seatHoldData1.getSeatHoldKey(), seatHoldData1);
		dao.save(seatHoldData2);
		assertEquals(2, dao.getAllSeatHolds().size());
		EasyMock.verify(config);
	}
	
	@Test
	public void testFindBySeatHoldIdAndCustomerEmail() throws Exception {
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		EasyMock.replay(config);
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData1 = new SeatHold(111, "aaa@abc.com", heldSeats);
		List<Seat> availableSeats =getAvailableSeats();
		SeatHold seatHoldData2 = new SeatHold(222, "bbb@abc.com", availableSeats);
		
		InMemorySeatHoldDAOImpl dao = new InMemorySeatHoldDAOImpl(config);
		dao.getAllSeatHolds().put(seatHoldData1.getSeatHoldKey(), seatHoldData1);
		dao.getAllSeatHolds().put(seatHoldData2.getSeatHoldKey(), seatHoldData2);
		SeatHold findBySeatHoldIdAndCustomerEmail = dao.findBySeatHoldIdAndCustomerEmail(seatHoldData2.getSeatHoldKey());
		assertEquals(seatHoldData2, findBySeatHoldIdAndCustomerEmail);
		EasyMock.verify(config);
	}
	
	//---util
	private List<Seat> getHeldSeats() {
		Level level = new Level(2, new BigDecimal(8));
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1, 1, 10, Status.HELD, level));
		seats.add(new Seat(2, 1, 10, Status.HELD, level));
		return seats;
	}
	
	private List<Seat> getAvailableSeats() {
		Level level = new Level(2, new BigDecimal(8));
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(3, 2, 10, Status.AVAILABLE, level));
		seats.add(new Seat(4, 2, 10, Status.AVAILABLE, level));
		return seats;
	}
	
	
}
