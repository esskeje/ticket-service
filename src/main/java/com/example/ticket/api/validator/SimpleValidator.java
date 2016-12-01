package com.example.ticket.api.validator;

public class SimpleValidator implements Validator {
	protected Field[] inputFields;

	public SimpleValidator(Field[] inputFields) {
		this.inputFields = inputFields;
	}

	@Override
	public void validate() throws ValidationException {
		for (Field field : inputFields) {
			field.validate();
		}
	}

}
