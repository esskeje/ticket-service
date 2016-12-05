package com.example.ticket.api.models;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class VenueTest {
	@Test
	public void testVenueSettersAndGetters() throws Exception {
		Venue v= new Venue();
		v.setName("theatre");
		Set<Level> levels = createLevels();
		v.setLevels(levels);
		assertEquals("name","theatre", v.getName());
		assertEquals("levels",levels, v.getLevels());
	}
	//util
	public static Set<Level> createLevels() throws IOException {
		Set<Level> levels = new HashSet<>();
		int seatCount=1;
		int rank=100;//TODO rank can be calculated based on various factor then seats can be ranked based on it
		int numSeatsPerRow=2;
		BigDecimal price= new BigDecimal(50);
		//right now it's unused
		for(int levelCount=1;levelCount<=2;levelCount++){
			Level level= new Level(levelCount,price);
			levels.add(level);
			List<Seat> seats= new ArrayList<>();
			level.setSeats(seats);
			price.subtract(new BigDecimal(5));
			for(int rowCount=1;rowCount<=2;rowCount++){
				for(int seatsPerRowCount=1;seatsPerRowCount<=numSeatsPerRow;seatsPerRowCount++){
					Status status=Status.AVAILABLE;
					Seat seat= new Seat(seatCount, rowCount, rank, status,level);
					seats.add(seat);
					seatCount++;
				}
			}
		}
		return levels;
	}

}
