package com.example.ticket.api.validator;

/**
 * Interface to represent a field that needs to be validated.
 * 
 * @author satish
 *
 */
public interface Field {
	/**
	 * if validation is un-successful throw exception.
	 * @throws ValidationException
	 */
	void validate() throws ValidationException;
}
