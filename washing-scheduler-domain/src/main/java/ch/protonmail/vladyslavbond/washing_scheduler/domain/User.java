package ch.protonmail.vladyslavbond.washing_scheduler.domain;

public abstract class User {
	private final short  id;
	private final String facebookId;
	
	protected User(short id, String facebookId) {
		super();
		assert id > 0;
		this.id = id;
		this.facebookId = facebookId;
	}
	
	public int getId() {
		return id;
	}
	
	public String getFacebookId() {
		return facebookId;
	}

	public static User getInstance(short id) {
		return new SimpleUser(id);
	}
}
