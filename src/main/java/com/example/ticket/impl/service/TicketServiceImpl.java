package com.example.ticket.impl.service;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.example.ticket.api.BestSeatStrategy;
import com.example.ticket.api.Config;
import com.example.ticket.api.ConfirmationCodeGenerator;
import com.example.ticket.api.Constants;
import com.example.ticket.api.SeatHoldIdGenerator;
import com.example.ticket.api.dao.DAOException;
import com.example.ticket.api.dao.DAOFactory;
import com.example.ticket.api.dao.SeatDAO;
import com.example.ticket.api.dao.SeatHoldDAO;
import com.example.ticket.api.models.Seat;
import com.example.ticket.api.models.SeatHold;
import com.example.ticket.api.models.Status;
import com.example.ticket.api.service.TicketService;

/**
 * @author satish
 *
 */
public class TicketServiceImpl implements TicketService {
	protected SeatDAO seatDAO;
	protected SeatHoldDAO seatHoldDAO;
	protected SeatHoldIdGenerator seatHoldIdGenerator;
	protected ConfirmationCodeGenerator confirmationCodeGenerator;
	protected BestSeatStrategy bestSeatStrategy;
	protected Logger logger;
	protected TicketServiceInputValidator validator;
	
	/**
	 * Constructor with default values
	 * 
	 * @param config
	 * @param factory
	 * @throws TicketServiceException
	 */
	public TicketServiceImpl(Config config,DAOFactory factory)throws TicketServiceException {
		this(config,new SimpleSeatHoldIdGenerator(),new SimpleConfirmationCodeGenerator(),
				factory, new PriceBasedBestSeatStrategy(),Logger.getLogger(TicketServiceImpl.class.getName()));
	}

	/**
	 * Constructor
	 * 
	 * @param config
	 * @param seatHoldIdGenerator
	 * @param confirmationCodeGenerator
	 * @param factory
	 * @param bestSeatStrategy
	 * @param logger
	 * @throws TicketServiceException
	 */
	public TicketServiceImpl(Config config, SeatHoldIdGenerator seatHoldIdGenerator,
			ConfirmationCodeGenerator confirmationCodeGenerator, DAOFactory factory, BestSeatStrategy bestSeatStrategy,
			Logger logger) throws TicketServiceException {
		TicketServiceUtil.nullCheck(config,"Config");
		TicketServiceUtil.nullCheck(factory,"DAOFactory");
		TicketServiceUtil.nullCheck(seatHoldIdGenerator,"SeatHoldIdGenerator");
		TicketServiceUtil.nullCheck(confirmationCodeGenerator,"ConfirmationCodeGenerator");
		TicketServiceUtil.nullCheck(bestSeatStrategy,"BestSeatStrategy");
		TicketServiceUtil.nullCheck(logger,"Logger");
		//initialize ttl
		try {
			//create DAOs
			this.seatDAO = factory.createSeatDAO(config);
			this.seatHoldDAO = factory.createSeatHoldDAO(config);
		} catch (DAOException e) {
			// TODO log error
			throw new TicketServiceException("Ticket Service intialization failed.", e);
		}
		this.seatHoldIdGenerator = seatHoldIdGenerator;
		this.confirmationCodeGenerator = confirmationCodeGenerator;
		this.bestSeatStrategy = bestSeatStrategy;
		this.logger = logger;
		this.validator=new TicketServiceInputValidator();
	}

	/**
	 * Logic: 
	 * 1) remove expired holds and mark seats as available 
	 * 2) return all available seats those are not held nor reserved
	 */
	public int numSeatsAvailable() {
		removeExpiredSeatHoldsAndMarkThemAsAvailableSeats();
		// TODO if cleanup can be done asynchronously in a separate thread in background
		List<Seat> availableSeats = seatDAO.findByStatus(Status.AVAILABLE);
		return availableSeats.size();
	}

	/**
	 * Logic: 
	 * 1) validate user input - numSeats and customerEmail 
	 * 2) remove expired holds and mark seats as available 
	 * 3) put hold on the numSeats seats based on the BestSeatStrategy 
	 * 4) if held seats not equal to numSeats, fail 
	 * 5) save to db
	 * 6) return SeatHold instance
	 */
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) throws TicketServiceException {
		try {
			validator.validateInputForFindAndHoldSeats(numSeats, customerEmail);
			// TODO if cleanup can be done asynchronously in a separate thread in the background
			removeExpiredSeatHoldsAndMarkThemAsAvailableSeats();
			List<Seat> bestAvailbleSeats = seatDAO.findBestByStatus(Status.AVAILABLE,bestSeatStrategy, numSeats);
			if(bestAvailbleSeats==null){
				throw new TicketServiceException("TicketService.findAndHoldSeats() failed. Cause: requested seats not available.");
			}
			validator.validateRequestedNumberOfSeatsHasBeenHeld(bestAvailbleSeats.size(), numSeats);
			SeatHold seatHold = new SeatHold(seatHoldIdGenerator.getNextSeatHoldId(), customerEmail, bestAvailbleSeats);
			TicketServiceUtil.updateSeatStatus(bestAvailbleSeats, Status.HELD);
			saveToDB(bestAvailbleSeats, seatHold);
			return seatHold;
		} catch (DAOException e) {
			// TODO log error
			throw new TicketServiceException("TicketService.findAndHoldSeats() failed.", e);
		}
	}

	/**
	 * Logic: 
	 * 1) validate user input - seatHoldId and customerEmail 
	 * 2) if there is no held seats for supplied seatHoldId and customerEmail combination 
	 * then fail 
	 * 3) remove expired holds and mark seats as available 
	 * 4) if any of the seats those were HELD are not in HELD status now then fail 
	 * 5) save to db 
	 * 6) return confirmationCode
	 */
	public String reserveSeats(int seatHoldId, String customerEmail) throws TicketServiceException {
		try {
			validator.validateInputForReserveSeats(seatHoldId, customerEmail);
			SeatHold.SeatHoldKey seatHoldKey = new SeatHold.SeatHoldKey(seatHoldId, customerEmail);
			SeatHold seatHold = seatHoldDAO.findBySeatHoldIdAndCustomerEmail(seatHoldKey);
			validator.validateSeatHoldIsNotNull(seatHoldKey, seatHold);
			// TODO if cleanup can be done asynchronously in a separate thread in background
			removeExpiredSeatHoldsAndMarkThemAsAvailableSeats();
			validator.validateAllReservedSeatsHaveBeenInHoldBefore(seatHold);
			List<Seat> heldSeats = seatHold.getHeldSeats();
			TicketServiceUtil.updateSeatStatus(heldSeats, Status.COMMITED);
			String confirmationCode = confirmationCodeGenerator.getNextConfirmationCode();
			seatHold.setConfirmationCode(confirmationCode);
			saveToDB(heldSeats, seatHold);
			return confirmationCode;
		} catch (DAOException e) {
			// TODO log error
			throw new TicketServiceException("TicketService.reserveSeats().", e);
		}
	}
	
	/**
	 * Utility method to save Seats and SeatHold to DB.
	 * 
	 * @param seats
	 * @param seatHold
	 * @throws DAOException
	 */
	protected void saveToDB(List<Seat> seats, SeatHold seatHold) throws DAOException {
		seatDAO.save(seats);
		seatHoldDAO.save(seatHold);
	}
	
	/**
	 * This method removes the expired seat holds and marks the available seats as availble.
	 * Also updates seatDAO (DB)
	 */
	protected void removeExpiredSeatHoldsAndMarkThemAsAvailableSeats() {
		Collection<SeatHold> allExpiredSeatHolds = seatHoldDAO.findAllButRemoveExpired();
		if(allExpiredSeatHolds!=null){
			for (SeatHold expiredSeatHold : allExpiredSeatHolds) {
				TicketServiceUtil.updateSeatStatus(expiredSeatHold.getHeldSeats(), Status.AVAILABLE);
				seatDAO.save(expiredSeatHold.getHeldSeats());
			}
		}
	}
}