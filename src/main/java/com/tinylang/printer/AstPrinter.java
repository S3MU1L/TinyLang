package com.tinylang.printer;

import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;

import java.util.List;

public class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {

    public static void print(List<Stmt> statements) {
        AstPrinter printer = new AstPrinter();
        for (Stmt stmt : statements) {
            System.out.println(printer.print(stmt));
        }
    }

    public String print(Stmt stmt) {
        return stmt.accept(this);
    }

    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitLetStmt(Stmt.Let stmt) {
        return "(let " + stmt.name + " " + (stmt.initializer != null ? print(stmt.initializer) : "nil") + ")";
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return print(stmt.expression);
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        String result = "(if " + print(stmt.condition) + " " + print(stmt.thenBranch);
        if (stmt.elseBranch != null) {
            result += " " + print(stmt.elseBranch);
        }
        return result + ")";
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
        return "(while " + print(stmt.condition) + " " + print(stmt.body) + ")";
    }

    @Override
    public String visitReturnStmt(Stmt.Return stmt) {
        return "(return " + (stmt.value != null ? print(stmt.value) : "nil") + ")";
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(block");
        for (Stmt statement : stmt.statements) {
            builder.append(" ").append(print(statement));
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitClassStmt(Stmt.Class stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(class ").append(stmt.name);
        for (Stmt.Function method : stmt.methods) {
            builder.append(" ").append(print(method));
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitFunctionStmt(Stmt.Function stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(fn ").append(stmt.name).append(" (");
        for (int i = 0; i < stmt.params.size(); i++) {
            builder.append(stmt.params.get(i));
            if (i < stmt.params.size() - 1) {
                builder.append(" ");
            }
        }
        builder.append(") ").append(print(stmt.body)).append(")");
        return builder.toString();
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return "(print " + print(stmt.expression) + ")";
    }

    @Override
    public String visitBinaryExpr(Expr.BinaryExpr expr) {
        return "(" + expr.operator + " " + print(expr.left) + " " + print(expr.right) + ")";
    }

    @Override
    public String visitUnaryExpr(Expr.UnaryExpr expr) {
        return "(" + expr.operator + " " + print(expr.right) + ")";
    }

    @Override
    public String visitVarExpr(Expr.VarExpr expr) {
        return expr.name.lexeme();
    }

    @Override
    public String visitAssignExpr(Expr.AssignExpr expr) {
        return "(assign " + expr.name + " " + print(expr.value) + ")";
    }

    @Override
    public String visitCallExpr(Expr.CallExpr expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("(call ").append(print(expr.callee));
        for (Expr argument : expr.arguments) {
            builder.append(" ").append(print(argument));
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitGroupingExpr(Expr.GroupingExpr expr) {
        return "(group " + print(expr.expression) + ")";
    }

    @Override
    public String visitFunctionExpr(Expr.Function expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("(fn (");
        for (int i = 0; i < expr.params.size(); i++) {
            builder.append(expr.params.get(i));
            if (i < expr.params.size() - 1) {
                builder.append(" ");
            }
        }
        builder.append(") ").append(print(expr.body)).append(")");
        return builder.toString();
    }

    @Override
    public String visitLiteralExpr(Expr.LiteralExpr expr) {
        return expr.value != null ? expr.value.toString() : "nil";
    }

    @Override
    public String visitLogicalExpr(Expr.Logical logical) {
        return "(" + logical.operator + " " + print(logical.left) + " " + print(logical.right) + ")";
    }
}
