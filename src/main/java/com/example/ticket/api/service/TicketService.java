package com.example.ticket.api.service;

import com.example.ticket.api.models.SeatHold;
import com.example.ticket.impl.service.TicketServiceException;

/**
 * Interface for Ticket Service
 * 
 * @author satish
 *
 */
public interface TicketService {

	/**
	 * The number of seats available those are neither held nor reserved
	 * 
	 * 
	 * @return number of available seats/tickets
	 */
    int numSeatsAvailable();

    /**
     * Find and hold the best available seats for a customer
     * 
     * 
     * @param numSeats 			number of seats to find then hold. Value must be a positive integer.
     * @param customerEmail 	email address of customer. it uniquely identifies a customer.
     * @return a SeatHold object to represent held seats and related information.
     * @throws TicketServiceException 
     */
    SeatHold findAndHoldSeats(int numSeats, String customerEmail);

    /**
     * Reserve and commit seats held for a  customer
     * 
     * 
     * @param seatHoldId 		a unique identifier for seats held
     * @param customerEmail 	email address of customer. it uniquely identifies a customer.
     * @return a confirmation code
     */
    String reserveSeats(int seatHoldId, String customerEmail);

}
 
