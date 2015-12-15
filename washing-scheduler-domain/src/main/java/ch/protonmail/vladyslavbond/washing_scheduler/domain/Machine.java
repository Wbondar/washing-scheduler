package ch.protonmail.vladyslavbond.washing_scheduler.domain;

public class Machine {
	private final int   id;
	private final Model model;
	
	private Machine(int id, Model model) {
		super();
		assert id > 0;
		this.id = id;
		assert model != null;
		this.model = model;
	}

	public class Model {
		private final int    id;
		private final String code;
		
		private Model(int id, String code) {
			super();
			assert id > 0;
			this.id = id;
			assert code != null && !code.isEmpty();
			this.code = code;
		}

		public int getId() {
			return id;
		}
		
		public String getCode() {
			return code;
		}
	}

	public int getId() {
		return id;
	}

	public Model getModel() {
		return model;
	}
}
