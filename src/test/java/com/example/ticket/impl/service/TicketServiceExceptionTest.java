package com.example.ticket.impl.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TicketServiceExceptionTest {
	@Test
	public void testTicketServiceExceptionConstrctionForOneArgument() throws Exception {
		TicketServiceException ex= new TicketServiceException("message");
		assertEquals("message", ex.getMessage());
	}
	
	@Test
	public void testTicketServiceExceptionConstrctionForTwoArguments() throws Exception {
		Throwable throwable = new Throwable("throwable");
		TicketServiceException ex= new TicketServiceException("message",throwable );
		assertEquals("message", ex.getMessage());
		assertEquals(throwable, ex.getCause());
	}

}
