package com.parser.ast;

import java.util.List;

public abstract class Expr {

    public interface Visitor<R> {
        R visitBinaryExpr(BinaryExpr expr);

        R visitUnaryExpr(UnaryExpr expr);

        R visitVarExpr(VarExpr expr);

        R visitAssignExpr(AssignExpr expr);

        R visitCallExpr(callExpr expr);

        R visitGroupingExpr(GroupingExpr expr);

        R visitFunctionExpr(Function expr);
    }

    abstract <R> R accept(Visitor<R> visitor);

    public static class BinaryExpr extends Expr {
        public final Expr left;
        public final String operator;
        public final Expr right;

        public BinaryExpr(Expr left, String operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class UnaryExpr extends Expr {
        public final String operator;
        public final Expr right;

        public UnaryExpr(String operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public static class VarExpr extends Expr {
        public final String name;

        public VarExpr(String name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarExpr(this);
        }
    }

    public static class AssignExpr extends Expr {
        public final String name;
        public final Expr value;

        public AssignExpr(String name, Expr value) {
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }
    }

    public static class callExpr extends Expr {
        public final Expr callee;
        public final String paren;
        public final List<Expr> arguments;

        public callExpr(Expr callee, String paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }
    }

    public static class GroupingExpr extends Expr {
        public final Expr expression;

        public GroupingExpr(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    public static class Function extends Expr {
        public final List<String> params;
        public final Stmt.Block body;

        public Function(List<String> params, Stmt.Block body) {
            this.params = params;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionExpr(this);
        }
    }
}
