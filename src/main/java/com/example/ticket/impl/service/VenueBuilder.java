package com.example.ticket.impl.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.ticket.api.models.Level;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.models.Venue;

public class VenueBuilder {
	public static Venue create() throws IOException {
		Set<Level> levels = new HashSet<>();
		Venue venue = new Venue();
		
		int seatCount=1;
		int rank=100;//TODO rank can be calculated based on various factor then seats can be ranked based on it
		int numSeatsPerRow=10;
		BigDecimal price= new BigDecimal(50);
		//right now it's unused
		for(int levelCount=1;levelCount<=3;levelCount++){
			Level level= new Level(levelCount,price);
			levels.add(level);
			List<Seat> seats= new ArrayList<>();
			level.setSeats(seats);
			price.subtract(new BigDecimal(5));
			for(int rowCount=1;rowCount<=3;rowCount++){
				for(int seatsPerRowCount=1;seatsPerRowCount<=numSeatsPerRow;seatsPerRowCount++){
					Seat seat= new Seat(seatCount, rowCount, rank, Status.AVAILABLE,level);
					seats.add(seat);
					seatCount++;
				}
			}
		}
		venue.setLevels(levels);
		return venue;
	}

	public static void main(String[] args) {
		try {

			Venue venue = VenueBuilder.create();
			//System.out.println(venue);

			Set<Seat> allSeats = new HashSet<>();
			for (Level level : venue.getLevels()) {
				System.out.println("level " + level.getId());
				System.out.println("#seats in level " + level.getSeats().size());
					for (Seat seat : level.getSeats()) {
						
						allSeats.add(seat);
				}
			}
			System.out.println(allSeats.size());
			System.out.println(allSeats);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
