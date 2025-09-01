package com.tinylang;

import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;
import com.tinylang.token.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Resolver implements Stmt.Visitor<Void>, Expr.Visitor<Void> {

    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes = new Stack<>();
    private FunctionType currentFunctionType = FunctionType.NULL;
    private ClassType currentClassType = ClassType.NULL;

    public Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Void visitBinaryExpr(Expr.BinaryExpr expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visitUnaryExpr(Expr.UnaryExpr expr) {
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visitVarExpr(Expr.VarExpr expr) {
        if (!scopes.isEmpty() && scopes.peek().get(expr.name.lexeme()) == Boolean.FALSE) {
            System.err.println("Error: Can't read local variable in its own initializer.");
        }
        resolveLocal(expr, expr.name);
        return null;
    }

    @Override
    public Void visitAssignExpr(Expr.AssignExpr expr) {
        resolve(expr.value);
        resolveLocal(expr, expr.name);
        return null;
    }

    @Override
    public Void visitCallExpr(Expr.CallExpr expr) {
        resolve(expr.callee);
        for (Expr argument : expr.arguments) {
            resolve(argument);
        }
        return null;
    }

    @Override
    public Void visitGroupingExpr(Expr.GroupingExpr expr) {
        resolve(expr.expression);
        return null;
    }

    @Override
    public Void visitFunctionExpr(Expr.Function expr) {
        FunctionType enclosingFunction = currentFunctionType;
        currentFunctionType = FunctionType.FUNCTION;
        beginScope();
        for (String param : expr.params) {
            declare(param);
            define(param);
        }
        resolve(expr.body);
        endScope();
        currentFunctionType = enclosingFunction;
        return null;
    }

    @Override
    public Void visitLiteralExpr(Expr.LiteralExpr expr) {
        return null;
    }

    @Override
    public Void visitLogicalExpr(Expr.Logical logical) {
        resolve(logical.left);
        resolve(logical.right);
        return null;
    }

    @Override
    public Void visitGetExpr(Expr.GetExpr expr) {
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visitSetExpr(Expr.SetExpr setExpr) {
        resolve(setExpr.value);
        resolve(setExpr.object);
        return null;
    }

    @Override
    public Void visitThisExpr(Expr.ThisExpr thisExpr) {
        if (currentClassType == ClassType.NULL) {
            TinyLang.error("Can't use 'this' outside of a class.");
            return null;
        }
        resolveLocal(thisExpr, thisExpr.keyword);
        return null;
    }

    @Override
    public Void visitLetStmt(Stmt.Let stmt) {
        declare(stmt.name);
        if (stmt.initializer != null) {
            resolve(stmt.initializer);
        }
        define(stmt.name);
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        resolve(stmt.condition);
        resolve(stmt.thenBranch);
        if (stmt.elseBranch != null) {
            resolve(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        resolve(stmt.condition);
        resolve(stmt.body);
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        if (currentFunctionType == FunctionType.NULL) {
            TinyLang.error("Can't return from top-level code.");
        }
        if (currentFunctionType == FunctionType.INITIALIZER) {
            TinyLang.error("Can't return a value from an initializer.");
        }
        if (stmt.value != null) {
            resolve(stmt.value);
        }
        return null;
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        beginScope();
        resolve(stmt.statements);
        endScope();
        return null;
    }

    @Override
    public Void visitClassStmt(Stmt.Class stmt) {
        ClassType enclosingClassType = currentClassType;
        currentClassType = ClassType.CLASS;
        declare(stmt.name.lexeme());
        define(stmt.name.lexeme());
        beginScope();
        scopes.peek().put("this", true);
        for (Stmt.Function method : stmt.methods) {
            FunctionType type = method.name.equals("init") ? FunctionType.INITIALIZER : FunctionType.METHOD;
            resolveFunction(method, type);
        }
        endScope();
        currentClassType = enclosingClassType;
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        declare(stmt.name);
        define(stmt.name);
        resolveFunction(stmt, FunctionType.FUNCTION);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        resolve(stmt.expression);
        return null;
    }

    private void resolveLocal(Expr expr, Token name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name.lexeme())) {
                interpreter.resolve(expr, scopes.size() - 1 - i);
                return;
            }
        }
    }

    public void resolve(List<Stmt> statements) {
        for (Stmt statement : statements) {
            resolve(statement);
        }
    }

    public void resolve(Stmt statement) {
        statement.accept(this);
    }

    public void resolve(Expr expression) {
        expression.accept(this);
    }

    private void resolveFunction(Stmt.Function stmt, FunctionType functionType) {
        FunctionType enclosingFunction = currentFunctionType;
        currentFunctionType = functionType;
        beginScope();
        for (String param : stmt.params) {
            declare(param);
            define(param);
        }
        resolve(stmt.body);
        endScope();
        currentFunctionType = enclosingFunction;
    }

    private void endScope() {
        scopes.pop();
    }

    private void beginScope() {
        scopes.push(new HashMap<>());
    }

    private void define(String name) {
        if (scopes.isEmpty()) return;
        scopes.peek().put(name, true);
    }

    private void declare(String name) {
        if (scopes.isEmpty()) return;
        Map<String, Boolean> scope = scopes.peek();
        if (scope.containsKey(name)) {
            TinyLang.error("Variable with " + name + " already declared in this scope.");
        }
        scope.put(name, false);
    }
}
