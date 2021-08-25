package com.salama.easysqlparser.query;

import com.salama.easysqlparser.base.ISelectQueryHandler;
import com.salama.easysqlparser.base.SqlObj;
import com.salama.easysqlparser.base.SqlPart;
import com.salama.easysqlparser.base.SqlPart.SqlPartType;
import com.salama.easysqlparser.base.SqlPartKeyword;
import com.salama.easysqlparser.util.Range;
import com.salama.easysqlparser.util.SqlParseException;
import com.salama.easysqlparser.util.SubQueryFinder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class SelectQuery extends SqlObj {
    private final static Log logger = LogFactory.getLog(SelectQuery.class);

    private static SqlPartKeyword[] SqlPartKeywords = null;
    private static SqlPartKeyword[] InvalidSqlPartKeywords = null;

    static {
        try {
            SqlPartKeywords = new SqlPartKeyword[]{
                    new SqlPartKeyword("select"),
                    new SqlPartKeyword("from"),
                    new SqlPartKeyword("join"),
                    new SqlPartKeyword("inner"),
                    new SqlPartKeyword("cross"),
                    new SqlPartKeyword("straight_join"),
                    new SqlPartKeyword("left"),
                    new SqlPartKeyword("right"),
                    new SqlPartKeyword("natural"),
                    new SqlPartKeyword("where"),
                    new SqlPartKeyword("order"),
                    new SqlPartKeyword("group"),
                    new SqlPartKeyword("having"),
                    new SqlPartKeyword("limit"),
                    new SqlPartKeyword("union")
            };

            InvalidSqlPartKeywords = new SqlPartKeyword[]{
                    new SqlPartKeyword("insert"),
                    new SqlPartKeyword("into"),
                    new SqlPartKeyword("values"),
                    new SqlPartKeyword("update"),
                    new SqlPartKeyword("set"),
                    new SqlPartKeyword("delete"),
                    new SqlPartKeyword("truncate"),
                    new SqlPartKeyword("drop"),
                    new SqlPartKeyword("create"),
                    new SqlPartKeyword("using")
            };
        } catch (SqlParseException e) {
            //logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    ;


    private final static String SYMBOL_SUB_QUERY = "$subq";

    private ISelectQueryHandler _queryHandler;

    private List<SelectQuery> _subQuerieList = null;

    private boolean _isUnionSelect = false;
    private List<Range> _unionBlockRangeList = null;

    public SelectQuery(String sql,
                       ISelectQueryHandler queryHandler) throws SqlParseException {
        super(sql);
        _queryHandler = queryHandler;

        parseSql();
    }

    public SelectQuery(String sql,
                       ISelectQueryHandler queryHandler,
                       LiteralsValueStack literalsValueStack) throws SqlParseException {
        super(sql, literalsValueStack);

        _queryHandler = queryHandler;

        parseSql();
    }

    private void parseSql() throws SqlParseException {
        symbolizeSubQuery();

        parseToSqlPart(SqlPartKeywords, InvalidSqlPartKeywords);

        //check is union
        checkIsUnion();

        //invoke delegate
        _queryHandler.handleQuery(this);
    }


    @Override
    public void toSql(StringBuilder sql) throws SqlParseException {
        SqlPart sqlPartTmp = null;
        int i, k;
        String token;

        for (i = 0; i < _sqlPartList.size(); i++) {
            sqlPartTmp = _sqlPartList.get(i);
            //logger.debug(sqlPartTmp.partType + "--------------");

            for (k = 0; k < sqlPartTmp.tokenList.size(); k++) {
                token = sqlPartTmp.tokenList.get(k);

                //logger.debug(token);

                if (token.startsWith(SYMBOL_SUB_QUERY)) {
                    getSubQueryByName(token).toSql(sql);
                } else if (token.startsWith(LiteralsValueStack.SYMBOL_LITERALS)) {
                    sql.append(" ").append(_literalsValueStack.getValue(token));
                } else {
                    if (!token.equals("(")) {
                        sql.append(" ");
                    }

                    sql.append(token);
                }
            }
        }
    }

    private SelectQuery getSubQueryByName(String subQueryName) {
        int index = Integer.parseInt(subQueryName.substring(SYMBOL_SUB_QUERY.length()));
        return _subQuerieList.get(index);
    }


    private void symbolizeSubQuery() throws SqlParseException {
        long beginTime = System.currentTimeMillis();

        List<Range> rangeList = SubQueryFinder.findAllSubQuery(_editingSql);

        if (rangeList != null) {
            Range range = null;
            if (rangeList.size() > 0) {
                _subQuerieList = new ArrayList<SelectQuery>();
            }

            for (int i = rangeList.size() - 1; i >= 0; i--) {
                range = rangeList.get(i);

                String subQuerySql = _editingSql.substring(range.start, range.end);
                _editingSql.replace(range.start, range.end, " " + (SYMBOL_SUB_QUERY + i) + " ");

                _subQuerieList.add(0,
                        new SelectQuery(
                                subQuerySql, _queryHandler, _literalsValueStack));
            }
        }

        //logger.debug("symbolizeSubQuery() Takes MS:" + Long.toString(System.currentTimeMillis() - beginTime));
    }

    private void checkIsUnion() {
        SqlPart sqlPart = null;

        int i;
        //int lastSelectPartIndex = -1;
        Range range = new Range();
        range.start = 0;
        _isUnionSelect = false;
        for (i = 0; i < _sqlPartList.size(); i++) {
            sqlPart = _sqlPartList.get(i);

            if (sqlPart.partType == SqlPartType.Union) {
                if (_unionBlockRangeList == null) {
                    _unionBlockRangeList = new ArrayList<Range>();
                }

                range.end = i - 1;
                _unionBlockRangeList.add(range);

                range = new Range();
                range.start = i + 1;

                _isUnionSelect = true;
            }
        }

        if (_isUnionSelect) {
            range.end = _sqlPartList.size() - 1;
            _unionBlockRangeList.add(range);
        }

    }

    public boolean isUnionSelect() {
        return _isUnionSelect;
    }

    public List<Range> getUnionBlockRangeList() {
        return _unionBlockRangeList;
    }

//	public List<TableName> getFromTables() throws SqlParseException {
//		SqlPart sqlPart = null;
//		for(int i = 0; i < _sqlPartList.size(); i++) {
//			sqlPart = _sqlPartList.get(i);
//			
//			if(sqlPart.partType == SqlPartType.From) {
//				return SqlPartUtil.getFromTables(sqlPart);
//			}
//		}
//		
//		return null;
//	}

}
