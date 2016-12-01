package com.example.ticket.api.validator;

public class EmailField extends StringField {

	public EmailField(int minLength, int maxLength, String fieldName, String fieldValue) {
		super(minLength, maxLength, fieldName, fieldValue);
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (fieldValue != null) {
			String[] split = fieldValue.split("@");
			if (split.length != 2) {
				throw new ValidationException(fieldName + " is invalid.");
			}
		}
	}

}