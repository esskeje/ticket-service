package com.example.ticket.impl.dao;

import com.example.ticket.api.Constants;
import com.example.ticket.api.dao.DAOException;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.impl.dao.inmemory.InMemoryDAOFactoryImpl;

public class AbstractDAOFactory {
	public static  DAOFactory create(String persistenceType) throws DAOException{
		if(Constants.PERSITENCE_TYPE_MEMORY.equalsIgnoreCase(persistenceType)){
			return new InMemoryDAOFactoryImpl();
		}
		else{
			throw new DAOException("Unsupported persitence type: "+persistenceType);
		}
	}
}
