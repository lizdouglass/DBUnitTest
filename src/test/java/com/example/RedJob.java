package com.example;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.dbunit.database.IDatabaseConnection;

public class RedJob {

	private final IDatabaseConnection connection;
	private final String jobName;

	public RedJob(IDatabaseConnection connection, String jobName) {
		this.connection = connection;
		this.jobName = jobName;
	}

	public void execute() throws SQLException {
		CallableStatement stmt = connection.getConnection().prepareCall("{call Ws_Job_Release (?, ?, ?, ?, ?, ?, ?, ?, ?)}");
		stmt.setInt(1, 1);
		stmt.setString(2, "Release Job");
		stmt.setString(3, "Release");
		stmt.setInt(4, 0);
		stmt.setInt(5, 0);
		stmt.setString(6, jobName);
		stmt.registerOutParameter(7, Types.VARCHAR);
		stmt.registerOutParameter(8, Types.VARCHAR);
		stmt.registerOutParameter(9, Types.INTEGER);
		stmt.execute();
	}

}
