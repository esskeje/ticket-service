package com.example.ticket.impl.service;
/**
 * Exception class TicketService.
 * It's defined as a subclass of RuntimeException because method signature in TicketService interface does not
 * have any exception defined.
 * Assumption is that we can't change the contract.
 * 
 * @author satish
 *
 */
@SuppressWarnings("serial")
public class TicketServiceException extends RuntimeException {

	/**
	 * Two argument constructor: String and Throwable
	 * 
	 * @param message
	 * @param th
	 */
	public TicketServiceException(String message, Throwable th) {
		super(message, th);
	}

	/**
	 * One argument constructor with String parameter
	 * @param message
	 */
	public TicketServiceException(String message) {
		super(message);
	}

}
