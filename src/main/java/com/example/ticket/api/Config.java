package com.example.ticket.api;

public interface Config {
	String getProperty(String name);
	String getProperty(String name,String defaultValue);
	//TODO support more data types
	int getIntegerProperty(String name, int defaultValue);
}
