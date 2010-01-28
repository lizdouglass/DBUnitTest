package com.example;

public enum Database {
	TEST_REPOSITORY("jdbc:jtds:sqlserver://192.168.56.101:1433/TestRepository;user=sa;password="),
	SOURCE_DATABASE("jdbc:jtds:sqlserver://192.168.56.101:1433/WslTutorial;user=sa;password="),
	WAREHOUSE_DATABASE("jdbc:jtds:sqlserver://192.168.56.101:1433/WslWarehouse;user=sa;password=");

	private final String databaseURL;

	Database(String databaseURL) {
		this.databaseURL = databaseURL;
	}

	public String getUrl() {
		return databaseURL;
	}
}
