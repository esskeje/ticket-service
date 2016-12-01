package com.example.ticket.api.validator;

public class IntegerField implements Field {
	private int minValue;
	private int maxValue;
	private int fieldValue;
	private String fieldName;

	public IntegerField(int minValue, int maxValue, String fieldName, int fieldValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.fieldValue = fieldValue;
		this.fieldName = fieldName;
	}

	@Override
	public void validate() throws ValidationException {
		if (fieldValue < minValue || fieldValue > maxValue) {
			throw new ValidationException(fieldName + " is invalid.");
		}
	}

}