package com.example.ticket.api.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringFieldTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testValidCaseMin() throws Exception {
		new StringField(4, 5, "email", "a@b_").validate();
	}
	@Test
	public void testValidCaseMax() throws Exception {
		new StringField(4, 5, "email", "a@b.c").validate();
	}
	@Test
	public void testInvalidCaseMin() throws Exception {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("email is invalid.");
		new StringField(4, 5, "email", "a@b").validate();
	}
	@Test
	public void testInvalidCaseMax() throws Exception {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("email is invalid.");
		new StringField(4, 5, "email", "a@b.cd").validate();
	}
}
