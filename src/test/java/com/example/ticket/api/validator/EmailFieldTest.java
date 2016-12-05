package com.example.ticket.api.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EmailFieldTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testValidCaseMin() throws Exception {
		new EmailField(4, 5, "email", "a@b_").validate();
	}
	@Test
	public void testValidCaseMax() throws Exception {
		new EmailField(4, 5, "email", "a@b.c").validate();
	}
	@Test
	public void testInvalidCaseMin() throws Exception {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("email is invalid.");
		new EmailField(4, 5, "email", "a@b").validate();
	}
	@Test
	public void testInvalidCaseMax() throws Exception {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("email is invalid.");
		new EmailField(4, 5, "email", "a@b.cd").validate();
	}
	@Test
	public void testInvalidEmail() throws Exception {
		thrown.expect(ValidationException.class);
		thrown.expectMessage("email is invalid.");
		new EmailField(4, 5, "email", "aaaa").validate();
	}

}
