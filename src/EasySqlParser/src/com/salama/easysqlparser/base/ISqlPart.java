package com.salama.easysqlparser.base;

import com.salama.easysqlparser.util.SqlParseException;

public interface ISqlPart {
	void appendToSql(StringBuilder sql) throws SqlParseException;
}
