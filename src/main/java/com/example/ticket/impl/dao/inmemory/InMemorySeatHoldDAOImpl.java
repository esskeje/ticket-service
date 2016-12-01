package com.example.ticket.impl.dao.inmemory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.ticket.api.dao.SeatHoldDAO;
import com.example.ticket.api.models.SeatHold;

public class InMemorySeatHoldDAOImpl implements SeatHoldDAO{
	Map<SeatHold.SeatHoldKey,SeatHold> allSeatHolds= new HashMap<>();
	
	@Override
	public Collection<SeatHold> findAll() {
		return allSeatHolds.values();
	}

	@Override
	public Collection<SeatHold> findAndRemoveAllExpired(long ttlInSec) {
		Set<SeatHold> expiredHeldSeats = new HashSet<>();
		for (SeatHold heldSeats : allSeatHolds.values()) {
			if(heldSeats.getHoldTime().plusSeconds(ttlInSec).isBefore(LocalDateTime.now())){
				expiredHeldSeats.add(heldSeats);
				allSeatHolds.remove(heldSeats);
			}
		}
		return expiredHeldSeats;
	}
	
	@Override
	public void save(SeatHold seatHold) {
		allSeatHolds.put(seatHold.getSeatHoldKey(),seatHold);
		
	}

	@Override
	public SeatHold findBySeatHoldIdAndCustomerEmail(SeatHold.SeatHoldKey seatHoldKey) {
		return allSeatHolds.get(seatHoldKey);
	}

}
