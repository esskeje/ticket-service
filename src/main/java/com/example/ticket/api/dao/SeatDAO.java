package com.example.ticket.api.dao;

import java.util.List;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.Status;

public interface SeatDAO {
	
	List<Seat> findByStatus(Status status);
	void save(List<Seat> seats);
	List<Seat> findBestByStatus(Status status,BestSeatStrategy bestStrategy, int numSeats);

}
