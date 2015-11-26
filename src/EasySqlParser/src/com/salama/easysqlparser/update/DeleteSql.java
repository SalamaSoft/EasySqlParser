package com.salama.easysqlparser.update;

import org.apache.log4j.Logger;

import com.salama.easysqlparser.base.SqlObj;
import com.salama.easysqlparser.base.SqlPartKeyword;
import com.salama.easysqlparser.util.SqlParseException;

public class DeleteSql extends SqlObj {
	private final static Logger logger = Logger.getLogger(DeleteSql.class);

	private static SqlPartKeyword[] SqlPartKeywords = null;
	private static SqlPartKeyword[] InvalidSqlPartKeywords = null;

	static {
		try {
			SqlPartKeywords = new SqlPartKeyword[]{
					new SqlPartKeyword("delete"),
					new SqlPartKeyword("from"),
					new SqlPartKeyword("where"),
					new SqlPartKeyword("order"),
					new SqlPartKeyword("limit")
				};
			
			InvalidSqlPartKeywords = new SqlPartKeyword[]{
					new SqlPartKeyword("insert"),
					new SqlPartKeyword("into"),
					new SqlPartKeyword("values"),
					new SqlPartKeyword("select"),
					new SqlPartKeyword("update"),
					new SqlPartKeyword("truncate"),
					new SqlPartKeyword("drop"),
					new SqlPartKeyword("create"),
					new SqlPartKeyword("using")
				};		
		} catch(SqlParseException e) {
			logger.error("", e);
		}
	};
	
	public DeleteSql(String sql) throws SqlParseException {
		super(sql);
		
		parseToSqlPart(SqlPartKeywords, InvalidSqlPartKeywords);
	}
	
	
}
