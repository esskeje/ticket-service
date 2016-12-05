package com.example.ticket.api.validator;
/**
 * Its a a simple validator class.
 * It iterates through all the fields and individually validates each field.
 * if either of a field validation fails, the complete validation fails 
 * (fail fast approach)
 * 
 * @author satish
 *
 */
public class SimpleValidator implements Validator {
	protected Field[] fields;

	/**
	 * Constructor.
	 * supply list of fields
	 * 
	 * @param fields
	 */
	public SimpleValidator(Field[] fields) {
		this.fields = fields;
	}

	/**
	 * call validate on each field iteratively.
	 */
	@Override
	public void validate() throws ValidationException {
		for (Field field : fields) {
			field.validate();
		}
	}

}
