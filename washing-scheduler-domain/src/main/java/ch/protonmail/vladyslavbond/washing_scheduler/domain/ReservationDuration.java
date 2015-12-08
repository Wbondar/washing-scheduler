package ch.protonmail.vladyslavbond.washing_scheduler.domain;

import org.postgresql.util.PGInterval;

public class ReservationDuration extends PGInterval {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2105665112239116633L;
	
	private final static ReservationDuration EMPTY = new ReservationDuration();
	
	private ReservationDuration() {
		this(0);
	}
	
	private ReservationDuration(int quantityOfMinutes) {
		super(0, 0, 0, 0, quantityOfMinutes, 0.0);
	}
	
	public static ReservationDuration valueOf(String duration) {
		Integer quantityOfMinutes = Integer.valueOf(duration);
		if (quantityOfMinutes > 0) {
			return new ReservationDuration(quantityOfMinutes.intValue());
		}
		return ReservationDuration.EMPTY;
	}
}
