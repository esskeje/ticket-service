package com.example.ticket.impl.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;

public class TicketServiceUtilTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testUpdateSeatStatusWhenSeatsAreNonNull() throws Exception {
		List<Seat> heldSeats =getHeldSeats();
		TicketServiceUtil.updateSeatStatus(heldSeats, Status.AVAILABLE);
		for (Seat seat : heldSeats) {
			assertEquals(Status.AVAILABLE, seat.getStatus());
		}
	}
	
	public void testUpdateSeatStatusWhenSeatsAreNull() throws Exception {
		TicketServiceUtil.updateSeatStatus(null, Status.AVAILABLE);
	}

	@Test
	public void testNullCheckWhenValueIsNull() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("please supply p1.");
		TicketServiceUtil.nullCheck(null, "p1");
	}
	
	@Test
	public void testNullCheckWhenValueIsNonNull() throws Exception {
		TicketServiceUtil.nullCheck(new Object(), "p1");
	}
	
	private List<Seat> getHeldSeats() {
		Level level = new Level(2, new BigDecimal(8));
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(3, 3, 10, Status.HELD, level));
		seats.add(new Seat(4, 3, 10, Status.HELD, level));
		return seats;
	}

}
