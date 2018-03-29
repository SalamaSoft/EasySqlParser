package com.salama.easysqlparser.util;

import java.util.ArrayList;
import java.util.List;

public class LiteralsFinder {
    private static final char LITERAL_TOKEN_CHAR = '\'';

    public static List<Range> findAllLiterals(StringBuilder input) throws SqlParseException {
        List<Range> rangeList = null;

        char c;
        int curPos = 0;
        int nextPos = 0;
        int begin = -1;
        while (curPos < input.length()) {
            c = input.charAt(curPos);

            if (begin < 0) {
                //repalce these empty char
                if (c == '\r' || c == '\n' || c == '\t') {
                    input.setCharAt(curPos, ' ');
                } else if (c == LITERAL_TOKEN_CHAR) {
                    begin = curPos;
                } else {
                    //do nothing
                }
            } else {
                if (c == LITERAL_TOKEN_CHAR) {
                    nextPos = curPos + 1;

                    if (nextPos < input.length() && input.charAt(nextPos) == LITERAL_TOKEN_CHAR) {
                        //escape chars, then move to next char
                        curPos++;
                    } else {
                        //end this time
                        if (rangeList == null) {
                            rangeList = new ArrayList<Range>();
                        }

                        rangeList.add(new Range(begin, curPos + 1));

                        //reset begin
                        begin = -1;
                    }
                } else {
                    //do nothing
                }
            }

            curPos++;
        }

        if (begin >= 0) {
            throw new SqlParseException("Format is incorrect because of count of '");
        }

        return rangeList;
    }
}
