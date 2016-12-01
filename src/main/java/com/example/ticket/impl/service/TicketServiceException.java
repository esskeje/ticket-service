package com.example.ticket.impl.service;

@SuppressWarnings("serial")
public class TicketServiceException extends RuntimeException {

	public TicketServiceException(String message, Throwable th) {
		super(message, th);
	}

	public TicketServiceException(String message) {
		super(message);
	}

}
