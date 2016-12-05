package com.example.ticket.api.dao;

/**
 * Exception for DAO Layer.
 * 
 * @author satish
 *
 */
@SuppressWarnings("serial")
public class DAOException extends Exception {

	public DAOException(String message) {
		super(message);
	}

	public DAOException(String message, Throwable th) {
		super(message,th);
	}

}
