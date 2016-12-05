package com.example.ticket.impl.service;

import java.util.Collection;

import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;

public class TicketServiceUtil {

	/**
	 * Utility method. Updates the status of every seat in supplied seats to
	 * supplied status
	 * 
	 * @param seats
	 * @param status
	 */
	public static void updateSeatStatus(Collection<Seat> seats, Status status) {
		if (seats != null) {
			for (Seat seat : seats) {
				seat.setStatus(status);
			}
		}
	}

	/**
	 * Utility method to do null check for objects. if obj is null it throws
	 * IllegalArgumentException
	 * 
	 * @param obj
	 * @param param
	 */
	public static void nullCheck(Object obj, String param) {
		if (obj == null) {
			throw new IllegalArgumentException("please supply " + param + ".");
		}
	}

}
