package com.example.ticket.impl.service;

import java.util.List;

import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.SeatHold;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.validator.EmailField;
import com.example.ticket.api.validator.Field;
import com.example.ticket.api.validator.IntegerField;
import com.example.ticket.api.validator.SimpleValidator;
import com.example.ticket.api.validator.ValidationException;

/**
 * This class does the input parameter validation for TicketService.
 * It's kind of a validation utility/helper class.
 * It has coupling with TicketService. All the methods in this class
 * has default scope.
 * 
 * @author satish
 *
 */
public class TicketServiceInputValidator{
	
	/**
	 * validate input for reserveSeats.
	 * particularly validates - seatHoldId and customerEmail.
	 * if validation fails, it throws TicketServiceException
	 * @param seatHoldId
	 * @param customerEmail
	 */
	void validateInputForReserveSeats(int seatHoldId, String customerEmail) {
		try {
			new SimpleValidator(
					new Field[] { getFieldForSeatHoldId(seatHoldId), getFieldForCustomerEmail(customerEmail) })
							.validate();
		} catch (ValidationException e) {
			throw new TicketServiceException(
					"TicketService.reserveSeats() failed. Invalid Input. Cause: " + e.getMessage());
		}
	}

	/**
	 * This validate input for findAndHoldSeats.
	 * particularly validates - numSeats and customerEmail.
	 * if validation fails, it throws TicketServiceException
	 * 
	 * @param numSeats
	 * @param customerEmail
	 */
	void validateInputForFindAndHoldSeats(int numSeats, String customerEmail) {
		try {
			new SimpleValidator(new Field[] { getFieldForNumSeats(numSeats), getFieldForCustomerEmail(customerEmail) })
					.validate();
		} catch (ValidationException e) {
			throw new TicketServiceException(
					"TicketService.findAndHoldSeats() failed. Invalid Input. Cause: " + e.getMessage());
		}
	}
	
	/**
	 * Returns customerEmail field for input validation
	 * @param customerEmail
	 * @return EmailField
	 */
	EmailField getFieldForCustomerEmail(String customerEmail) {
		// assuming min 5=a@b.c..max as 50
		return new EmailField(5, 50, "customerEmail", customerEmail);
	}

	/**
	 * Returns numSeats field for input validation
	 * 
	 * @param numSeats
	 * @return IntegerField
	 */
	IntegerField getFieldForNumSeats(int numSeats) {
		// assuming min =1, max=500.
		// TODO max seats are hard coded for now but can be read from config
		return new IntegerField(1, 500, "numSeats", numSeats);
	}

	/**
	 * Returns seatHoldId field for input validation
	 * @param seatHoldId
	 * @return	IntegerField
	 */
	IntegerField getFieldForSeatHoldId(int seatHoldId) {
		// assuming min 1= max as max int value
		return new IntegerField(1, Integer.MAX_VALUE, "seatHoldId", seatHoldId);
	}

	/**
	 * Validates the supplied seatHold is not null.
	 * Otherwise throws TicketServiceException
	 * 
	 * @param seatHoldKey
	 * @param seatHold
	 */
	void validateSeatHoldIsNotNull(SeatHold.SeatHoldKey seatHoldKey, SeatHold seatHold){
		if (seatHold == null) {
			throw new TicketServiceException("Seat Hold doesn't exist for customerEmail="
					+ seatHoldKey.getCustomerEmail() + " and seatHoldId=" + seatHoldKey.getSeatHoldId());
		}
	}

	/**
	 * Validates the requested seats has been held if not throws
	 * TicketServiceException.
	 * 
	 * @param size
	 * @param numSeats
	 */
	void validateRequestedNumberOfSeatsHasBeenHeld(int size, int numSeats) {
		if (size != numSeats) {
			throw new TicketServiceException("Enough seats are not available : " + numSeats);
		}
	}

	/**
	 * Validates that all the seats which are being reserved are in HELD status.
	 * if not if not throws TicketServiceException.
	 * also if there are no held seats it will throw TicketServiceException
	 * @param seatHold
	 */
	void validateAllReservedSeatsHaveBeenInHoldBefore(SeatHold seatHold) {
		//usually it should not happen, to guard against any system error
		//better to have null check
		List<Seat> heldSeats = seatHold.getHeldSeats();
		if(heldSeats==null || heldSeats.size()==0){
			throw new TicketServiceException(
					"TicketService.reserveSeats() failed. Cause: There are no held seats for the supplied seat hold, please restart the ticket booking process.");
		}
		
		//check if all the held seats has status as HELD
		for (Seat seat : heldSeats) {
			if (seat.getStatus() != Status.HELD) {
				throw new TicketServiceException(
						"TicketService.reserveSeats() failed. Cause: Some of the seats those were on hold have expired, please restart the ticket booking process.");

			}
		}
	}

}
