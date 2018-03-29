package com.salama.easysqlparser.base;

import com.salama.easysqlparser.base.SqlPart.SqlPartType;
import com.salama.easysqlparser.query.LiteralsValueStack;
import com.salama.easysqlparser.util.LiteralsFinder;
import com.salama.easysqlparser.util.Range;
import com.salama.easysqlparser.util.SqlParseException;
import com.salama.easysqlparser.util.SqlPartParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class SqlObj {
    private final static Log logger = LogFactory.getLog(SqlObj.class);

    protected StringBuilder _editingSql = null;
    protected LiteralsValueStack _literalsValueStack = null;

    protected List<SqlPart> _sqlPartList = new ArrayList<SqlPart>();

    protected SqlObj(String sql) throws SqlParseException {
        _editingSql = new StringBuilder(sql.trim());
        _literalsValueStack = new LiteralsValueStack();
        symbolizeLiteras();
    }

    protected SqlObj(String sql, LiteralsValueStack literalsValueStack) throws SqlParseException {
        _editingSql = new StringBuilder(sql.trim());
        _literalsValueStack = literalsValueStack;
    }

//	protected abstract SqlPartKeyword[] getSqlPartKeywords();
//	protected abstract SqlPartKeyword[] getInvalidSqlPartKeywords();

    protected void parseToSqlPart(SqlPartKeyword[] sqlPartKeywords, SqlPartKeyword[] invalidSqlPartKeywords) throws SqlParseException {
        long beginTime = System.currentTimeMillis();

        _sqlPartList = SqlPartParser.parseSqlPart(_editingSql,
                sqlPartKeywords, invalidSqlPartKeywords);

        logger.debug("parseToSqlPart() Takes MS:" + Long.toString(System.currentTimeMillis() - beginTime));
    }

    protected void symbolizeLiteras() throws SqlParseException {
        long beginTime = System.currentTimeMillis();

        List<Range> rangeList = LiteralsFinder.findAllLiterals(_editingSql);

        Range range = null;
        if (rangeList != null && rangeList.size() > 0) {
            for (int i = 0; i < rangeList.size(); i++) {
                range = rangeList.get(i);
                _literalsValueStack.addValue(_editingSql.substring(range.start, range.end));
            }
            for (int i = rangeList.size() - 1; i >= 0; i--) {
                range = rangeList.get(i);
                _editingSql.replace(range.start, range.end,
                        " " + LiteralsValueStack.getNameByIndex(i) + " ");
            }
        }

        logger.debug("symbolizeLiteras() Takes MS:" + Long.toString(System.currentTimeMillis() - beginTime));
    }

    public List<SqlPart> getSqlPartList() {
        return _sqlPartList;
    }

    public StringBuilder getEditingSql() {
        return _editingSql;
    }

    @Override
    public String toString() {

        try {
            StringBuilder sql = new StringBuilder();

            toSql(sql);

            return sql.toString();
        } catch (SqlParseException e) {
            return null;
        }
    }

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

                if (token.startsWith(LiteralsValueStack.SYMBOL_LITERALS)) {
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

    /**
     * 根据SqlPartType检索SqlPart
     *
     * @param partType 检索对象的SqlPartType
     * @return 匹配的SqlPart列表。没有匹配的结果时，返回null
     * @throws SqlParseException
     */
    public List<SqlPart> findSqlPartByType(SqlPartType partType) throws SqlParseException {
        List<SqlPart> partList = null;

        SqlPart sqlPart = null;
        for (int i = 0; i < _sqlPartList.size(); i++) {
            sqlPart = _sqlPartList.get(i);

            if (sqlPart.partType == partType) {
                if (partList == null) {
                    partList = new ArrayList<SqlPart>();
                }

                partList.add(sqlPart);
            }
        }

        return partList;
    }

    /**
     * 根据SqlPartType检索SqlPart(第1个)
     *
     * @param partType 检索对象的SqlPartType
     * @return 第一个匹配的SqlPart。没有匹配的结果时，返回null
     * @throws SqlParseException
     */
    public SqlPart find1stSqlPartByType(SqlPartType partType) throws SqlParseException {
        SqlPart sqlPart = null;
        for (int i = 0; i < _sqlPartList.size(); i++) {
            sqlPart = _sqlPartList.get(i);

            if (sqlPart.partType == partType) {
                return sqlPart;
            }
        }

        return null;
    }

    public void setLiteralsValue(String name, String value) throws SqlParseException {
        _literalsValueStack.setValue(name, value);
    }
}
