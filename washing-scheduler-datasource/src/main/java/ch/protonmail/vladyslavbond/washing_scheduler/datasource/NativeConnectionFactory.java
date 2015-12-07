package ch.protonmail.vladyslavbond.washing_scheduler.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum NativeConnectionFactory {
	INSTANCE;
	
	private transient Connection connection = null;
	
	private NativeConnectionFactory() {}
	
	public static Connection getConnection() {
		return INSTANCE.getConnection();
	}
	
	private final Connection getConnection0() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(
						System.getenv("WASHING_SCHEDULER_DATABASE_JDBC_URL"),
						System.getenv("WASHING_SCHEDULER_DATABASE_USERNAME"),
						System.getenv("WASHING_SCHEDULER_DATABASE_PASSWORD"));
			}
		} catch (SQLException e) {
			throw new AssertionError("Failed to establish a database connection.", e);
		}
		return connection;
	}
}
