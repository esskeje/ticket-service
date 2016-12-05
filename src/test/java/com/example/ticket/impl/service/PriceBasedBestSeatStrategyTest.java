package com.example.ticket.impl.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;

public class PriceBasedBestSeatStrategyTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFindBest() {
		BestSeatStrategy s = new PriceBasedBestSeatStrategy();
		List<Seat> seats1 = getSeats(1, new BigDecimal(8), 1, 2, 3);
		List<Seat> seats2 = getSeats(1, new BigDecimal(10), 4, 5, 6);
		List<Seat> seats3 = getSeats(1, new BigDecimal(5), 7, 8, 9);
		seats1.addAll(seats2);
		seats1.addAll(seats3);
		List<Seat> best = s.findBest(seats1, 4);
		assertEquals(4, best.get(0).getId());
		assertEquals(5, best.get(1).getId());
		assertEquals(6, best.get(2).getId());
		assertEquals(1, best.get(3).getId());

	}

	@Test
	public void testFindBestWhenCountIsMoreThanSizeOfSeats() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("count must be less or equal to 3");
		BestSeatStrategy s = new PriceBasedBestSeatStrategy();
		List<Seat> seats1 = getSeats(1, new BigDecimal(8), 1, 2, 3);
		List<Seat> best = s.findBest(seats1, 4);
	}

	@Test
	public void testFindBestWhenSeatsIsNull() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("seats must not be null");
		BestSeatStrategy s = new PriceBasedBestSeatStrategy();
		List<Seat> best = s.findBest(null, 4);

	}

	// -----utility methods
	private List<Seat> getSeats(int levelId, BigDecimal price, int i, int j, int k) {
		Level level = new Level(levelId, price);
		List<Seat> seats = new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(i, 1, 10, Status.AVAILABLE, level));
		seats.add(new Seat(j, 2, 10, Status.AVAILABLE, level));
		seats.add(new Seat(k, 2, 10, Status.AVAILABLE, level));
		return seats;
	}

}
