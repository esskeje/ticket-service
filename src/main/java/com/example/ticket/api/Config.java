package com.example.ticket.api;

import com.example.ticket.api.models.Venue;
/**
 * Interface/contract for configuration management
 * .
 * @author satish
 *
 */
public interface Config {
	/**
	 * 
	 * @param name
	 * @return string value
	 */
	String getProperty(String name);
	/**
	 * if value is null will return the supplied defaultValue
	 * 
	 * @param name
	 * @param defaultValue
	 * @return string value
	 */
	String getProperty(String name,String defaultValue);
	/**
	 * if value is null or invalid, it will return the supplied defaultValue
	 * @param name
	 * @param defaultValue
	 * @return integer value
	 */
	int getIntegerProperty(String name, int defaultValue);
	/**
	 * set the Venue pojo.
	 * 
	 * @param venue
	 */
	void setVenue(Venue venue);
	
	/**
	 * 
	 * @return venue pojo
	 */
	Venue getVenue();
}
