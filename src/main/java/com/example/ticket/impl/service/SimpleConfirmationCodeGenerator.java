package com.example.ticket.impl.service;

import java.util.UUID;

import com.example.ticket.api.ConfirmationCodeGenerator;

public class SimpleConfirmationCodeGenerator implements ConfirmationCodeGenerator{
	
	public String getNextConfirmationCode() {
		return UUID.randomUUID().toString();
	}

}
