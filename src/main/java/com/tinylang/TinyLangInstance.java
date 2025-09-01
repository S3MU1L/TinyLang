package com.tinylang;

import com.tinylang.error.RuntimeError;
import com.tinylang.token.Token;

public class TinyLangInstance {

    private final TinyLangClass klass;
    private final java.util.Map<String, Object> fields = new java.util.HashMap<>();

    TinyLangInstance(TinyLangClass klass) {
        this.klass = klass;
    }

    Object get(Token name) {
        if (fields.containsKey(name.lexeme())) {
            return fields.get(name.lexeme());
        }

        TinyLangFunction method = klass.findMethod(name.lexeme());
        if (method != null) {
            return method.bind(this);
        }

        throw new RuntimeError(name, "Undefined property '" + name.lexeme() + "'.");
    }

    void set(String name, Object value) {
        fields.put(name, value);
    }

    @Override
    public String toString() {
        return klass.name() + " instance";
    }
}
