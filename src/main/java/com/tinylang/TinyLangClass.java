package com.tinylang;

import java.util.List;
import java.util.Map;

public class TinyLangClass implements TinyLangCallable {

    private final String name;
    private final Map<String, TinyLangFunction> methods;

    TinyLangClass(String name, Map<String, TinyLangFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public String toString() {
        return name;
    }

    public String name() {
        return name;
    }

    public TinyLangFunction findMethod(String init) {
        if (methods.containsKey(init)) {
            return methods.get(init);
        }
        return null;
    }

    @Override
    public int arity() {
        TinyLangFunction initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        TinyLangInstance instance = new TinyLangInstance(this);
        TinyLangFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }
}
