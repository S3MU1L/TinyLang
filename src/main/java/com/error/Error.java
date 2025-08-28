package com.error;

/**
 * The lineNum attribute for syntax errors is set by the {@link com.lexer.Lexer}.
 * We do not track the line numbers of statements in the interpreter,
 * and thus runtime errors always have line_no = 0.
 */

public class Error {

    private final String message;
    private final int lineNum;

    public Error(String message, int lineNum) {
        this.message = message;
        this.lineNum = lineNum;
    }

    @Override
    public String toString() {
        String kind;
        if (lineNum == 0) {
            kind = "Runtime error: ";
        } else {
            kind = "Syntax error on line " + Integer.toString(lineNum) + ": ";
        }
        return kind + message;
    }
}
