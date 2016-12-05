package com.example.ticket.impl.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.example.ticket.api.Config;
import com.example.ticket.api.models.Venue;

public class PropertyFileBasedConfig implements Config {
	private Properties configs= new Properties();
	private Venue venue=null;
	public PropertyFileBasedConfig(InputStream stream) throws IOException{
		if(stream==null){
			throw new IllegalArgumentException("'stream' must not be null.");
		}
		configs.load(stream);
	}

	@Override
	public String getProperty(String name, String defaultValue) {
		return configs.getProperty(name, defaultValue);
	}

	@Override
	public String getProperty(String name) {
		return configs.getProperty(name);
	}

	@Override
	public int getIntegerProperty(String name, int defaultValue) {
		int value=defaultValue;
		try {
			String valueAsString = getProperty(name);
			if(valueAsString!=null){
				value= Integer.parseInt(valueAsString);
			}
		} catch (NumberFormatException e) {
			//DO nothing
			//TODO log warning that default value is being used
		}
		return value;
	}
	
	@Override
	public void setVenue(Venue venue) {
		this.venue=venue;
		
	}
	@Override
	public Venue getVenue() {
		return venue;
	}

}
