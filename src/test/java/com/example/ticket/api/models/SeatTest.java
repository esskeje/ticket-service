package com.example.ticket.api.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class SeatTest {
	@Test
	public void testConstruction() throws Exception {
		Level l=new Level(1, new BigDecimal(5));
		Seat seat = new Seat(1, 1, 10, Status.AVAILABLE, l);
		assertEquals("id",1, seat.getId());
		assertEquals("level",l, seat.getLevel());
		assertEquals("rowId",1, seat.getRowId());
		assertEquals("rank",10, seat.getRank());
		assertEquals("status",Status.AVAILABLE, seat.getStatus());
	}
	
	@Test
	public void testEquals() throws Exception {
		Level l1=new Level(1, new BigDecimal(5));
		Seat seat1 = new Seat(1, 1, 10, Status.AVAILABLE, l1);
		
		Level l2=new Level(1, new BigDecimal(5));
		Seat seat2 = new Seat(1, 1, 10, Status.AVAILABLE, l2);
		assertTrue("both the seats should be equal",seat1.equals(seat2));
		assertFalse("object references are not equal for both the seats",seat1==seat2);
	}
	
	@Test
	public void testHashCode() throws Exception {
		Level l1=new Level(1, new BigDecimal(5));
		Seat seat1 = new Seat(1, 1, 10, Status.AVAILABLE, l1);
		
		Level l2=new Level(1, new BigDecimal(5));
		Seat seat2 = new Seat(1, 1, 10, Status.AVAILABLE, l2);
		assertTrue("hashCode should be equal for both the seats",seat1.hashCode()==seat2.hashCode());
		assertFalse("object references are not equal for both the seats",seat1==seat2);
	}

}
