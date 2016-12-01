package com.example.ticket.api;

import java.util.List;

import com.example.ticket.api.models.Seat;

public interface BestSeatStrategy {
	List<Seat> findBest(List<Seat> seats, int count);

}
