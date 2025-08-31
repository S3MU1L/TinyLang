package com.tinylang;

import com.tinylang.token.Token;
import com.tinylang.token.TokenType;

import java.util.*;

import static com.tinylang.token.TokenType.*;
import static java.lang.Character.isDigit;

public class Lexer {

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fn", FN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("while", WHILE);
        keywords.put("let", LET);
    }

    private int start = 0;
    private int current = 0;
    private int line = 1;
    private final String content;
    private final List<Token> tokens;

    public Lexer(String content) {
        this.content = content;
        this.tokens = new ArrayList<>();
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' -> addToken(TokenType.LEFT_PAREN);
            case ')' -> addToken(TokenType.RIGHT_PAREN);
            case '{' -> addToken(TokenType.LEFT_BRACE);
            case '}' -> addToken(TokenType.RIGHT_BRACE);
            case ',' -> addToken(TokenType.COMMA);
            case '.' -> addToken(TokenType.DOT);
            case '-' -> addToken(TokenType.MINUS);
            case '+' -> addToken(TokenType.PLUS);
            case ';' -> addToken(TokenType.SEMICOLON);
            case '%' -> addToken(TokenType.PERCENT);
            case '*' -> addToken(match('*') ? TokenType.STAR_STAR : TokenType.STAR);
            case '!' -> addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
            case '=' -> addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
            case '<' -> addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
            case '>' -> addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
            case '/' -> {
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(TokenType.SLASH);
                }
            }
            case ' ', '\r', '\t' -> {
                // Ignore whitespace.
            }
            case '\n' -> line++;
            case '"' -> string();
            default -> {
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    TinyLang.error(line, "Unexpected character.");
                }
            }
        }
    }

    private boolean isAlpha(char c) {
        return Character.isAlphabetic(c) || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return content.charAt(current);
    }

    private char advance() {
        return content.charAt(current++);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (content.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private void addToken(TokenType tokenType) {
        addToken(tokenType, null);
    }

    private void addToken(TokenType tokenType, Object literal) {
        String text = content.substring(start, current);
        tokens.add(new Token(tokenType, text, literal, line));
    }

    private boolean isAtEnd() {
        return current >= content.length();
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            TinyLang.error(line, "Unterminated string.");
            return;
        }

        advance();
        addToken(TokenType.STRING, content.substring(start + 1, current - 1));
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek())) {
                advance();
            }
        }
        addToken(TokenType.NUMBER, Double.parseDouble(content.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= content.length()) return '\0';
        return content.charAt(current + 1);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        String text = content.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) {
            type = TokenType.IDENTIFIER;
        }
        addToken(type, text);
    }
}
