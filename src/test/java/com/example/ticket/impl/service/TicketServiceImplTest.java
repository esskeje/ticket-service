package com.example.ticket.impl.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.Config;
import com.example.ticket.api.ConfirmationCodeGenerator;
import com.example.ticket.api.SeatHoldIdGenerator;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.api.dao.SeatDAO;
import com.example.ticket.api.dao.SeatHoldDAO;
import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.SeatHold;
import com.example.ticket.api.models.SeatHold.SeatHoldKey;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.service.TicketService;

public class TicketServiceImplTest{
	@Test
	public void testNumSeatsAvailableWhenThereIsNoExpiredSeatHolds() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level = new Level(1, new BigDecimal(5));
		List<Seat> seats= new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1,1,10,Status.AVAILABLE,level));
		seats.add(new Seat(2,2,10,Status.AVAILABLE,level));
		
		EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats);
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(new ArrayList<>());
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				new PriceBasedBestSeatStrategy(),
				logger);
		
		int numSeatsAvailable = ticketService.numSeatsAvailable();
		assertEquals(2, numSeatsAvailable);
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);
	}
	
	@Test
	public void testNumSeatsAvailableWhenThereAreSomeExpiredSeatHold() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level1 = new Level(1, new BigDecimal(5));
		List<Seat> seats1= new ArrayList<>();
		level1.setSeats(seats1);
		seats1.add(new Seat(1,1,10,Status.AVAILABLE,level1));
		seats1.add(new Seat(2,2,10,Status.AVAILABLE,level1));
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats1);
		
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				new PriceBasedBestSeatStrategy(),
				logger);
		
		int numSeatsAvailable = ticketService.numSeatsAvailable();
		assertEquals(2, numSeatsAvailable);
		for (Seat seat : seats2) {
			assertEquals(Status.AVAILABLE, seat.getStatus());
		}
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);

	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testNullConfigCheck() throws Exception{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply Config.");
		TicketService ticketService = new TicketServiceImpl(null, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				null, 
				new PriceBasedBestSeatStrategy(),
				Logger.getLogger("TicketServiceImplTest"));
		
	}
	
	@Test
	public void testNullFactoryCheck() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply DAOFactory.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				null, 
				new PriceBasedBestSeatStrategy(),
				Logger.getLogger("TicketServiceImplTest"));
		
	}
	
	@Test
	public void testNullSeatHoldIdGeneratorCheck() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level = new Level(1, new BigDecimal(5));
		List<Seat> seats= new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1,1,10,Status.AVAILABLE,level));
		seats.add(new Seat(2,2,10,Status.AVAILABLE,level));
		
		EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats);
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(new ArrayList<>());
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply SeatHoldIdGenerator.");
		TicketService ticketService = new TicketServiceImpl(config, null,
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				new PriceBasedBestSeatStrategy(),
				Logger.getLogger("TicketServiceImplTest"));
		
	}
	@Test
	public void testNullConfirmationCodeGeneratorCheck() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level = new Level(1, new BigDecimal(5));
		List<Seat> seats= new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1,1,10,Status.AVAILABLE,level));
		seats.add(new Seat(2,2,10,Status.AVAILABLE,level));
		
		EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats);
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(new ArrayList<>());
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply ConfirmationCodeGenerator.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				null, 
				factory, 
				new PriceBasedBestSeatStrategy(),
				Logger.getLogger("TicketServiceImplTest"));
		
	}
	@Test
	public void testNullBestSeatStrategyCheck() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level = new Level(1, new BigDecimal(5));
		List<Seat> seats= new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1,1,10,Status.AVAILABLE,level));
		seats.add(new Seat(2,2,10,Status.AVAILABLE,level));
		
		EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats);
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(new ArrayList<>());
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply BestSeatStrategy.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				null,
				Logger.getLogger("TicketServiceImplTest"));
		
	}
	@Test
	public void testNullLoggerCheck() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level = new Level(1, new BigDecimal(5));
		List<Seat> seats= new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1,1,10,Status.AVAILABLE,level));
		seats.add(new Seat(2,2,10,Status.AVAILABLE,level));
		
		EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats);
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(new ArrayList<>());
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply Logger.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				new PriceBasedBestSeatStrategy(),
				null);
		
	}
	
	@Test
	public void testFindAndHoldSeats() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level1 = new Level(1, new BigDecimal(5));
		List<Seat> seats1= new ArrayList<>();
		level1.setSeats(seats1);
		seats1.add(new Seat(1,1,10,Status.AVAILABLE,level1));
		seats1.add(new Seat(2,2,10,Status.AVAILABLE,level1));
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 2)).andReturn(seats1);
		
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		
		SeatHoldIdGenerator seatHoldIdGenerator= EasyMock.createNiceMock(SeatHoldIdGenerator.class);
		EasyMock.expect(seatHoldIdGenerator.getNextSeatHoldId()).andReturn(111);
		
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		seatDAO.save(seats1);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger,seatHoldIdGenerator);
		
		TicketService ticketService = new TicketServiceImpl(config, seatHoldIdGenerator,
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				bestSeatStrategy,
				logger);
		
		SeatHold seatHold = ticketService.findAndHoldSeats(2, "aaa@abc,com");
		assertEquals("aaa@abc,com", seatHold.getSeatHoldKey().getCustomerEmail());
		assertEquals(111, seatHold.getSeatHoldKey().getSeatHoldId());
		for (Seat seat : seats1) {
			assertEquals(Status.HELD, seat.getStatus());
		}
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger,seatHoldIdGenerator);

	}
	
	@Test
	public void testFindAndHoldSeatsWhenInvalidNumSeatsIsSupplied() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level1 = new Level(1, new BigDecimal(5));
		List<Seat> seats1= new ArrayList<>();
		level1.setSeats(seats1);
		seats1.add(new Seat(1,1,10,Status.AVAILABLE,level1));
		seats1.add(new Seat(2,2,10,Status.AVAILABLE,level1));
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 2)).andReturn(seats1);
		
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		seatDAO.save(seats1);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: ");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				bestSeatStrategy,
				logger);
		SeatHold seatHold = ticketService.findAndHoldSeats(-1, "aaa@abc.com");
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);
	}
	
	@Test
	public void testFindAndHoldSeatsWhenInvalidCustomerEmailIsSupplied() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level1 = new Level(1, new BigDecimal(5));
		List<Seat> seats1= new ArrayList<>();
		level1.setSeats(seats1);
		seats1.add(new Seat(1,1,10,Status.AVAILABLE,level1));
		seats1.add(new Seat(2,2,10,Status.AVAILABLE,level1));
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 2)).andReturn(seats1);
		
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		seatDAO.save(seats1);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: ");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				bestSeatStrategy,
				logger);
		
		SeatHold seatHold = ticketService.findAndHoldSeats(2, "aaa.abc.com");
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);

	}
	@Test
	public void testFindAndHoldSeatsWhenEnoughSeatsAreNotAvailable() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level1 = new Level(1, new BigDecimal(5));
		List<Seat> seats1= new ArrayList<>();
		level1.setSeats(seats1);
		seats1.add(new Seat(1,1,10,Status.AVAILABLE,level1));
		seats1.add(new Seat(2,2,10,Status.AVAILABLE,level1));
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		//EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats1);
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 3)).andReturn(seats1);
		
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		seatDAO.save(seats1);
		EasyMock.expectLastCall().once();
		//seatHoldDAO.save();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("Seats are not available : 3");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				bestSeatStrategy,
				logger);
		
		SeatHold seatHold = ticketService.findAndHoldSeats(3, "aaa@abc.com");
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);

	}
	@Test
	public void testReserveSeats() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level1 = new Level(1, new BigDecimal(5));
		List<Seat> seats1= new ArrayList<>();
		level1.setSeats(seats1);
		seats1.add(new Seat(1,1,10,Status.AVAILABLE,level1));
		seats1.add(new Seat(2,2,10,Status.AVAILABLE,level1));
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		
		Level level3 = new Level(3, new BigDecimal(5));
		List<Seat> seats3= new ArrayList<>();
		level3.setSeats(seats3);
		seats3.add(new Seat(1,1,10,Status.HELD,level3));
		seats3.add(new Seat(2,2,10,Status.HELD,level3));
		
		SeatHold seatHold2= new SeatHold(1, "aaa@abc.com", seats3);
		//EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats1);
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		//EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 2)).andReturn(seats1);
		SeatHoldKey seatHoldKey= new SeatHoldKey(1, "aaa@abc.com");
		EasyMock.expect(seatHoldDAO.findBySeatHoldIdAndCustomerEmail(seatHoldKey)).andReturn(seatHold2);
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		seatDAO.save(seats1);
		EasyMock.expectLastCall().once();
		ConfirmationCodeGenerator confirmationCodeGenerator= EasyMock.createNiceMock(ConfirmationCodeGenerator.class);
		EasyMock.expect(confirmationCodeGenerator.getNextConfirmationCode()).andReturn("abcd");
		//seatHoldDAO.save();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger,confirmationCodeGenerator);
		
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				confirmationCodeGenerator, 
				factory, 
				bestSeatStrategy,
				logger);
		
		String confirmationCode = ticketService.reserveSeats(1, "aaa@abc.com");
		assertEquals("abcd", confirmationCode);
		for (Seat seat : seats3) {
			assertEquals(Status.COMMITED, seat.getStatus());
		}
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger,confirmationCodeGenerator);

	}
	
	@Test
	public void testReserveSeatsWhenSeatHoldDoesnotExists() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level1 = new Level(1, new BigDecimal(5));
		List<Seat> seats1= new ArrayList<>();
		level1.setSeats(seats1);
		seats1.add(new Seat(1,1,10,Status.AVAILABLE,level1));
		seats1.add(new Seat(2,2,10,Status.AVAILABLE,level1));
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		
		Level level3 = new Level(3, new BigDecimal(5));
		List<Seat> seats3= new ArrayList<>();
		level3.setSeats(seats3);
		seats3.add(new Seat(1,1,10,Status.HELD,level3));
		seats3.add(new Seat(2,2,10,Status.HELD,level3));
		
		SeatHold seatHold2= new SeatHold(1, "aaa@abc.com", seats3);
		//EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats1);
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		//EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 2)).andReturn(seats1);
		SeatHoldKey seatHoldKey= new SeatHoldKey(1, "aaa@abc.com");
		EasyMock.expect(seatHoldDAO.findBySeatHoldIdAndCustomerEmail(seatHoldKey)).andReturn(seatHold2);
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		seatDAO.save(seats1);
		EasyMock.expectLastCall().once();
		ConfirmationCodeGenerator confirmationCodeGenerator= EasyMock.createNiceMock(ConfirmationCodeGenerator.class);
		EasyMock.expect(confirmationCodeGenerator.getNextConfirmationCode()).andReturn("abcd");
		//seatHoldDAO.save();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger,confirmationCodeGenerator);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("Seat Hold doesn't exist for customerEmail:bbb@abc.com and seatHoldId=1");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				confirmationCodeGenerator, 
				factory, 
				bestSeatStrategy,
				logger);
		
		String confirmationCode = ticketService.reserveSeats(1, "bbb@abc.com");
		assertEquals("abcd", confirmationCode);
		for (Seat seat : seats3) {
			assertEquals(Status.COMMITED, seat.getStatus());
		}
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger,confirmationCodeGenerator);

	}
	@Test
	public void testReserveSeatsWhenSomeOfTheHoldsHasBeenExpired() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		Level level1 = new Level(1, new BigDecimal(5));
		List<Seat> seats1= new ArrayList<>();
		level1.setSeats(seats1);
		seats1.add(new Seat(1,1,10,Status.AVAILABLE,level1));
		seats1.add(new Seat(2,2,10,Status.AVAILABLE,level1));
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		
		Level level3 = new Level(3, new BigDecimal(5));
		List<Seat> seats3= new ArrayList<>();
		level3.setSeats(seats3);
		seats3.add(new Seat(1,1,10,Status.AVAILABLE,level3));
		seats3.add(new Seat(2,2,10,Status.HELD,level3));
		
		SeatHold seatHold2= new SeatHold(1, "aaa@abc.com", seats3);
		//EasyMock.expect(seatDAO.findByStatus(Status.AVAILABLE)).andReturn(seats1);
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		//EasyMock.expect(seatDAO.findBestByStatus(Status.AVAILABLE, bestSeatStrategy, 2)).andReturn(seats1);
		SeatHoldKey seatHoldKey= new SeatHoldKey(1, "aaa@abc.com");
		EasyMock.expect(seatHoldDAO.findBySeatHoldIdAndCustomerEmail(seatHoldKey)).andReturn(seatHold2);
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		seatDAO.save(seats1);
		EasyMock.expectLastCall().once();
		ConfirmationCodeGenerator confirmationCodeGenerator= EasyMock.createNiceMock(ConfirmationCodeGenerator.class);
		EasyMock.expect(confirmationCodeGenerator.getNextConfirmationCode()).andReturn("abcd");
		//seatHoldDAO.save();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger,confirmationCodeGenerator);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Cause: Some of the seats those were on hold have expired, please restart the ticket booking process.");
		TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				confirmationCodeGenerator, 
				factory, 
				bestSeatStrategy,
				logger);
		
		String confirmationCode = ticketService.reserveSeats(1, "aaa@abc.com");
		assertEquals("abcd", confirmationCode);
		for (Seat seat : seats3) {
			assertEquals(Status.COMMITED, seat.getStatus());
		}
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger,confirmationCodeGenerator);

	}
	
	@Test
	public void testSaveToDB() throws Exception{

		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);

		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seatHold2= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seatHold2);
		
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		seatHoldDAO.save(seatHold2);
		EasyMock.expectLastCall().once();
		
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		
		TicketServiceImpl ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				bestSeatStrategy,
				logger);
		
		ticketService.saveToDB(seats2, seatHold2);
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);
	
	}

	@Test
	public void testRemoveExpiredSeatHoldsAndMarkThemAsAvailableSeats() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		List<SeatHold> seatHolds = new ArrayList<>();
		SeatHold seathHold1= new SeatHold(1, "aaa@abc.com", seats2);
		seatHolds.add(seathHold1);
		
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		EasyMock.expect(seatHoldDAO.findAndRemoveAllExpired(10)).andReturn(seatHolds);
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		seatDAO.save(seats2);
		EasyMock.expectLastCall().once();
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		TicketServiceImpl ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				bestSeatStrategy,
				logger);
		
		ticketService.removeExpiredSeatHoldsAndMarkThemAsAvailableSeats();;
		for (Seat seat : seats2) {
			assertEquals(Status.AVAILABLE, seat.getStatus());
		}
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);

	}
	
	@Test
	public void testUpdateSeatStatus() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		
		Level level2 = new Level(2, new BigDecimal(5));
		List<Seat> seats2= new ArrayList<>();
		level2.setSeats(seats2);
		seats2.add(new Seat(1,1,10,Status.HELD,level2));
		seats2.add(new Seat(2,2,10,Status.HELD,level2));
		
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		TicketServiceImpl ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				bestSeatStrategy,
				logger);
		
		ticketService.updateSeatStatus(seats2, Status.AVAILABLE);
		for (Seat seat : seats2) {
			assertEquals(Status.AVAILABLE, seat.getStatus());
		}
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);

	}
	
	@Test
	public void testNullCheck() throws Exception{
		Config config= EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getIntegerProperty("hold.expiry.time.inSeconds", 10)).andReturn(10);
		DAOFactory factory= EasyMock.createNiceMock(DAOFactory.class);
		SeatDAO seatDAO= EasyMock.createNiceMock(SeatDAO.class);
		SeatHoldDAO seatHoldDAO= EasyMock.createNiceMock(SeatHoldDAO.class);
		BestSeatStrategy bestSeatStrategy = new PriceBasedBestSeatStrategy();
		EasyMock.expect(factory.createSeatDAO()).andReturn(seatDAO);
		EasyMock.expect(factory.createSeatHoldDAO()).andReturn(seatHoldDAO);
		Logger logger= EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(config,factory,seatDAO,seatHoldDAO,logger);
		TicketServiceImpl ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
				new SimpleConfirmationCodeGenerator(), 
				factory, 
				bestSeatStrategy,
				logger);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply p1.");

		ticketService.nullCheck(null, "p1");
		EasyMock.verify(config,factory,seatDAO,seatHoldDAO,logger);

	}

}
