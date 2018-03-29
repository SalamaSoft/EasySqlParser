package com.salama.easysqlparser.base;

import com.salama.easysqlparser.query.SelectQuery;
import com.salama.easysqlparser.util.SqlParseException;

public interface ISelectQueryHandler {
    void handleQuery(SelectQuery query) throws SqlParseException;
}
