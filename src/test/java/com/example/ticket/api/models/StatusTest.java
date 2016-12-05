package com.example.ticket.api.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StatusTest {

	@Test
	public void testStatus() {
		assertEquals("Status.values()",3, Status.values().length);
		assertEquals("Status.AVAILABLE",Status.AVAILABLE, Status.values()[0]);
		assertEquals("Status.HELD)",Status.HELD, Status.values()[1]);
		assertEquals("Status.COMMITED",Status.COMMITED, Status.values()[2]);
	}

}
