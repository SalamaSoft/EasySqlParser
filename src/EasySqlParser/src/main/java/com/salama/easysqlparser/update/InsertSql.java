package com.salama.easysqlparser.update;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.salama.easysqlparser.base.SqlObj;
import com.salama.easysqlparser.base.SqlPartKeyword;
import com.salama.easysqlparser.util.SqlParseException;

public class InsertSql extends SqlObj {
    //private final static Log logger = LogFactory.getLog(DeleteSql.class);

    private static SqlPartKeyword[] SqlPartKeywords = null;
    private static SqlPartKeyword[] InvalidSqlPartKeywords = null;

    static {
        try {
            SqlPartKeywords = new SqlPartKeyword[]{
                    new SqlPartKeyword("insert"),
                    new SqlPartKeyword("into"),
                    new SqlPartKeyword("values")
            };

            InvalidSqlPartKeywords = new SqlPartKeyword[]{
                    new SqlPartKeyword("delete"),
                    new SqlPartKeyword("from"),
                    new SqlPartKeyword("select"),
                    new SqlPartKeyword("update"),
                    new SqlPartKeyword("truncate"),
                    new SqlPartKeyword("drop"),
                    new SqlPartKeyword("create"),
                    new SqlPartKeyword("using"),
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
        } catch (SqlParseException e) {
            //logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    ;

    public InsertSql(String sql) throws SqlParseException {
        super(sql);

        parseToSqlPart(SqlPartKeywords, InvalidSqlPartKeywords);
    }

}
