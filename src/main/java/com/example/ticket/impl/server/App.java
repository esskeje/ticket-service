package com.example.ticket.impl.server;

import java.io.IOException;
import java.util.logging.Logger;

import com.example.ticket.api.Config;
import com.example.ticket.api.Constants;
import com.example.ticket.api.dao.DAOException;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.api.models.SeatHold;
import com.example.ticket.api.service.TicketService;
import com.example.ticket.impl.dao.AbstractDAOFactory;
import com.example.ticket.impl.service.SimpleConfirmationCodeGenerator;
import com.example.ticket.impl.service.PriceBasedBestSeatStrategy;
import com.example.ticket.impl.service.PropertyFileBasedConfig;
import com.example.ticket.impl.service.SimpleSeatHoldIdGenerator;
import com.example.ticket.impl.service.TicketServiceException;
import com.example.ticket.impl.service.TicketServiceImpl;

/**
 * Main application
 *
 */
public class App {
	
	public static void main(String[] args) {
		System.out.println("Ticket Service! START");
		try {
			Config config = new PropertyFileBasedConfig("ticket-service.properties");
			DAOFactory factory = AbstractDAOFactory.create(config.getProperty(Constants.PERSITENCE_TYPE, Constants.PERSITENCE_TYPE_MEMORY));
			TicketService ticketService = new TicketServiceImpl(config, new SimpleSeatHoldIdGenerator(),
					new SimpleConfirmationCodeGenerator(), 
					factory, 
					new PriceBasedBestSeatStrategy(),
					Logger.getLogger("TicketService"));
			
			int numSeatsAvailable = ticketService.numSeatsAvailable();
			System.out.println("Initial numSeatsAvailable: "+numSeatsAvailable);
			SeatHold seatHold = ticketService.findAndHoldSeats(2, "abc1@gmail.com");
			System.out.println("SeatHold="+seatHold.getSeatHoldKey().getSeatHoldId()+", "+seatHold.getSeatHoldKey().getCustomerEmail());
			
			numSeatsAvailable = ticketService.numSeatsAvailable();
			System.out.println("After 2 seats held, numSeatsAvailable: "+numSeatsAvailable);
			
			ticketService.reserveSeats(seatHold.getSeatHoldKey().getSeatHoldId(), seatHold.getSeatHoldKey().getCustomerEmail());
			numSeatsAvailable = ticketService.numSeatsAvailable();
			System.out.println("After above 2 seats reserved, numSeatsAvailable: "+numSeatsAvailable);
			
			seatHold = ticketService.findAndHoldSeats(2, "abc2@gmail.com");
			System.out.println("SeatHold="+seatHold.getSeatHoldKey().getSeatHoldId()+", "+seatHold.getSeatHoldKey().getCustomerEmail());
			
			numSeatsAvailable = ticketService.numSeatsAvailable();
			System.out.println("After another 2 seats held, numSeatsAvailable: "+numSeatsAvailable);
			
			ticketService.reserveSeats(seatHold.getSeatHoldKey().getSeatHoldId(), seatHold.getSeatHoldKey().getCustomerEmail());
			numSeatsAvailable = ticketService.numSeatsAvailable();
			System.out.println("After above 2 held seats reserved, numSeatsAvailable: "+numSeatsAvailable);
			
			
		} catch (TicketServiceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		System.out.println("Ticket Service! END.");
	}
	
}
