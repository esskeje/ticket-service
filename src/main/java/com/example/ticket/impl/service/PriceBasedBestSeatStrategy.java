package com.example.ticket.impl.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.models.Seat;

/**
 * This class implements BestSeatStrategy based on the price.
 * 
 * @author satish
 *
 */
public class PriceBasedBestSeatStrategy implements BestSeatStrategy {

	@Override
	public List<Seat> findBest(List<Seat> seats, int count) {
		if (seats == null) {
			throw new IllegalArgumentException("seats must not be null");
		}
		if (count > seats.size()) {
			throw new IllegalArgumentException("count must be less or equal to " + seats.size());
		}
		//Sort based on the price
		Collections.sort(seats, new Comparator<Seat>() {
			@Override
			public int compare(Seat o1, Seat o2) {
				return o2.getLevel().getPrice().compareTo(o1.getLevel().getPrice());
			}
		});
		//return the sublist
		return seats.subList(0, count);
	}

}
