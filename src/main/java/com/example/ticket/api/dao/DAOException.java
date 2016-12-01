package com.example.ticket.api.dao;

@SuppressWarnings("serial")
public class DAOException extends Exception {

	public DAOException(String message) {
		super(message);
	}

	public DAOException(String message, Throwable th) {
		super(message,th);
	}

}
