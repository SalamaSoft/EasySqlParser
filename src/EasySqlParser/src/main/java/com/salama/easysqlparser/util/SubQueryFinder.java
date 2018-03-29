package com.salama.easysqlparser.util;

import java.util.ArrayList;
import java.util.List;

public class SubQueryFinder {
    private final static char SubQueryBlockTokenCharStart0 = '(';
    private final static char SubQueryBlockTokenCharEnd = ')';

    //private final static String SubQueryBlockTokenStart1 = "select";
    private final static char[] CHARS_SELECT_LOWER = new char[]{'s', 'e', 'l', 'e', 'c', 't'};
    private final static char[] CHARS_SELECT_UPPER = new char[]{'S', 'E', 'L', 'E', 'C', 'T'};

    public static List<Range> findAllSubQuery(StringBuilder sql) {
        int searchBegin = 0;
        int posTmp = 0;

        int blockStart = 0;
        int blockEnd = 0;

        int blockTokenCount = 0;

        //find 1st select
        posTmp = indexOfSelect(sql, 0);
        if (posTmp < 0) {
            return null;
        } else {
            searchBegin = posTmp + CHARS_SELECT_LOWER.length;
        }

        List<Range> rangeList = null;
        while (true) {
            if (blockTokenCount == 0) {
                blockStart = 0;
                blockEnd = 0;

                posTmp = indexOfSelect(sql, searchBegin);

                if (posTmp < 0) {
                    break;
                }

                //search back for block start
                blockStart = searchBackBlockStart(sql, posTmp - 1);
                if (blockStart < 0) {
                    searchBegin += CHARS_SELECT_LOWER.length;
                } else {
                    blockTokenCount++;
                }
            } else {
                //search for block end
                char c;
                int i;
                for (i = blockStart + 1; i < sql.length(); i++) {
                    c = sql.charAt(i);

                    if (c == SubQueryBlockTokenCharStart0) {
                        blockTokenCount++;
                    } else if (c == SubQueryBlockTokenCharEnd) {
                        blockTokenCount--;

                        if (blockTokenCount == 0) {
                            blockEnd = i + 1;

                            if (rangeList == null) {
                                if (checkIfSubQueryIsWholeQuery(sql, blockStart, blockEnd)) {
                                    //this is the whole query, so do not take as sub query
                                    return null;
                                }

                                rangeList = new ArrayList<Range>();
                            }

                            rangeList.add(new Range(blockStart, blockEnd));
                            break;
                        }
                    }
                }

                searchBegin = i;
            }
        }

        return rangeList;
    }

    private static int indexOfSelect(StringBuilder sql, int beginIndex) {
        int i = beginIndex;
        int len = sql.length();
        char c;
        int matchedCnt = 0;
        int matchedMaxCnt = CHARS_SELECT_LOWER.length - 1;

        for (i = beginIndex; i < len; i++) {
            c = sql.charAt(i);

            if (c == CHARS_SELECT_LOWER[matchedCnt] || c == CHARS_SELECT_UPPER[matchedCnt]) {
                if (matchedCnt == matchedMaxCnt) {
                    return i - matchedMaxCnt;
                } else {
                    matchedCnt++;
                }
            } else {
                matchedCnt = 0;
            }
        }

        return -1;
    }

    private static boolean checkIfSubQueryIsWholeQuery(StringBuilder sql, int blockStart, int blockEnd) {
        char c;
        int i;
        boolean fromBeginning = true;
        boolean toEnd = true;

        for (i = blockStart - 1; i >= 0; i--) {
            c = sql.charAt(i);

            if (c == '\t' || c == ' ' || c == '\n' || c == '\r') {
                continue;
            } else {
                fromBeginning = false;
                break;
            }
        }

        for (i = blockEnd + 1; i < sql.length(); i++) {
            c = sql.charAt(i);

            if (c == '\t' || c == ' ' || c == '\n' || c == '\r') {
                continue;
            } else {
                toEnd = false;
                break;
            }
        }

        return (fromBeginning && toEnd);
    }

    private static int searchBackBlockStart(StringBuilder sql, int begin) {
        char c;
        for (int i = begin; i >= 0; i--) {
            c = sql.charAt(i);
            if (c == SubQueryBlockTokenCharStart0) {
                return i;
            } else if (c == '\t' || c == ' ' || c == '\n' || c == '\r') {
                continue;
            } else {
                return -1;
            }
        }

        return -1;
    }
}
