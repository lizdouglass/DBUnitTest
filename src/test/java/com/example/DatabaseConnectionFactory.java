package com.example;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;

public class DatabaseConnectionFactory {

	private static final String DRIVER = "net.sourceforge.jtds.jdbc.Driver";
	
	private static final Map<String, BasicDataSource> urlToDataSource = new HashMap<String, BasicDataSource>();

	public static synchronized IDatabaseConnection getPooledDatabaseConnection(Database database) throws DatabaseUnitException, SQLException {

		BasicDataSource dataSource = urlToDataSource.get(database.getUrl());
		if(dataSource == null){
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(DRIVER);
			dataSource.setUrl(database.getUrl());
			urlToDataSource.put(database.getUrl(), dataSource);
		}
		
		return new DatabaseConnection(dataSource.getConnection());
	}
}
