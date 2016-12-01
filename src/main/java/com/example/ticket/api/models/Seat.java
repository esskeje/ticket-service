package com.example.ticket.api.models;

import java.util.Objects;
/**
 * Pojo class to represent a Seat
 * @author satish
 *
 */
public class Seat {
	private int id;
	private int  rowId;
	private int rank;
	private Level level;
	private Status status;
	
	public Seat(int id, int rowId, int rank, Status status,Level level) {
		this.id = id;
		this.rowId = rowId;
		this.rank = rank;
		this.status = status;
		this.level=level;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public int getRowId() {
		return rowId;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getId() {
		return id;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Seat){
			return Objects.equals(id, ((Seat)obj).getId());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public String toString() {
		return " seat id="+id;
	}
	
}
