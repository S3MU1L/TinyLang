package com.tinylang;

import com.tinylang.ast.Stmt;
import com.tinylang.error.Return;

import java.util.List;

public class TinyLangFunction implements TinyLangCallable {

    private final Stmt.Function declaration;
    private final Environment closure;

    public TinyLangFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i), arguments.get(i));
        }
        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            return returnValue.value();
        }
        return null;
    }
}
