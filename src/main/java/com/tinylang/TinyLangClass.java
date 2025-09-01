package com.tinylang;

import java.util.List;
import java.util.Map;

public class TinyLangClass implements TinyLangCallable {

    private final String name;
    private final Map<String, TinyLangFunction> methods;
    private final Map<String, TinyLangFunction> staticMethods;
    private final Object superclass;

    TinyLangClass(String name, Object superclass, Map<String, TinyLangFunction> methods, Map<String, TinyLangFunction> staticMethods) {
        this.name = name;
        this.methods = methods;
        this.staticMethods = staticMethods;
        this.superclass = superclass;
    }

    @Override
    public String toString() {
        return name;
    }

    public String name() {
        return name;
    }

    public TinyLangFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        if (staticMethods.containsKey(name)) {
            return staticMethods.get(name);
        }
        if (superclass != null) {
            return ((TinyLangClass) superclass).findMethod(name);
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
