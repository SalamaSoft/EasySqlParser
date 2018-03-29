package com.salama.easysqlparser.base;

import java.util.ArrayList;
import java.util.List;

public class SqlPart {
    public enum SqlPartType {
        //Union
        Union,
        //Select ----------
        Select, From, Join, Where,
        GroupBy, Having,
        OrderBy, Limit,
        //Insert ----------
        Insert, Into, Values, Value,
        //Update ----------
        Update, Set,
        //Delete ----------
        Delete,
        Truncate, Drop, Create, Using,
        Others
    }

    ;

    public SqlPartType partType;

    //public StringBuilder sql = new StringBuilder();
    public List<String> tokenList = new ArrayList<String>();

//	@Override
//	public void appendToSql(StringBuilder sql) throws SqlParseException {
//		sql.append(this.sql);
//	}

    public void toSql(StringBuilder sql) {
        for (int i = 0; i < tokenList.size(); i++) {
            sql.append(" ").append(tokenList.get(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();

        toSql(sql);

        return sql.toString();
    }
}
