package com.example.ticket.impl.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.ConfirmationCodeGenerator;

public class SimpleConfirmationCodeGeneratorTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testGetNextConfirmationCode() throws Exception {
		ConfirmationCodeGenerator gen= new SimpleConfirmationCodeGenerator();
		String nextConfirmationCode1 = gen.getNextConfirmationCode();
		assertNotNull(nextConfirmationCode1);
		String nextConfirmationCode2 = gen.getNextConfirmationCode();
		assertNotNull(nextConfirmationCode1);
		assertFalse("confirmation should be unique",nextConfirmationCode1.equals(nextConfirmationCode2));
	}

}
