package com.example.ticket.impl.dao.inmemory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.ticket.api.Config;
import com.example.ticket.api.Constants;
import com.example.ticket.api.dao.SeatHoldDAO;
import com.example.ticket.api.models.SeatHold;
/**
 * This class implements in-memory version of SeatHoldDAO.
 * 
 * @author satish
 *
 */
public class InMemorySeatHoldDAOImpl implements SeatHoldDAO{
	private Map<SeatHold.SeatHoldKey,SeatHold> allSeatHolds= new HashMap<>();
	private int ttlInSec;
	/**
	 * Constructor
	 * 
	 * @param config
	 */
	InMemorySeatHoldDAOImpl(Config config){
		this.ttlInSec = config.getIntegerProperty(Constants.HOLD_EXPIRY_TIME, Constants.HOLD_EXPIRY_TIME_DEFAULT_VALUE);
	}
	
	/**
	 * Find all the seat holds
	 */
	@Override
	public Collection<SeatHold> findAll() {
		return allSeatHolds.values();
	}

	/**
	 * Find all the un-expired seat holds and also remove the expired ones.
	 */
	@Override
	public Collection<SeatHold> findAllButRemoveExpired() {
		Set<SeatHold> expiredHeldSeats = new HashSet<>();
		for (SeatHold heldSeats : allSeatHolds.values()) {
			if(heldSeats.getHoldTime().plusSeconds(ttlInSec).isBefore(LocalDateTime.now())){
				expiredHeldSeats.add(heldSeats);
				allSeatHolds.remove(heldSeats);
			}
		}
		return expiredHeldSeats;
	}
	
	/**
	 * Save the supplied seatHold
	 */
	@Override
	public void save(SeatHold seatHold) {
		allSeatHolds.put(seatHold.getSeatHoldKey(),seatHold);
		
	}

	/**
	 * Find seatHold by SeatHoldKey e.e. seatHoldId and customerEmail
	 */
	@Override
	public SeatHold findBySeatHoldIdAndCustomerEmail(SeatHold.SeatHoldKey seatHoldKey) {
		return allSeatHolds.get(seatHoldKey);
	}
	
	//for testing
	Map<SeatHold.SeatHoldKey,SeatHold> getAllSeatHolds(){
		return allSeatHolds;
	}

}
