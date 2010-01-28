package com.example;

import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseDataSet;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilterSimple;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBUnitTest {
	
	private static IDataSet testDataSet;
	private static IDataSet sourceDataSet;
	
	private static IDatabaseConnection testDatabaseConnection;
	private static IDatabaseConnection sourceDatabaseConnection;
	private static IDatabaseConnection warehouseDatabaseConnection;

	@BeforeClass
	public static void handleSetUpOperation() throws Exception {
		testDatabaseConnection = DatabaseConnectionFactory.getPooledDatabaseConnection(Database.TEST_REPOSITORY);
		sourceDatabaseConnection = DatabaseConnectionFactory.getPooledDatabaseConnection(Database.SOURCE_DATABASE);
		warehouseDatabaseConnection = DatabaseConnectionFactory.getPooledDatabaseConnection(Database.WAREHOUSE_DATABASE);

		ITableFilterSimple tableFilter = new TableFilterBuilder().withTable("order_header").build();

		testDataSet = getDataSet(tableFilter, testDatabaseConnection);
		DatabaseOperation.CLEAN_INSERT.execute(sourceDatabaseConnection, testDataSet);
		sourceDataSet = getDataSet(tableFilter, sourceDatabaseConnection);
	}


	@AfterClass
	public static void handleTearDownOperation() throws Exception {
		testDatabaseConnection.close();
		sourceDatabaseConnection.close();
		warehouseDatabaseConnection.close();
	}

	@Test
	public void TestRed() throws Exception {
		Assertion.assertEquals(testDataSet, sourceDataSet);
		
		RedJob job = new RedJob(warehouseDatabaseConnection, "LoadOrderHeader");
		job.execute();

		// Wait for job to finish

		ITableFilterSimple tableFilter = new TableFilterBuilder().withTable("load_order_header").build();
		IDataSet warehouseDataSet = getDataSet(tableFilter,warehouseDatabaseConnection);
		IDataSet testRepositoryDataSet = getDataSet(tableFilter,testDatabaseConnection);
		
		Assertion.assertEquals(testRepositoryDataSet, warehouseDataSet);

	}

	private static IDataSet getDataSet(ITableFilterSimple tableFilter, IDatabaseConnection connection)
			throws SQLException {
		return new DatabaseDataSet(connection, false, tableFilter);
	}


}
