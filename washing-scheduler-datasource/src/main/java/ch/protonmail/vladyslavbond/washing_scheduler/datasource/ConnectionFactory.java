package ch.protonmail.vladyslavbond.washing_scheduler.datasource;

import java.sql.Connection;

public interface ConnectionFactory {
	Connection getConnection();
}
