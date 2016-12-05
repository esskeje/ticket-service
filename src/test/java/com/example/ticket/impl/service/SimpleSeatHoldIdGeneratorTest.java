package com.example.ticket.impl.service;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.SeatHoldIdGenerator;

public class SimpleSeatHoldIdGeneratorTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testGetNextSeatHoldId() throws Exception {
		SeatHoldIdGenerator gen= new SimpleSeatHoldIdGenerator();
		int seatHoldId1 = gen.getNextSeatHoldId();
		assertEquals(1,seatHoldId1);
		int seatHoldId2 = gen.getNextSeatHoldId();
		assertEquals(2,seatHoldId2);
	}
}
