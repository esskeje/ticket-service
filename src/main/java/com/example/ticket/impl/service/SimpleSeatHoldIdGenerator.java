package com.example.ticket.impl.service;

import java.util.concurrent.atomic.AtomicInteger;

import com.example.ticket.api.SeatHoldIdGenerator;
/**This is a simple implementation of SeatHoldId generator.
 * It will work in one instance of a application
 * if there are more than one instances then it will not guarantee the
 * uniqueness.
 * 
 * @author satish
 *
 */
public class SimpleSeatHoldIdGenerator implements SeatHoldIdGenerator{
	private AtomicInteger id=new AtomicInteger(0);
	
	public int getNextSeatHoldId() {
		return id.incrementAndGet();
	}
}
