package com.example.ticket.api;

/**
 * Interface/contract to generate unique confirmation code.
 * 
 * @author satish
 *
 */
public interface ConfirmationCodeGenerator {
	/**
	 * 
	 * @return unique confirmation code
	 */
	public String getNextConfirmationCode();
}
