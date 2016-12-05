package com.example.ticket.impl.dao.inmemory;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.Config;
import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.models.Venue;
import com.example.ticket.impl.service.PriceBasedBestSeatStrategy;

public class InMemorySeatDAOImplTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testFindByStatus() throws Exception {
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getVenue()).andReturn(create()).anyTimes();
		EasyMock.replay(config);
		InMemorySeatDAOImpl dao = new InMemorySeatDAOImpl(config);
		List<Seat> list = dao.findByStatus(Status.AVAILABLE);
		assertEquals(4, list.size());
		EasyMock.verify(config);
	}
	@Test
	public void testFindBestByStatus() throws Exception {
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getVenue()).andReturn(create()).anyTimes();
		EasyMock.replay(config);
		InMemorySeatDAOImpl dao = new InMemorySeatDAOImpl(config);
		List<Seat> list = dao.findBestByStatus(Status.HELD, new PriceBasedBestSeatStrategy(), 3);
		assertEquals(3, list.size());
		assertEquals(3, list.get(0).getId());
		assertEquals(4, list.get(1).getId());
		assertEquals(7, list.get(2).getId());
		EasyMock.verify(config);
	}
	
	@Test
	public void testSave() throws Exception {
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getVenue()).andReturn(create()).anyTimes();
		EasyMock.replay(config);
		InMemorySeatDAOImpl dao = new InMemorySeatDAOImpl(config);
		List<Seat> seats = getSeats();
		dao.save(seats);
		assertEquals(9, dao.getAllSeats().size());
		EasyMock.verify(config);
	}
	
	@Test
	public void testGetAllSeatsAndObjectConstruction() throws Exception {
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.expect(config.getVenue()).andReturn(create()).anyTimes();
		EasyMock.replay(config);
		InMemorySeatDAOImpl dao = new InMemorySeatDAOImpl(config);
		assertEquals(8, dao.getAllSeats().size());
		EasyMock.verify(config);
	}
	@Test
	public void testObjectConstructionWhenConfigIsNull() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("'config' must not be null.");
		InMemorySeatDAOImpl dao = new InMemorySeatDAOImpl(null);
	}
	@Test
	public void testObjectConstructionWhenConfigDotGetVenueIsNull() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("'config.getVenue()' must not be null.");
		Config config = EasyMock.createNiceMock(Config.class);
		EasyMock.replay(config);
		InMemorySeatDAOImpl dao = new InMemorySeatDAOImpl(config);
		EasyMock.verify(config);
	}
	//util method
	
	private List<Seat> getSeats() {
		Level level = new Level(1, new BigDecimal(10));
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1, 1, 10, Status.AVAILABLE, level));
		seats.add(new Seat(20, 2, 10, Status.AVAILABLE, level));
		return seats;
	}
	
	public static Venue create() throws IOException {
		Set<Level> levels = new HashSet<>();
		Venue venue = new Venue();
		
		int seatCount=1;
		int rank=100;//TODO rank can be calculated based on various factor then seats can be ranked based on it
		int numSeatsPerRow=2;
		BigDecimal price= new BigDecimal(50);
		//right now it's unused
		for(int levelCount=1;levelCount<=2;levelCount++){
			Level level= new Level(levelCount,price);
			levels.add(level);
			List<Seat> seats= new ArrayList<>();
			level.setSeats(seats);
			price.subtract(new BigDecimal(5));
			for(int rowCount=1;rowCount<=2;rowCount++){
				for(int seatsPerRowCount=1;seatsPerRowCount<=numSeatsPerRow;seatsPerRowCount++){
					Status status=null;
					if(rowCount%2==0){
						status=Status.HELD;
					}
					else{
						status=Status.AVAILABLE;
					}
					Seat seat= new Seat(seatCount, rowCount, rank, status,level);
					seats.add(seat);
					seatCount++;
				}
			}
		}
		venue.setLevels(levels);
		return venue;
	}

}
