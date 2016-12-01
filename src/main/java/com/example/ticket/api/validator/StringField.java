package com.example.ticket.api.validator;

public class StringField implements Field {
	protected int minLength;
	protected int maxLength;
	protected String fieldValue;
	protected String fieldName;

	public StringField(int minLength, int maxLength, String fieldName, String fieldValue) {
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Override
	public void validate() throws ValidationException {
		if (fieldValue==null || (fieldValue.length() < minLength || fieldValue.length() > maxLength)) {
			throw new ValidationException(fieldName + " is invalid.");
		}
	}

}