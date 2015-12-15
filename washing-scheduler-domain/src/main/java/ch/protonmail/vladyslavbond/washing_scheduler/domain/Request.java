package ch.protonmail.vladyslavbond.washing_scheduler.domain;

import java.time.*;

public abstract class Request {
	private final int           id;
	private final User          client;
	private final Machine       machine;
	private final LocalDateTime beginsAt;
	private final LocalDateTime finishesAt;
	
	private Request(int id, User client, Machine machine, LocalDateTime beginsAt, LocalDateTime finishesAt) {
		super();
		assert id > 0;
		this.id = id;
		assert client != null;
		this.client = client;
		assert machine != null;
		this.machine = machine;
		assert beginsAt != null;
		this.beginsAt = beginsAt;
		assert finishesAt != null && finishesAt.isAfter(beginsAt);
		this.finishesAt = finishesAt;
	}

	public int getId() {
		return id;
	}
	
	public User getClient() {
		return client;
	}
	
	public Machine getMachine() {
		return machine;
	}
	
	public LocalDateTime getBeginsAt() {
		return beginsAt;
	}
	
	public LocalDateTime getFinishesAt() {
		return finishesAt;
	}
}
