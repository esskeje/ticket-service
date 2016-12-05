package com.example.ticket.impl.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.Config;
import com.example.ticket.api.ConfirmationCodeGenerator;
import com.example.ticket.api.SeatHoldIdGenerator;
import com.example.ticket.api.dao.DAOException;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.api.dao.SeatDAO;
import com.example.ticket.api.dao.SeatHoldDAO;
import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.SeatHold;
import com.example.ticket.api.models.SeatHold.SeatHoldKey;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.service.TicketService;

public class TicketServiceImplTest {
	Config config = null;
	DAOFactory factory = null;
	SeatDAO seatDAO = null;
	SeatHoldDAO seatHoldDAO = null;
	Logger logger = null;
	BestSeatStrategy bestSeatStrategy =null;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws Exception{
		config = EasyMock.createNiceMock(Config.class);
		factory = EasyMock.createNiceMock(DAOFactory.class);
		seatDAO = EasyMock.createNiceMock(SeatDAO.class);
		seatHoldDAO = EasyMock.createNiceMock(SeatHoldDAO.class);
		logger = EasyMock.createNiceMock(Logger.class);
		bestSeatStrategy = new PriceBasedBestSeatStrategy();
	}

	@Test
	public void testServiceConstructionWhenConfigIsNull() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply Config.");
		TicketService ticketService = new TicketServiceImpl(null, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), null, bestSeatStrategy, logger);

	}

	@Test
	public void testServiceConstructionWhenFactoryIsNull() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply DAOFactory.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), null, bestSeatStrategy, logger);

	}

	@Test
	public void testServiceConstructionWhenSeatHoldIdGeneratorIsNull() throws Exception {
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply SeatHoldIdGenerator.");
		TicketService ticketService = new TicketServiceImpl(config, null, new SimpleConfirmationCodeGenerator(),
				factory, bestSeatStrategy, logger);

	}

	@Test
	public void testServiceConstructionWhenConfirmationCodeGeneratorIsNull() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply ConfirmationCodeGenerator.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(), null, factory,
				bestSeatStrategy, logger);

	}

	@Test
	public void testServiceConstructionWhenBestSeatStrategyIsNull() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply BestSeatStrategy.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, null, logger);

	}

	@Test
	public void testServiceConstructionWhenLoggerIsNull() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply Logger.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, null);

	}
	@Test
	public void testServiceConstructionWhenSeatDAOCreationFailed() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andThrow(new DAOException("DAO"));
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("Ticket Service intialization failed.");
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}
	
	@Test
	public void testServiceConstructionWhenSeatHoldDAOCreationFailed() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andThrow(new DAOException("DAO"));
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("Ticket Service intialization failed.");
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}

	@Test
	public void testNumSeatsAvailableWhenThereIsNoExpiredSeatHolds() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(getAvailableSeats());
		EasyMock.expect(seatHoldDAO.findAllButRemoveExpired()).andReturn(new ArrayList<>());
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		int numSeatsAvailable = ticketService.numSeatsAvailable();
		assertEquals(2, numSeatsAvailable);
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}

	@Test
	public void testNumSeatsAvailableWhenThereAreSomeExpiredSeatHold() throws Exception {
		List<Seat> availableSeats = getAvailableSeats();
		List<Seat> heldSeats = getHeldSeats();
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1 = new SeatHold(1, "aaa@abc.com", heldSeats);
		seatHolds.add(seathHold1);
		availableSeats.addAll(heldSeats);
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);		
		EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(availableSeats);
		EasyMock.expect(seatHoldDAO.findAllButRemoveExpired()).andReturn(seatHolds);
		seatDAO.save(heldSeats);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		int numSeatsAvailable = ticketService.numSeatsAvailable();
		assertEquals(4, numSeatsAvailable);
		for (Seat seat : heldSeats) {
			assertEquals(Status.AVAILABLE, seat.getStatus());
		}
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}

	@Test
	public void testFindAndHoldSeatsWhenAllIsWell() throws Exception {
		List<Seat> availableSeats = getAvailableSeats();
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData = new SeatHold(111, "aaa@abc.com", heldSeats);
		List<SeatHold> seatHolds = new ArrayList<>();
		seatHolds.add(seatHoldData);
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 2)).andReturn(availableSeats);
		EasyMock.expect(seatHoldDAO.findAllButRemoveExpired()).andReturn(seatHolds);
		SeatHoldIdGenerator seatHoldIdGenerator = EasyMock.createNiceMock(SeatHoldIdGenerator.class);
		EasyMock.expect(seatHoldIdGenerator.getNextSeatHoldId()).andReturn(111);
		seatDAO.save(heldSeats);
		EasyMock.expectLastCall().once();
		seatDAO.save(availableSeats);
		EasyMock.expectLastCall().once();
		seatHoldDAO.save(seatHoldData);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger, seatHoldIdGenerator);
		TicketService ticketService = new TicketServiceImpl(config, seatHoldIdGenerator,
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		SeatHold seatHold = ticketService.findAndHoldSeats(2, "aaa@abc.com");
		assertEquals("aaa@abc.com", seatHold.getSeatHoldKey().getCustomerEmail());
		assertEquals(111, seatHold.getSeatHoldKey().getSeatHoldId());
		for (Seat seat : availableSeats) {
			assertEquals(Status.HELD, seat.getStatus());
		}
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger, seatHoldIdGenerator);
	}

	@Test
	public void testFindAndHoldSeatsWhenInvalidNumSeatsIsSupplied() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: ");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		SeatHold seatHold = ticketService.findAndHoldSeats(-1, "aaa@abc.com");
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}

	@Test
	public void testFindAndHoldSeatsWhenInvalidCustomerEmailIsSupplied() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: ");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		SeatHold seatHold = ticketService.findAndHoldSeats(2, "aaa.abc.com");
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}

	@Test
	public void testFindAndHoldSeatsWhenEnoughSeatsAreNotAvailable() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		List<Seat> availableSeats = getAvailableSeats();
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData = new SeatHold(111, "aaa@abc.com", heldSeats);
		List<SeatHold> seatHolds = new ArrayList<>();
		seatHolds.add(seatHoldData);
		EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 3)).andReturn(availableSeats);
		EasyMock.expect(seatHoldDAO.findAllButRemoveExpired()).andReturn(seatHolds);
		seatDAO.save(availableSeats);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("Enough seats are not available : 3");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		SeatHold seatHold = ticketService.findAndHoldSeats(3, "aaa@abc.com");
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}
	
	@Test
	public void testFindAndHoldSeatsWhenNoSeatsAreNotAvailable() throws Exception {
		List<Seat> availableSeats = getAvailableSeats();
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData = new SeatHold(111, "aaa@abc.com", heldSeats);
		List<SeatHold> seatHolds = new ArrayList<>();
		seatHolds.add(seatHoldData);
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 3)).andReturn(null);
		EasyMock.expect(seatHoldDAO.findAllButRemoveExpired()).andReturn(seatHolds);
		seatDAO.save(availableSeats);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Cause: requested seats not available.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		SeatHold seatHold = ticketService.findAndHoldSeats(3, "aaa@abc.com");
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}

	@Test
	public void testReserveSeatsWhenAllIsWell() throws Exception {
		List<Seat> expiredHeldSeats =getHeldSeats();
		SeatHold expiredSeatHoldData = new SeatHold(111, "bbb@abc.com", expiredHeldSeats);
		List<SeatHold> expiredSeatHolds = new ArrayList<>();
		expiredSeatHolds.add(expiredSeatHoldData);
		List<Seat> nonExpiredHeldSeats =getHeldSeatsInLevel3();
		SeatHold nonExpiredSeatHoldData = new SeatHold(222, "aaa@abc.com", nonExpiredHeldSeats);
		SeatHoldKey seatHoldKey = new SeatHoldKey(222, "aaa@abc.com");
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.expect(seatHoldDAO.findBySeatHoldIdAndCustomerEmail(seatHoldKey)).andReturn(nonExpiredSeatHoldData);
		EasyMock.expect(seatHoldDAO.findAllButRemoveExpired()).andReturn(expiredSeatHolds);
		seatDAO.save(expiredHeldSeats);
		EasyMock.expectLastCall().once();
		seatDAO.save(nonExpiredHeldSeats);
		EasyMock.expectLastCall().once();
		seatHoldDAO.save(nonExpiredSeatHoldData);
		EasyMock.expectLastCall().once();
		ConfirmationCodeGenerator confirmationCodeGenerator = EasyMock.createNiceMock(ConfirmationCodeGenerator.class);
		EasyMock.expect(confirmationCodeGenerator.getNextConfirmationCode()).andReturn("abcd");
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger, confirmationCodeGenerator);
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				confirmationCodeGenerator, factory, bestSeatStrategy, logger);
		String confirmationCode = ticketService.reserveSeats(222, "aaa@abc.com");
		assertEquals("abcd", confirmationCode);
		for (Seat seat : nonExpiredHeldSeats) {
			assertEquals(Status.COMMITED, seat.getStatus());
		}
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger, confirmationCodeGenerator);
	}
	
	@Test
	public void testReserveSeatsWhenAllIsWellButThereAreNoExpiredSeats() throws Exception {
		List<Seat> nonExpiredHeldSeats =getHeldSeatsInLevel3();
		SeatHold nonExpiredSeatHoldData = new SeatHold(222, "aaa@abc.com", nonExpiredHeldSeats);
		SeatHoldKey seatHoldKey = new SeatHoldKey(222, "aaa@abc.com");
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.expect(seatHoldDAO.findBySeatHoldIdAndCustomerEmail(seatHoldKey)).andReturn(nonExpiredSeatHoldData);
		seatDAO.save(nonExpiredHeldSeats);
		EasyMock.expectLastCall().once();
		seatHoldDAO.save(nonExpiredSeatHoldData);
		EasyMock.expectLastCall().once();
		ConfirmationCodeGenerator confirmationCodeGenerator = EasyMock.createNiceMock(ConfirmationCodeGenerator.class);
		EasyMock.expect(confirmationCodeGenerator.getNextConfirmationCode()).andReturn("abcd");
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger, confirmationCodeGenerator);
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				confirmationCodeGenerator, factory, bestSeatStrategy, logger);
		String confirmationCode = ticketService.reserveSeats(222, "aaa@abc.com");
		assertEquals("abcd", confirmationCode);
		for (Seat seat : nonExpiredHeldSeats) {
			assertEquals(Status.COMMITED, seat.getStatus());
		}
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger, confirmationCodeGenerator);
	}


	@Test
	public void testReserveSeatsWhenSeatHoldDoesnotExists() throws Exception {
		List<Seat> nonExpiredHeldSeats =getHeldSeatsInLevel3();
		SeatHold nonExpiredSeatHoldData = new SeatHold(222, "aaa@abc.com", nonExpiredHeldSeats);
		SeatHoldKey seatHoldKey = new SeatHoldKey(222, "aaa@abc.com");
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.expect(seatHoldDAO.findBySeatHoldIdAndCustomerEmail(seatHoldKey)).andReturn(nonExpiredSeatHoldData);
		ConfirmationCodeGenerator confirmationCodeGenerator = EasyMock.createNiceMock(ConfirmationCodeGenerator.class);
		EasyMock.expect(confirmationCodeGenerator.getNextConfirmationCode()).andReturn("abcd");
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger, confirmationCodeGenerator);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("Seat Hold doesn't exist for customerEmail=bbb@abc.com and seatHoldId=222");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				confirmationCodeGenerator, factory, bestSeatStrategy, logger);
		String confirmationCode = ticketService.reserveSeats(222, "bbb@abc.com");
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger, confirmationCodeGenerator);
	}

	@Test
	public void testReserveSeatsWhenSomeOfTheHoldsHasBeenExpired() throws Exception {
		List<Seat> nonExpiredHeldSeats =getHeldSeatsInLevel3();
		nonExpiredHeldSeats.addAll(getAvailableSeats());//now the seat hold has some available(expired) seats too
		SeatHold nonExpiredSeatHoldData = new SeatHold(222, "aaa@abc.com", nonExpiredHeldSeats);
		SeatHoldKey seatHoldKey = new SeatHoldKey(222, "aaa@abc.com");
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.expect(seatHoldDAO.findBySeatHoldIdAndCustomerEmail(seatHoldKey)).andReturn(nonExpiredSeatHoldData);
		seatDAO.save(nonExpiredHeldSeats);
		EasyMock.expectLastCall().once();
		ConfirmationCodeGenerator confirmationCodeGenerator = EasyMock.createNiceMock(ConfirmationCodeGenerator.class);
		EasyMock.expect(confirmationCodeGenerator.getNextConfirmationCode()).andReturn("abcd");
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger, confirmationCodeGenerator);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage(
				"TicketService.reserveSeats() failed. Cause: Some of the seats those were on hold have expired, please restart the ticket booking process.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				confirmationCodeGenerator, factory, bestSeatStrategy, logger);
		String confirmationCode = ticketService.reserveSeats(222, "aaa@abc.com");
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger, confirmationCodeGenerator);
	}

	@Test
	public void testSaveToDB() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		List<SeatHold> seatHolds = new ArrayList<>();
		List<Seat> heldSeats = getHeldSeats();
		SeatHold seatHold = new SeatHold(1, "aaa@abc.com", heldSeats);
		seatHolds.add(seatHold);
		seatDAO.save(heldSeats);
		EasyMock.expectLastCall().once();
		seatHoldDAO.save(seatHold);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		TicketServiceImpl ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		ticketService.saveToDB(heldSeats, seatHold);
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}

	@Test
	public void testRemoveExpiredSeatHoldsAndMarkThemAsAvailableSeats() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData = new SeatHold(111, "aaa@abc.com", heldSeats);
		List<SeatHold> seatHolds = new ArrayList<>();
		seatHolds.add(seatHoldData);
		EasyMock.expect(seatHoldDAO.findAllButRemoveExpired()).andReturn(seatHolds);
		seatDAO.save(heldSeats);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		TicketServiceImpl ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		ticketService.removeExpiredSeatHoldsAndMarkThemAsAvailableSeats();
		for (Seat seat : heldSeats) {
			assertEquals(Status.AVAILABLE, seat.getStatus());
		}
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}
	
	@Test
	public void testRemoveExpiredSeatHoldsAndMarkThemAsAvailableSeatsWhenThereIsNoExpiredSeat() throws Exception {
		EasyMock.expect(factory.createSeatDAO(config)).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO(config)).andReturn(seatHoldDAO);
		EasyMock.expect(seatHoldDAO.findAllButRemoveExpired()).andReturn(null);
		EasyMock.replay(config, factory, seatDAO, seatHoldDAO, logger);
		TicketServiceImpl ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), factory, bestSeatStrategy, logger);
		ticketService.removeExpiredSeatHoldsAndMarkThemAsAvailableSeats();
		EasyMock.verify(config, factory, seatDAO, seatHoldDAO, logger);
	}
	//-----utility methods
	private List<Seat> getAvailableSeats() {
		Level level = new Level(1, new BigDecimal(10));
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1, 1, 10, Status.AVAILABLE, level));
		seats.add(new Seat(2, 2, 10, Status.AVAILABLE, level));
		return seats;
	}
	private List<Seat> getHeldSeats() {
		Level level = new Level(2, new BigDecimal(8));
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(3, 3, 10, Status.HELD, level));
		seats.add(new Seat(4, 3, 10, Status.HELD, level));
		return seats;
	}
	private List<Seat> getHeldSeatsInLevel3() {
		Level level = new Level(3, new BigDecimal(5));
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(5, 4, 10, Status.HELD, level));
		seats.add(new Seat(6, 4, 10, Status.HELD, level));
		return seats;
	}


}
