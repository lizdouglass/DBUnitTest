package com.example;

import org.dbunit.dataset.filter.ITableFilterSimple;
import org.dbunit.dataset.filter.IncludeTableFilter;

public class TableFilterBuilder {

	private String tableName = "";

	public TableFilterBuilder withTable(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public ITableFilterSimple build() {
		ITableFilterSimple tableFilter = new IncludeTableFilter(); 
		((IncludeTableFilter) tableFilter).includeTable(tableName);
		return tableFilter;
	}

}
