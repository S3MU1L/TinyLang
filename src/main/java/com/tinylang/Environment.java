package com.tinylang;

import com.tinylang.error.RuntimeError;
import com.tinylang.token.Token;

import java.util.HashMap;

public class Environment {

    private final Environment enclosing;
    private final HashMap<String, Object> values = new HashMap<>();

    public Environment() {
        this.enclosing = null;
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme())) {
            values.put(name.lexeme(), value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme() + "'.");
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme())) {
            return values.get(name.lexeme());
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme() + "'.");
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
