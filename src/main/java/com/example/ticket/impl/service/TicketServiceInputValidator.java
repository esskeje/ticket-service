package com.example.ticket.impl.service;

import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.SeatHold;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.validator.EmailField;
import com.example.ticket.api.validator.Field;
import com.example.ticket.api.validator.IntegerField;
import com.example.ticket.api.validator.SimpleValidator;
import com.example.ticket.api.validator.ValidationException;

public class TicketServiceInputValidator {
	public void validateInputForReserveSeats(int seatHoldId, String customerEmail) {
		try {
			new SimpleValidator(
					new Field[] { getFieldForSeatHoldId(seatHoldId), getFieldForCustomerEmail(customerEmail) })
							.validate();
		} catch (ValidationException e) {
			throw new TicketServiceException(
					"TicketService.reserveSeats() failed. Invalid Input. Cause: " + e.getMessage());
		}
	}

	public void validateInputForFindAndHoldSeats(int numSeats, String customerEmail) {
		try {
			new SimpleValidator(new Field[] { getFieldForNumSeats(numSeats), getFieldForCustomerEmail(customerEmail) })
					.validate();
		} catch (ValidationException e) {
			throw new TicketServiceException(
					"TicketService.findAndHoldSeats() failed. Invalid Input. Cause: " + e.getMessage());
		}
	}

	private EmailField getFieldForCustomerEmail(String customerEmail) {
		// assuming min 5=a@b.c..max as 50
		return new EmailField(5, 50, "customerEmail", customerEmail);
	}

	private IntegerField getFieldForNumSeats(int numSeats) {
		// assuming min =1, max=500.
		// TODO max seats are hard coded for now but can be read from config
		return new IntegerField(1, 500, "numSeats", numSeats);
	}

	private IntegerField getFieldForSeatHoldId(int seatHoldId) {
		// assuming min 1= max as max int value
		return new IntegerField(1, Integer.MAX_VALUE, "seatHoldId", seatHoldId);
	}

	public void validateSeatHoldIsNotNull(SeatHold.SeatHoldKey seatHoldKey, SeatHold seatHold)
			throws TicketServiceException {
		if (seatHold == null) {
			throw new TicketServiceException("Seat Hold doesn't exist for customerEmail:"
					+ seatHoldKey.getCustomerEmail() + " and seatHoldId=" + seatHoldKey.getSeatHoldId());
		}
	}

	public void validateRequestedNumberOfSeatsHasBeenHeld(int size, int numSeats) {
		if (size != numSeats) {
			throw new TicketServiceException("Seats are not available : " + numSeats);
		}
	}

	public void validateAllReservedSeatsHaveBeenInHoldBefore(SeatHold seatHold) {
		for (Seat seat : seatHold.getHeldSeats()) {
			if (seat.getStatus() != Status.HELD) {
				throw new TicketServiceException(
						"TicketService.reserveSeats() failed. Cause: Some of the seats those were on hold have expired, please restart the ticket booking process.");

			}
		}
	}

}
