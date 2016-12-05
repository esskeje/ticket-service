package com.example.ticket.impl.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.SeatHold;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.validator.EmailField;
import com.example.ticket.api.validator.IntegerField;

public class TicketServiceInputValidatorTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testValidateInputForReserveSeatsWhenValuesAreAtMinBoundary(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		Exception ex=null;
		try {
			inputValidator.validateInputForReserveSeats(1, "a@b.c");
		} catch (Exception e) {
			ex=e;
		}
		assertNull("exception should not occur", ex);
	}
	
	@Test
	public void testValidateInputForReserveSeatsWhenValuesAreAtMaxBoundary() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		Exception ex=null;
		try {
			inputValidator.validateInputForReserveSeats(Integer.MAX_VALUE, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@abc.com");
		} catch (Exception e) {
			ex=e;
		}
		assertNull("exception should not occur", ex);
	}
	
	@Test
	public void testValidateInputForReserveSeatsWhenSeatHoldIdIsZero(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Invalid Input. Cause: seatHoldId is invalid");
		inputValidator.validateInputForReserveSeats(0, "a@b.c");
	}
	
	@Test
	public void testValidateInputForReserveSeatsWhenSeatHoldIdIsMoreThanMaxIntValue(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Invalid Input. Cause: seatHoldId is invalid");
		inputValidator.validateInputForReserveSeats(Integer.MAX_VALUE+1, "a@b.c");
	}
	
	@Test
	public void testValidateInputForReserveSeatsWhenCustomerEmailHasLessThan5Characters(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Invalid Input. Cause: customerEmail is invalid");
		inputValidator.validateInputForReserveSeats(Integer.MAX_VALUE, "a@c");
	}
	
	@Test
	public void testValidateInputForReserveSeatsWhenCustomerEmailHasNoAtCharacter(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Invalid Input. Cause: customerEmail is invalid");
		inputValidator.validateInputForReserveSeats(5, "aaa.abc,com");
	}
	
	@Test
	public void testValidateInputForReserveSeatsWhenCustomerEmailHasMoreThan50Characters(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Invalid Input. Cause: customerEmail is invalid");
		inputValidator.validateInputForReserveSeats(2, "baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@abc.com");
	}
	
	@Test
	public void testValidateInputForReserveSeatsWhenCustomerEmailIsNull(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Invalid Input. Cause: customerEmail is invalid");
		inputValidator.validateInputForReserveSeats(Integer.MAX_VALUE-1, null);
	}
	
	@Test
	public void testValidateInputForFindAndHoldSeatsWhenValuesAreAtMinBoundary(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		Exception ex=null;
		try {
			inputValidator.validateInputForFindAndHoldSeats(1, "a@b.c");
		} catch (Exception e) {
			ex=e;
		}
		assertNull("exception should not occur", ex);
	}
	
	@Test
	public void testValidateInputForFindAndHoldSeatsWhenValuesAreAtMaxBoundary(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		Exception ex=null;
		try {
			inputValidator.validateInputForFindAndHoldSeats(500, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@abc.com");
		} catch (Exception e) {
			ex=e;
		}
		assertNull("exception should not occur", ex);
	}
	
	@Test
	public void testValidateInputForFindAndHoldSeatsWhenNumSeatsIsZero(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: numSeats is invalid");
		inputValidator.validateInputForFindAndHoldSeats(0, "a@b.c");
	}
	
	@Test
	public void testValidateInputForFindAndHoldSeatsWhenNumSeatsIsMoreThanMaxIntValue(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: numSeats is invalid");
		inputValidator.validateInputForFindAndHoldSeats(501, "a@b.c");
	}
	
	@Test
	public void testValidateInputForFindAndHoldSeatsWhenCustomerEmailHasLessThan5Characters(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: customerEmail is invalid");
		inputValidator.validateInputForFindAndHoldSeats(500, "a@c");
	}
	
	@Test
	public void testValidateInputForFindAndHoldSeatsWhenCustomerEmailHasNoAtCharacter(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: customerEmail is invalid");
		inputValidator.validateInputForFindAndHoldSeats(5, "aaa.abc,com");
	}
	
	@Test
	public void testValidateInputForFindAndHoldSeatsWhenCustomerEmailHasMoreThan50Characters(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: customerEmail is invalid");
		inputValidator.validateInputForFindAndHoldSeats(2, "baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@abc.com");
	}
	
	@Test
	public void testValidateInputForFindAndHoldSeatsWhenCustomerEmailIsNull(){
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.findAndHoldSeats() failed. Invalid Input. Cause: customerEmail is invalid");
		inputValidator.validateInputForFindAndHoldSeats(499, null);
	}
	
	@Test
	public void testGetFieldForCustomerEmail() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		EmailField emailField = inputValidator.getFieldForCustomerEmail("a@b.c");
		assertEquals("customerEmail field name","customerEmail", emailField.getFieldName());
		assertEquals("customerEmail field value","a@b.c", emailField.getFieldValue());
		assertEquals("customerEmail min length",5, emailField.getMinLength());
		assertEquals("customerEmail max length",50, emailField.getMaxLength());
	}
	
	@Test
	public void testGetFieldForSeatHoldId() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		IntegerField field = inputValidator.getFieldForSeatHoldId(2);
		assertEquals("seatHoldId field name","seatHoldId", field.getFieldName());
		assertEquals("seatHoldId field value",2, field.getFieldValue());
		assertEquals("seatHoldId min value",1, field.getMinValue());
		assertEquals("seatHoldId max value",Integer.MAX_VALUE, field.getMaxValue());
	}
	
	@Test
	public void testGetFieldForNumSeats() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		IntegerField field = inputValidator.getFieldForNumSeats(2);
		assertEquals("numSeats field name","numSeats", field.getFieldName());
		assertEquals("numSeats field value",2, field.getFieldValue());
		assertEquals("numSeats min value",1, field.getMinValue());
		assertEquals("numSeats max value",500, field.getMaxValue());
	}
	
	@Test
	public void testValidateSeatHoldIsNotNullWhenSeatHoldIsNotNull() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		SeatHold sh= new SeatHold(1, "a@b.c", null);
		Exception ex=null;
		try {
			inputValidator.validateSeatHoldIsNotNull(sh.getSeatHoldKey(),sh);
		} catch (Exception e) {
			ex=e;
		}
		assertNull("exception should not occur", ex);
	}
	
	@Test
	public void testValidateSeatHoldIsNotNullWhenSeatHoldIsNull() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		SeatHold sh= new SeatHold(1, "a@b.c", null);
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("Seat Hold doesn't exist for customerEmail="
				+ sh.getSeatHoldKey().getCustomerEmail() + " and seatHoldId=" +  sh.getSeatHoldKey().getSeatHoldId());
		inputValidator.validateSeatHoldIsNotNull(sh.getSeatHoldKey(),null);
	}
	
	@Test
	public void testValidateRequestedNumberOfSeatsHasBeenHeldWhenThereIsMatch() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		Exception ex=null;
		try {
			inputValidator.validateRequestedNumberOfSeatsHasBeenHeld(2,2);
		} catch (Exception e) {
			ex=e;
		}
		assertNull("exception should not occur", ex);
	}
	
	@Test
	public void testValidateRequestedNumberOfSeatsHasBeenHeldWhenThereIsMisMatch() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("Enough seats are not available : 2");
		inputValidator.validateRequestedNumberOfSeatsHasBeenHeld(3,2);
	}
	
	@Test
	public void testValidateAllReservedSeatsHaveBeenInHoldBeforeWhenNoneOfTheHeldSeatsHasExpired() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		Exception ex=null;
		try {
			Level level = new Level(2, new BigDecimal(5));
			List<Seat> seats= new ArrayList<>();
			level.setSeats(seats);
			seats.add(new Seat(1,1,10,Status.HELD,level));
			seats.add(new Seat(2,2,10,Status.HELD,level));
			SeatHold seatHold= new SeatHold(1, "aaa@abc.com", seats);
			inputValidator.validateAllReservedSeatsHaveBeenInHoldBefore(seatHold);
		} catch (Exception e) {
			ex=e;
		}
		assertNull("exception should not occur", ex);
	}
	
	@Test
	public void testValidateAllReservedSeatsHaveBeenInHoldBeforeWhenHeldSeatsIsNull() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Cause: There are no held seats for the supplied seat hold, please restart the ticket booking process.");
		SeatHold seatHold= new SeatHold(1, "aaa@abc.com", null);
		inputValidator.validateAllReservedSeatsHaveBeenInHoldBefore(seatHold);
	}
	
	@Test
	public void testValidateAllReservedSeatsHaveBeenInHoldBeforeWhenHeldSeatsIsEmpty() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Cause: There are no held seats for the supplied seat hold, please restart the ticket booking process.");
		SeatHold seatHold= new SeatHold(1, "aaa@abc.com",  new ArrayList<>());
		inputValidator.validateAllReservedSeatsHaveBeenInHoldBefore(seatHold);
	}
	
	@Test
	public void testValidateAllReservedSeatsHaveBeenInHoldBeforeWhenSomeOfTheHeldSeatsHasExpired() {
		TicketServiceInputValidator inputValidator = new TicketServiceInputValidator();
		thrown.expect(TicketServiceException.class);
		thrown.expectMessage("TicketService.reserveSeats() failed. Cause: Some of the seats those were on hold have expired, please restart the ticket booking process.");
		Level level = new Level(2, new BigDecimal(5));
		List<Seat> seats= new ArrayList<>();
		level.setSeats(seats);
		seats.add(new Seat(1,1,10,Status.HELD,level));
		seats.add(new Seat(2,2,10,Status.AVAILABLE,level));
		
		SeatHold seatHold= new SeatHold(1, "aaa@abc.com", seats);
		inputValidator.validateAllReservedSeatsHaveBeenInHoldBefore(seatHold);
	}

}
