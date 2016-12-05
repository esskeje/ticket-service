package com.example.ticket.api.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IntegerFieldTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testValidCaseMin() throws Exception {
		new IntegerField(10, 100, "int", 10).validate();
	}
	@Test
	public void testValidCaseMax() throws Exception {
		new IntegerField(10, 100, "int", 100).validate();
	}
	@Test
	public void testInvalidCaseMin() throws Exception {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("int is invalid.");
		new IntegerField(10, 100, "int", 9).validate();
	}
	@Test
	public void testInvalidCaseMax() throws Exception {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("int is invalid.");
		new IntegerField(10, 100, "int", 101).validate();
	}

}
