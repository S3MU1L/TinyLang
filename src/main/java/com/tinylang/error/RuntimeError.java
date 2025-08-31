package com.tinylang.error;

import com.tinylang.token.Token;

public class RuntimeError extends Error {

    private final Token token;

    public RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }

    public Token token() {
        return token;
    }
}
