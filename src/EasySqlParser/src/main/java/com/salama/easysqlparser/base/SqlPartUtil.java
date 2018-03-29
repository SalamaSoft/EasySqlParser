package com.salama.easysqlparser.base;

import com.salama.easysqlparser.base.SqlPart.SqlPartType;
import com.salama.easysqlparser.base.element.TableName;
import com.salama.easysqlparser.util.Range;
import com.salama.easysqlparser.util.SqlParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SqlPartUtil {
    public static List<TableName> getFromTables(SqlPart sqlPart) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.From) {
            return getFromOrUpdateTables(false, sqlPart);
        } else {
            throw new SqlParseException("SqlPart should be from part:" + sqlPart.toString());
        }
    }

    public static List<TableName> getUpdateTables(SqlPart sqlPart) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.Update) {
            return getFromOrUpdateTables(true, sqlPart);
        } else {
            throw new SqlParseException("SqlPart should be update part:" + sqlPart.toString());
        }
    }

    public static List<TableName> getFromOrUpdateTables(SqlPart sqlPart) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.From) {
            return getFromOrUpdateTables(false, sqlPart);
        } else if (sqlPart.partType == SqlPartType.Update) {
            return getFromOrUpdateTables(true, sqlPart);
        } else {
            throw new SqlParseException("SqlPart should be from part:" + sqlPart.toString());
        }
    }

    private static List<TableName> getFromOrUpdateTables(boolean isUpdate, SqlPart sqlPart) {
        List<TableName> tableNameList = new ArrayList<TableName>();

        String token = null;
        TableName tableName = null;

        tableName = new TableName();
        tableNameList.add(tableName);
        for (int i = 0; i < sqlPart.tokenList.size(); i++) {
            token = sqlPart.tokenList.get(i);

            if (isUpdate) {
                if (token.equalsIgnoreCase("update")) {
                    continue;
                }
            } else {
                if (token.equalsIgnoreCase("from")) {
                    continue;
                }
            }

            if (token.equals(",")) {
                tableName = new TableName();
                tableNameList.add(tableName);
            } else if (token.equalsIgnoreCase("as")) {
                continue;
            } else {
                if (tableName.tableName == null) {
                    tableName.tableName = token;
                } else {
                    tableName.tableNameAlias = token;
                }
            }
        }

        return tableNameList;
    }


    public static TableName getInsertTable(SqlPart sqlPart) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.Insert) {
            String token = null;
            for (int i = 0; i < sqlPart.tokenList.size(); i++) {
                token = sqlPart.tokenList.get(i);

                if (token.equalsIgnoreCase("into")) {
                    TableName t = new TableName();
                    t.tableName = sqlPart.tokenList.get(i + 1);

                    return t;
                }
            }

            return null;
        } else {
            throw new SqlParseException("SqlPart should be insert part:" + sqlPart.toString());
        }
    }

    public static TableName getJoinTable(SqlPart sqlPart) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.Join) {
            String token = null;
            String token1 = null;
            String token2 = null;
            String token3 = null;

            for (int i = 0; i < sqlPart.tokenList.size(); i++) {
                token = sqlPart.tokenList.get(i);

                if (token.equalsIgnoreCase("join")) {
                    TableName tableName = new TableName();

                    token1 = sqlPart.tokenList.get(i + 1);
                    token2 = sqlPart.tokenList.get(i + 2);

                    tableName.tableName = token1;
                    if (token2.equalsIgnoreCase("as")) {
                        token3 = sqlPart.tokenList.get(i + 3);
                        tableName.tableNameAlias = token3;
                    } else {
                        if (!token2.equalsIgnoreCase("on")) {
                            tableName.tableNameAlias = token2;
                        }
                    }

                    return tableName;
                }
            }

            return null;
        } else {
            throw new SqlParseException("SqlPart should be join part:" + sqlPart.toString());
        }
    }


    public static Range getInsertColumnNamesRange(SqlPart sqlPart) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.Insert) {
            String token = null;

            //List<String> colNameList = new ArrayList<String>();
            Range range = new Range();

            //0:searching 'into' 1:searching '(' 2:searching 'values' 3:end
            int searchStatus = 0;

            for (int i = 0; i < sqlPart.tokenList.size(); i++) {
                token = sqlPart.tokenList.get(i);

                if (searchStatus == 0) {
                    if (token.equalsIgnoreCase("into")) {
                        searchStatus++;
                    }
                } else if (searchStatus == 1) {
                    if (token.equalsIgnoreCase("(")) {
                        searchStatus++;

                        range.start = i + 1;
                    }
                } else if (searchStatus == 2) {
                    if (token.equalsIgnoreCase(")")) {
                        searchStatus++;

                        range.end = i - 1;
                    } else {
//						if(!token.equals(',')) {
//							colNameList.add(token);
//						}
                    }
                } else if (searchStatus == 3) {
                    break;
                }
            }

            return range;
        } else {
            throw new SqlParseException("SqlPart should be insert part:" + sqlPart.toString());
        }
    }

    public static Range getInsertColumnValuesRange(SqlPart sqlPart) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.Values) {
            String token = null;

            //List<String> colNameList = new ArrayList<String>();
            Range range = new Range();

            //0:searching 'values' 1:searching '(' 2:searching ')' 3:end
            int searchStatus = 0;

            for (int i = 0; i < sqlPart.tokenList.size(); i++) {
                token = sqlPart.tokenList.get(i);

                if (searchStatus == 0) {
                    if (token.equalsIgnoreCase("values")) {
                        searchStatus++;
                    }
                } else if (searchStatus == 1) {
                    if (token.equalsIgnoreCase("(")) {
                        searchStatus++;

                        range.start = i + 1;
                        break;
                    }
                }
            }

            range.end = sqlPart.tokenList.size() - 2;

            return range;
        } else {
            throw new SqlParseException("SqlPart should be insert part:" + sqlPart.toString());
        }
    }

    public static void appendConditionToJoinPart(SqlPart sqlPart,
                                                 boolean isAppendAsOrCondition, String conditionExpression) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.Join) {
            appendConditionToWherePart("on", sqlPart, isAppendAsOrCondition, conditionExpression);
        } else {
            throw new SqlParseException("SqlPart should be join part:" + sqlPart.toString());
        }

    }

    public static void appendConditionToWherePart(SqlPart sqlPart,
                                                  boolean isAppendAsOrCondition, String conditionExpression) throws SqlParseException {
        if (sqlPart.partType == SqlPartType.Where) {
            appendConditionToWherePart("where", sqlPart, isAppendAsOrCondition, conditionExpression);
        } else {
            throw new SqlParseException("SqlPart should be join part:" + sqlPart.toString());
        }
    }

    private static void appendConditionToWherePart(String conditionStartKeyword, SqlPart sqlPart,
                                                   boolean isAppendAsOrCondition, String conditionExpression) throws SqlParseException {
        String token = null;

        for (int i = 0; i < sqlPart.tokenList.size(); i++) {
            token = sqlPart.tokenList.get(i);

            if (token.equalsIgnoreCase(conditionStartKeyword)) {
                sqlPart.tokenList.add(i + 1, "(");
                break;
            }
        }

        sqlPart.tokenList.add(")");

        if (isAppendAsOrCondition) {
            sqlPart.tokenList.add("or");
        } else {
            sqlPart.tokenList.add("and");
        }

        sqlPart.tokenList.add("(");

        StringTokenizer stk = new StringTokenizer(conditionExpression, " ");
        while (stk.hasMoreTokens()) {
            sqlPart.tokenList.add(stk.nextToken());
        }

        sqlPart.tokenList.add(")");
    }
}
