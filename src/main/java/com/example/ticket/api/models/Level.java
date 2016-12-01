package com.example.ticket.api.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Pojo class to represent level and associated properties
 * 
 * @author satish
 *
 */
public class Level {
	private int id;
	private BigDecimal price;
	private List<Seat> seats= new ArrayList<>();
	
	public Level(int id,BigDecimal price){
		this.id=id;
		this.price=price;
	}
	
	public int getId() {
		return id;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public List<Seat> getSeats() {
		return seats;
	}
	
	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Level){
			return Objects.equals(id, ((Level)obj).getId());
		}
		return false;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public String toString() {
		return "level id="+id;
	}
	
}
