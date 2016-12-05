package com.example.ticket.impl.service;

import java.util.UUID;

import com.example.ticket.api.ConfirmationCodeGenerator;
/**
 * This class is an implementation of ConfirmationCodeGenerator interface.
 * It generates unique confirmation code based on java.util.UUID.
 *
 * @author satish
 *
 */
public class SimpleConfirmationCodeGenerator implements ConfirmationCodeGenerator{
	/**
	 * Method to generate unique confirmation code based on java.util.UUID
	 */
	@Override
	public String getNextConfirmationCode() {
		return UUID.randomUUID().toString();
	}

}
