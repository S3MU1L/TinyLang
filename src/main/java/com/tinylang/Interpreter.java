package com.tinylang;

import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;
import com.tinylang.error.Return;
import com.tinylang.error.RuntimeError;
import com.tinylang.token.Token;
import com.tinylang.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private Environment environment = new Environment();

    @Override
    public Object visitBinaryExpr(Expr.BinaryExpr expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type()) {
            case PLUS -> {
                if (left instanceof String || right instanceof String) {
                    return String.valueOf(left) + String.valueOf(right);
                }
                checkNumberOperands(expr.operator, left, right);
                return (Double) left + (Double) right;
            }
            case MINUS -> {
                checkNumberOperands(expr.operator, left, right);
                return (Double) left - (Double) right;
            }
            case STAR -> {
                checkNumberOperands(expr.operator, left, right);
                return (Double) left * (Double) right;
            }
            case SLASH -> {
                checkNumberOperands(expr.operator, left, right);
                if ((Double) right == 0) {
                    throw new RuntimeError(expr.operator, "Division by zero.");
                }
                return (Double) left / (Double) right;
            }
            case STAR_STAR -> {
                checkNumberOperands(expr.operator, left, right);
                return Math.pow((Double) left, (Double) right);
            }
            case GREATER -> {
                checkNumberOperands(expr.operator, left, right);
                return (Double) left > (Double) right;
            }
            case GREATER_EQUAL -> {
                checkNumberOperands(expr.operator, left, right);
                return (Double) left >= (Double) right;
            }
            case LESS -> {
                checkNumberOperands(expr.operator, left, right);
                return (Double) left < (Double) right;
            }
            case LESS_EQUAL -> {
                checkNumberOperands(expr.operator, left, right);
                return (Double) left <= (Double) right;
            }
            case EQUAL_EQUAL -> {
                return isEqual(left, right);
            }
            case BANG_EQUAL -> {
                return !isEqual(left, right);
            }
        }
        return null;
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null) return false;
        return left.equals(right);
    }

    @Override
    public Object visitUnaryExpr(Expr.UnaryExpr expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type()) {
            case MINUS -> {
                checkNumberOperand(expr.operator, right);
                return -(Double) right;
            }
            case BANG -> {
                return !isTruthy(right);
            }
        }
        return null;
    }

    @Override
    public Object visitVarExpr(Expr.VarExpr expr) {
        return environment.get(expr.name);
    }

    @Override
    public Object visitAssignExpr(Expr.AssignExpr expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    @Override
    public Object visitCallExpr(Expr.CallExpr expr) {
        Object calle = evaluate(expr.callee);
        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }
        if (!(calle instanceof TinyLangCallable function)) {
            throw new RuntimeError(expr.paren, "Can only call functions and classes.");
        }

        if (arguments.size() != function.arity()) {
            throw new RuntimeError(expr.paren, "Expected " + function.arity() + " arguments but got " + arguments.size() + ".");
        }

        return function.call(this, arguments);
    }

    @Override
    public Object visitGroupingExpr(Expr.GroupingExpr expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitFunctionExpr(Expr.Function expr) {
        return AnonymousFunctionAdapter.adapt(expr, environment);
    }

    @Override
    public Object visitLiteralExpr(Expr.LiteralExpr expr) {
        return expr.value;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical logical) {
        Object left = evaluate(logical.left);
        if (logical.operator.type() == TokenType.OR) {
            if (isTruthy(left)) return left;
        } else {
            if (!isTruthy(left)) return left;
        }
        return evaluate(logical.right);
    }

    @Override
    public Void visitLetStmt(Stmt.Let stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name, value);
        return null;
    }

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        if (isTruthy(stmt.condition)) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        Object value = null;
        if (stmt.value != null) {
            value = evaluate(stmt.value);
        }

        throw new Return(value);
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitClassStmt(Stmt.Class stmt) {
        // TODO implement classes
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        TinyLangFunction function = new TinyLangFunction(stmt, environment);
        environment.define(stmt.name, function);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    private boolean isTruthy(Object right) {
        if (right == null) return false;
        if (right instanceof Boolean b) return b;
        return true;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand of '" + operator + "' must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands of '" + operator + "' must be numbers.");
    }

    public void interpret(Stmt statement) {
        try {
            execute(statement);
        } catch (RuntimeError e) {
            TinyLang.runtimeError(e);
        }
    }

    public void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        this.environment = environment;
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    private void execute(Stmt statement) {
        statement.accept(this);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    public void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError e) {
            TinyLang.runtimeError(e);
        }
    }

    private String stringify(Object object) {
        if (object == null) return "nil";
        if (object instanceof Double d) {
            String text = d.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }

    public void interpret(List<Stmt> statements) {
        for (Stmt statement : statements) {
            execute(statement);
        }
    }
}
