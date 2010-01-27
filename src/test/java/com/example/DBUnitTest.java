package com.example;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBUnitTest {

	private static final String DATABASE_URL = "jdbc:derby://localhost:1527/firstdb";
	private static final String SEED_FILE = "src/test/resources/seed.xml";
	private static IDataSet data;
	private static IDatabaseConnection conn;

	@BeforeClass
	public static void handleSetUpOperation() throws Exception {
		conn = getConnection();
		data = getDataSet();

		DatabaseOperation.CLEAN_INSERT.execute(conn, data);
	}

	private static IDataSet getDataSet() throws IOException, DataSetException {
		File file = new File(SEED_FILE);
		return new FlatXmlDataSetBuilder().build(file);
	}

	private static IDatabaseConnection getConnection()
			throws ClassNotFoundException, SQLException, DatabaseUnitException {
		return new DatabaseConnection(DriverManager.getConnection(DATABASE_URL));
	}

	@AfterClass
	public static void handleTearDownOperation() throws Exception {
		conn.close();
	}
	
	@Test
	public void shouldHavePopulatedSeedDataInDatabase() throws Exception {
		IDatabaseConnection conn = getConnection();
		IDataSet databaseDataSet = conn.createDataSet();
		Assertion.assertEquals(data, databaseDataSet);
		Assert.assertEquals(3, ((IDataSet) databaseDataSet).getTable("FIRSTTABLE").getRowCount());
	}

}
