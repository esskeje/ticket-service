package com.example.ticket.impl.dao.inmemory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.Config;
import com.example.ticket.api.dao.SeatDAO;
import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.models.Venue;
/**
 * This class implements in-memory version of SeatDAO.
 * 
 * @author satish
 *
 */
public class InMemorySeatDAOImpl implements SeatDAO{
	private Set<Seat> allSeats=new HashSet<>();
	
	/**
	 * Constructor.
	 * It loads venue details from Venue object set in config into a list of seats.
	 * 
	 * @param config
	 */
	public InMemorySeatDAOImpl(Config config){
		if(config==null){
			throw new IllegalArgumentException("'config' must not be null.");
		}
		if(config.getVenue()==null){
			throw new IllegalArgumentException("'config.getVenue()' must not be null.");
		}
		load(config.getVenue());
	}
	/**
	 * loads venue details from Venue object set in config into a list of seats.
	 * @param venue
	 */
	private void load(Venue venue) {
		for (Level level : venue.getLevels()) {
			for (Seat seat : level.getSeats()) {
				allSeats.add(seat);
			}
		}
	}

	/**
	 * Find list of seats for a given status
	 */
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
	
	/**
	 * Save the supplied seats.
	 * if there is an existing seat already, remove that first then add
	 */
	@Override
	public void save(List<Seat> seats) {
		for (Seat seat : seats) {
			allSeats.remove(seat);
			allSeats.add(seat);
		}
		
	}
	
	/**
	 * Find given number of best seats based on given BestSeatStrategy for a given status
	 */
	@Override
	public List<Seat> findBestByStatus(Status status,BestSeatStrategy bestStrategy, int numSeats) {
		List<Seat> matchingSeats = findByStatus(status);
		List<Seat> bestSeats=bestStrategy.findBest(matchingSeats, numSeats);
		return bestSeats;
	}
	
	/**
	 * Added for unit testing
	 * @return all seats
	 */
	Set<Seat> getAllSeats() {
		return allSeats;
	}

}
