package com.example.ticket.api.models;

import java.util.HashSet;
import java.util.Set;
/**
 * Pojo class to represent a Venue.
 * 
 * @author satish
 *
 */
public class Venue {
	private String name;
	private Set<Level> levels= new HashSet<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Level> getLevels() {
		return levels;
	}
	public void setLevels(Set<Level> levels) {
		this.levels = levels;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (Level level : levels) {
			sb.append(level.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
