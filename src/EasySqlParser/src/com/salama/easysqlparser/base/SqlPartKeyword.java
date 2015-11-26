package com.salama.easysqlparser.base;

import com.salama.easysqlparser.base.SqlPart.SqlPartType;
import com.salama.easysqlparser.util.SqlParseException;

public class SqlPartKeyword {
	public SqlPartType partType;
	public String keyword;
	
	public SqlPartKeyword() {
		
	} 
	
	public SqlPartKeyword(String kw) throws SqlParseException {
		if (kw.equals("select")) {
			partType = SqlPartType.Select;
		} else if (kw.equals("from")) {
			partType = SqlPartType.From;
		} else if (kw.equals("join")) {
			partType = SqlPartType.Join;
		} else if (kw.equals("inner")) {
			partType = SqlPartType.Join;
		} else if (kw.equals("cross")) {
			partType = SqlPartType.Join;
		} else if (kw.equals("straight_join")) {
			partType = SqlPartType.Join;
		} else if (kw.equals("left")) {
			partType = SqlPartType.Join;
		} else if (kw.equals("right")) {
			partType = SqlPartType.Join;
		} else if (kw.equals("natural")) {
			partType = SqlPartType.Join;
		} else if (kw.equals("where")) {
			partType = SqlPartType.Where;
		} else if (kw.equals("group")) {
			partType = SqlPartType.GroupBy;
		} else if (kw.equals("having")) {
			partType = SqlPartType.Having;
		} else if (kw.equals("limit")) {
			partType = SqlPartType.Limit;
		} else if (kw.equals("order")) {
			partType = SqlPartType.OrderBy;
		} else if (kw.equals("insert")) {
			partType = SqlPartType.Insert;
		} else if (kw.equals("into")) {
			partType = SqlPartType.Into;
		} else if (kw.equals("values")) {
			partType = SqlPartType.Values;
		} else if (kw.equals("value")) {
			partType = SqlPartType.Value;
		} else if (kw.equals("update")) {
			partType = SqlPartType.Update;
		} else if (kw.equals("set")) {
			partType = SqlPartType.Set;
		} else if (kw.equals("delete")) {
			partType = SqlPartType.Delete;
		} else if(kw.equals("union")) {
			partType = SqlPartType.Union;
		} else if (kw.equals("truncate")) {
			partType = SqlPartType.Truncate;
		} else if (kw.equals("drop")) {
			partType = SqlPartType.Drop;
		} else if (kw.equals("create")) {
			partType = SqlPartType.Create;
		} else if (kw.equals("using")) {
			partType = SqlPartType.Using; 
		} else {
			throw new SqlParseException("SqlPartType keyword is invalid:" + kw);
		}
		
		keyword = kw;
	}
	
//	public SqlPartKeyword(SqlPartType type, String kw) {
//		partType = type;
//		keyword = kw;
//	}
	
}
