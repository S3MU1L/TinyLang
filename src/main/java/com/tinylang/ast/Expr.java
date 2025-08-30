package com.tinylang.ast;

import com.tinylang.token.Token;
import com.tinylang.token.Token;

import java.util.List;

public abstract class Expr {

    public interface Visitor<R> {
        R visitBinaryExpr(BinaryExpr expr);

        R visitUnaryExpr(UnaryExpr expr);

        R visitVarExpr(VarExpr expr);

        R visitAssignExpr(AssignExpr expr);

        R visitCallExpr(CallExpr expr);

        R visitGroupingExpr(GroupingExpr expr);

        R visitFunctionExpr(Function expr);

        R visitLiteralExpr(LiteralExpr expr);

        R visitLogicalExpr(Logical logical);
    }

    public abstract <R> R accept(Visitor<R> visitor);

    public static class LiteralExpr extends Expr {
        public final Object value;

        public LiteralExpr(Object value) {
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    public static class BinaryExpr extends Expr {
        public final Expr left;
        public final Token operator;
        public final Expr right;

        public BinaryExpr(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class Logical extends Expr {
        public final Expr left;
        public final Token operator;
        public final Expr right;

        public Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }
    }

    public static class UnaryExpr extends Expr {
        public final Token operator;
        public final Expr right;

        public UnaryExpr(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public static class VarExpr extends Expr {
        public final Token name;

        public VarExpr(Token name) {
            this.name = name;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarExpr(this);
        }
    }

    public static class AssignExpr extends Expr {
        public final Token name;
        public final Expr value;

        public AssignExpr(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }
    }

    public static class CallExpr extends Expr {
        public final Expr callee;
        public final Token paren;
        public final List<Expr> arguments;

        public CallExpr(Expr callee, Token paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }
    }

    public static class GroupingExpr extends Expr {
        public final Expr expression;

        public GroupingExpr(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    public static class Function extends Expr {
        public final List<String> params;
        public final Stmt body;

        public Function(List<String> params, Stmt body) {
            this.params = params;
            this.body = body;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionExpr(this);
        }
    }
}
