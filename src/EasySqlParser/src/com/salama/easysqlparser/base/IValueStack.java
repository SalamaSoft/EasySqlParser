package com.salama.easysqlparser.base;

import com.salama.easysqlparser.util.SqlParseException;

public interface IValueStack {
	String getValue(String name) throws SqlParseException;
	void setValue(String name, String value) throws SqlParseException;
}
