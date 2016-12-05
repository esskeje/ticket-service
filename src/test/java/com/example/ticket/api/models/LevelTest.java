package com.example.ticket.api.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LevelTest {
	@Test
	public void testConstruction() throws Exception {
		Level l=new Level(1, new BigDecimal(5));
		assertEquals(1, l.getId());
		assertEquals(new BigDecimal(5), l.getPrice());
	}
	@Test
	public void testSetAndGetSeats() throws Exception {
		Level l=new Level(1, new BigDecimal(5));
		List<Seat> seats = new ArrayList<>();
		seats.add(new Seat(1, 1, 10, Status.AVAILABLE, l));
		seats.add(new Seat(20, 2, 10, Status.AVAILABLE, l));
		l.setSeats(seats);
		assertEquals(seats.size(), l.getSeats().size());
		assertEquals(seats, l.getSeats());
	}
	@Test
	public void testEquals() throws Exception {
		Level l1=new Level(1, new BigDecimal(5));
		Level l2=new Level(1, new BigDecimal(5));
		List<Seat> seats = new ArrayList<>();
		seats.add(new Seat(1, 1, 10, Status.AVAILABLE, l1));
		seats.add(new Seat(1, 1, 10, Status.AVAILABLE, l2));
		l1.setSeats(seats);
		l2.setSeats(seats);
		assertTrue(l1.equals(l2));
		assertFalse(l1==l2);
	}
	
	public void testHashCode() throws Exception {
		Level l1=new Level(1, new BigDecimal(5));
		Level l2=new Level(1, new BigDecimal(5));
		List<Seat> seats = new ArrayList<>();
		seats.add(new Seat(1, 1, 10, Status.AVAILABLE, l1));
		seats.add(new Seat(1, 1, 10, Status.AVAILABLE, l2));
		l1.setSeats(seats);
		l2.setSeats(seats);
		assertTrue(l1.hashCode()==l2.hashCode());
		assertFalse(l1==l2);
	}
	
	

}
