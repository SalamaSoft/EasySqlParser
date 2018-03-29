package com.salama.easysqlparser.util;

public class SqlParseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -1072517561693834578L;

    public SqlParseException() {
        super();
    }

    public SqlParseException(String msg) {
        super(msg);
    }

    public SqlParseException(Throwable t) {
        super(t);
    }

    public SqlParseException(String msg, Throwable t) {
        super(msg, t);
    }
}
