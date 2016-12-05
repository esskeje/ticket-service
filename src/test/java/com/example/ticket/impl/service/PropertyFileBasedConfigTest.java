package com.example.ticket.impl.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.Config;

public class PropertyFileBasedConfigTest {
	Config config = null;
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Before
	public void setup() throws Exception {
		String properties = "strKey=strValue\nintKey=123";
		config = new PropertyFileBasedConfig(new ByteArrayInputStream(properties.getBytes()));

	}

	@Test
	public void testConstructionWhenStreamIsNull() throws Exception{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("'stream' must not be null.");
		config = new PropertyFileBasedConfig(null);
	}
	
	@Test
	public void testStringPropertyWhenPropertyExists() {
		assertEquals("strValue", config.getProperty("strKey"));
	}
	
	@Test
	public void testStringPropertyWhenPropertyDoesNotExistButDefaultSupplied() {
		assertEquals("defaultValue", config.getProperty("nonExistingStrKey","defaultValue"));
	}
	
	@Test
	public void testStringPropertyWhenPropertyDoesNotExist() {
		assertNull(config.getProperty("nonExistingStrKey"));
	}
	
	@Test
	public void testIntegerPropertyWhenPropertyExists() {
		assertEquals(123, config.getIntegerProperty("intKey",111));
	}
	
	@Test
	public void testIntegerPropertyWhenPropertyDoesNotExist() {
		assertEquals(111, config.getIntegerProperty("nonExistingIntKey",111));
	}
	
	@Test
	public void testIntegerPropertyWhenPropertyValueIsNotInteger() {
		assertEquals(111, config.getIntegerProperty("strKey",111));
	}

}
