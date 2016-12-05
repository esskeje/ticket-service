package com.example.ticket.api.validator;

/**
 *Interface/contract to implement validator for
 *fields based on data type or length.
 * @author satish
 *
 */
public interface Validator {
	/**
	 * If validation is un-successful throw validationException
	 * otherwise continue.
	 * 
	 * @throws ValidationException
	 */
	void validate() throws ValidationException;
}