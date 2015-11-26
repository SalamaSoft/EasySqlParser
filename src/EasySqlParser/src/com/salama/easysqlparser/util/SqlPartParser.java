package com.salama.easysqlparser.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.salama.easysqlparser.base.SqlPart;
import com.salama.easysqlparser.base.SqlPart.SqlPartType;
import com.salama.easysqlparser.base.SqlPartKeyword;

public class SqlPartParser {
	
	private final static char[] TokenDelims = new char[] {' ', '(', ')'};
	private final static boolean[] IsDelimAsToken = new boolean[] {false, true, true};
	
	public static List<SqlPart> parseSqlPart(StringBuilder sql, 
			SqlPartKeyword[] sqlPartKeywords, SqlPartKeyword[] invalidSqlPartKeyWords) throws SqlParseException {
		List<SqlPart> sqlPartList = new ArrayList<SqlPart>();
		
		int i;
		boolean isInvalidTokenExists = false;
		boolean isHitKeyword = false;
		//boolean partBegin = true;
		String token = null;
		SqlPartKeyword prevHitSqlPartKeyword = null;
		SqlPart sqlPartTmp = null;
		int searchBeginIndex = 0;
		int searchEndIndex = sql.length();
		
		SqlPart sqlPartBlockBegin = null;
		SqlPart sqlPartBlockEnd = null;
		if(sql.charAt(0) == '(' && sql.charAt(sql.length()-1) == ')') {
			sqlPartBlockBegin = new SqlPart();
			sqlPartBlockBegin.partType = SqlPartType.Others;
			sqlPartBlockBegin.tokenList.add("(");
			
			sqlPartBlockEnd = new SqlPart();
			sqlPartBlockEnd.partType = SqlPartType.Others;
			sqlPartBlockEnd.tokenList.add(")");
			
			searchBeginIndex = 1;
			searchEndIndex = sql.length() - 1;
		}
		
		CharDelimStringTokenizer stk = new CharDelimStringTokenizer(
				sql, searchBeginIndex, searchEndIndex,
				TokenDelims, IsDelimAsToken);
		while(stk.hasMoreTokens()) {
			token = stk.nextToken();
			
			//check invalid keyword
			isInvalidTokenExists = false;
			for(i = 0; i < invalidSqlPartKeyWords.length; i++) {
				if(token.equalsIgnoreCase(invalidSqlPartKeyWords[i].keyword)) {
					isInvalidTokenExists = true;
					break;
				}
			}
			if(isInvalidTokenExists) {
				throw new SqlParseException("Sql contains invalid keyword:" + invalidSqlPartKeyWords[i].keyword);
			}
			
			//check keyword
			isHitKeyword = false;
			for(i = 0; i < sqlPartKeywords.length; i++) {
				if(token.equalsIgnoreCase(sqlPartKeywords[i].keyword)) {
					isHitKeyword = true;
					break;
				}
			}
			if(isHitKeyword) {
				if(prevHitSqlPartKeyword != null 
						&& prevHitSqlPartKeyword.partType != SqlPartType.Others 
						&& prevHitSqlPartKeyword.partType == sqlPartKeywords[i].partType) {
					//continuous same SqlPart such as left join, does not need new SqlPart()
				} else {
					sqlPartTmp = new SqlPart();
					sqlPartList.add(sqlPartTmp);

					sqlPartTmp.partType = sqlPartKeywords[i].partType;
				}
				
				//sqlPartTmp.sql.append(" ").append(token);
				sqlPartTmp.tokenList.add(token);
				
				if(sqlPartTmp.partType == SqlPartType.Union) {
					sqlPartTmp = null;
				}
				
				prevHitSqlPartKeyword = sqlPartKeywords[i];		
			} else {
				if(sqlPartTmp == null) {
					sqlPartTmp = new SqlPart();
					sqlPartList.add(sqlPartTmp);

					sqlPartTmp.partType = SqlPartType.Others;
				}
				//sqlPartTmp.sql.append(" ").append(token);
				sqlPartTmp.tokenList.add(token);
				
				prevHitSqlPartKeyword = null;
			}
		}
		
		if(sqlPartBlockBegin != null) {
			sqlPartList.add(0, sqlPartBlockBegin);
			sqlPartList.add(sqlPartBlockEnd);
		}
		
		return sqlPartList;
	}
}
