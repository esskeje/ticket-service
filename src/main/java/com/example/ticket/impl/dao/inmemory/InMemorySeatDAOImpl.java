package com.example.ticket.impl.dao.inmemory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.dao.DAOException;
import com.example.ticket.api.dao.SeatDAO;
import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.models.Venue;

public class InMemorySeatDAOImpl implements SeatDAO{
	
	private Set<Seat> allSeats=new HashSet<>();
	public InMemorySeatDAOImpl() throws DAOException {
		try {
			Venue venue = VenueBuilder.create();
			for (Level level : venue.getLevels()) {
					for (Seat seat : level.getSeats()) {
						allSeats.add(seat);
					}
			}
		} catch (IOException e) {
			throw new DAOException("Loading of seats failed.",e);
		}
		
	}

	@Override
	public List<Seat> findByStatus(Status status) {
		List<Seat> matchingSeats= new ArrayList<>();
		for (Seat seat : allSeats) {
			if(status==seat.getStatus()){
				matchingSeats.add(seat);
			}
		}
		return matchingSeats;
	}
	
	@Override
	public void save(List<Seat> seats) {
		for (Seat seat : seats) {
			allSeats.remove(seat);
			allSeats.add(seat);
		}
		
	}
	@Override
	public List<Seat> findBestByStatus(Status status,BestSeatStrategy bestStrategy, int numSeats) {
		List<Seat> matchingSeats = findByStatus(status);
		List<Seat> bestSeats=bestStrategy.findBest(matchingSeats, numSeats);
		return bestSeats;
	}

}
