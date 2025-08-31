package com.tinylang.token;

public enum TokenType {

    // Single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,
    BANG, EQUAL, LESS, GREATER, PERCENT,

    // One or two character tokens
    EQUAL_EQUAL, GREATER_EQUAL,
    LESS_EQUAL, BANG_EQUAL, STAR_STAR,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Keywords
    AND, LET, FN, RETURN, IF, ELSE, FOR, NIL, OR,
    WHILE, TRUE, FALSE, CLASS, PRINT, SUPER, THIS,

    EOF
}
