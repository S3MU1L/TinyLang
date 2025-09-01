package com.tinylang;

import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;

public class AnonymousFunctionAdapter {

    private static int counter = 0;

    public static TinyLangCallable adapt(Expr.Function expr, Environment env) {
        String functionName = "<anonymous$" + counter + ">";
        Stmt.Function functionStmt = new Stmt.Function(functionName, expr.params, expr.body, false);
        counter++;
        return new TinyLangFunction(functionStmt, env, false);
    }
}
