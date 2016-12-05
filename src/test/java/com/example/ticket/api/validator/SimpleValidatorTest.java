package com.example.ticket.api.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SimpleValidatorTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testInvalidCase() throws Exception {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("int is invalid.");
		new SimpleValidator(
				new Field[] { new IntegerField(10, 100, "int", 5), 
						new EmailField(3, 5, "email", "aaa") })
						.validate();
	}
	
	@Test
	public void testValidCase() throws Exception {
		new SimpleValidator(
				new Field[] { new IntegerField(10, 100, "int", 10), 
						new EmailField(3, 5, "email", "a@b") })
						.validate();
	}
}
