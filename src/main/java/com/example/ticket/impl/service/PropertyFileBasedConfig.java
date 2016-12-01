package com.example.ticket.impl.service;

import java.io.IOException;
import java.util.Properties;

import com.example.ticket.api.Config;

public class PropertyFileBasedConfig implements Config {
	private Properties configs= new Properties();
	public PropertyFileBasedConfig(String propertyFileName) throws IOException{
		configs.load(PropertyFileBasedConfig.class.getClassLoader().getResourceAsStream(propertyFileName));
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
			//TODO log warning that default value
		}
		return value;
	}

}
