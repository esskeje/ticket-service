package com.example.ticket.impl.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.models.Seat;

public class PriceBasedBestSeatStrategy implements BestSeatStrategy {

	@Override
	public List<Seat> findBest(List<Seat> seats, int count) {
		Collections.sort(seats, new Comparator<Seat>() {
			@Override
			public int compare(Seat o1, Seat o2) {
				return  o1.getLevel().getPrice().compareTo(o2.getLevel().getPrice());
			}
		});
		return seats.subList(0, count);
	}

}
