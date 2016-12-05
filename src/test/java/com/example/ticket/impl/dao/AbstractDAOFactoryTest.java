package com.example.ticket.impl.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.ticket.api.dao.DAOException;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.impl.dao.inmemory.InMemoryDAOFactoryImpl;

public class AbstractDAOFactoryTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Test
	public void testCreateWhenPersistentTypeIsMemory() throws Exception {
		DAOFactory fac1= AbstractDAOFactory.create("memory");
		assertTrue("should create an object of class InMemoryDAOFactoryImpl", fac1 instanceof InMemoryDAOFactoryImpl);
		
		DAOFactory fac2= AbstractDAOFactory.create("memory");
		assertTrue("should create an object of class InMemoryDAOFactoryImpl", fac2 instanceof InMemoryDAOFactoryImpl);
		
		assertTrue("should create a unique object every create() call", fac1!=fac2);
	}
	
	@Test
	public void testCreateWhenPersistentTypeIsNotSupported() throws Exception {
		thrown.expect(DAOException.class);
		thrown.expectMessage("Unsupported persitence type: db");
		DAOFactory fac1= AbstractDAOFactory.create("db");
		
	}
}
