package ch.protonmail.vladyslavbond.washing_scheduler.domain;

public class ReservationDuration extends Number {
	private final static ReservationDuration EMPTY = new ReservationDuration();
	
	private final int quantityOfMinutes;
	
	private ReservationDuration() {
		this(0);
	}
	
	private ReservationDuration(int quantityOfMinutes) {
		this.quantityOfMinutes = quantityOfMinutes;
	}
	
	public static ReservationDuration valueOf(String duration) {
		Integer quantityOfMinutes = Integer.valueOf(duration);
		if (quantityOfMinutes > 0) {
			return new ReservationDuration(quantityOfMinutes);
		}
		return ReservationDuration.EMPTY;
	}

	@Override
	public double doubleValue() {
		return (double)quantityOfMinutes;
	}

	@Override
	public float floatValue() {
		return (float)quantityOfMinutes;
	}

	@Override
	public int intValue() {
		return quantityOfMinutes;
	}

	@Override
	public long longValue() {
		return (long)quantityOfMinutes;
	}
}
