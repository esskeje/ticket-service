package com.example.ticket.api.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SeatHoldTest {
	@Test
	public void testConstruction() throws Exception {
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData = new SeatHold(111, "aaa@abc.com", heldSeats);
		assertEquals("seatHoldId",111, seatHoldData.getSeatHoldKey().getSeatHoldId());
		assertEquals("customerEmail","aaa@abc.com", seatHoldData.getSeatHoldKey().getCustomerEmail());
		assertEquals("heldSeats",heldSeats, seatHoldData.getHeldSeats());
	}
	@Test
	public void testEquals(){
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData1 = new SeatHold(111, "aaa@abc.com", heldSeats);
		SeatHold seatHoldData2 = new SeatHold(111, "aaa@abc.com", heldSeats);
		assertTrue("both the seatHolds should be equal",seatHoldData1.equals(seatHoldData2));
		assertFalse("object references are not equal for both the seat holds",seatHoldData1==seatHoldData2);
	}
	
	@Test
	public void testHashCode(){
		List<Seat> heldSeats =getHeldSeats();
		SeatHold seatHoldData1 = new SeatHold(111, "aaa@abc.com", heldSeats);
		SeatHold seatHoldData2 = new SeatHold(111, "aaa@abc.com", heldSeats);
		assertTrue("both the seatHolds should have same hashCode",seatHoldData1.hashCode()==seatHoldData2.hashCode());
		assertFalse("object references are not equal for both the seat holds",seatHoldData1==seatHoldData2);
	}
	
	//--util
	private List<Seat> getHeldSeats() {
		Level level = new Level(2, new BigDecimal(8));
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(3, 3, 10, Status.HELD, level));
		seats.add(new Seat(4, 3, 10, Status.HELD, level));
		return seats;
	}
}
