package com.example.ticket.api.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SeatHold {
	private SeatHoldKey seatHoldKey;
	private List<Seat> heldSeats;
	private LocalDateTime holdTime;
	String confirmationCode;

	public SeatHold(int seatHoldId, String customerEmail, List<Seat> heldSeats) {
		this.seatHoldKey = new SeatHoldKey(seatHoldId,customerEmail);
		this.heldSeats = heldSeats;
		holdTime = LocalDateTime.now();
	}

	public SeatHoldKey getSeatHoldKey() {
		return seatHoldKey;
	}

	public List<Seat> getHeldSeats() {
		return heldSeats;
	}

	public LocalDateTime getHoldTime() {
		return holdTime;
	}
	
	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode=confirmationCode;
		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SeatHold) {
			SeatHold o = (SeatHold) obj;
			if (Objects.equals(seatHoldKey, o.seatHoldKey)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(seatHoldKey);
	}

	static public class SeatHoldKey {
		private String customerEmail;
		private int seatHoldId;

		public SeatHoldKey(int seatHoldId, String customerEmail) {
			this.seatHoldId = seatHoldId;
			this.customerEmail = customerEmail;
		}

		public String getCustomerEmail() {
			return customerEmail;
		}

		public int getSeatHoldId() {
			return seatHoldId;
		}
		

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof SeatHoldKey) {
				SeatHoldKey o = (SeatHoldKey) obj;
				if (Objects.equals(seatHoldId, o.seatHoldId) && Objects.equals(customerEmail, o.customerEmail)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(seatHoldId, customerEmail);
		}

		@Override
		public String toString() {
			return "customerEmail=" + customerEmail + ", seatHoldId=" + seatHoldId;
		}
	}

	

}
