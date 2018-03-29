package com.salama.easysqlparser.update;

import com.salama.easysqlparser.base.SqlObj;
import com.salama.easysqlparser.base.SqlPartKeyword;
import com.salama.easysqlparser.util.SqlParseException;

public class UpdateSql extends SqlObj {
    //private final static Log logger = LogFactory.getLog(UpdateSql.class);

    private static SqlPartKeyword[] SqlPartKeywords = null;
    private static SqlPartKeyword[] InvalidSqlPartKeywords = null;

    static {
        try {
            SqlPartKeywords = new SqlPartKeyword[]{
                    new SqlPartKeyword("update"),
                    new SqlPartKeyword("set"),
                    new SqlPartKeyword("where"),
                    new SqlPartKeyword("order"),
                    new SqlPartKeyword("limit")
            };

            InvalidSqlPartKeywords = new SqlPartKeyword[]{
                    new SqlPartKeyword("insert"),
                    new SqlPartKeyword("into"),
                    new SqlPartKeyword("values"),
                    new SqlPartKeyword("select"),
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

    public UpdateSql(String sql) throws SqlParseException {
        super(sql);

        parseToSqlPart(SqlPartKeywords, InvalidSqlPartKeywords);
    }

}
