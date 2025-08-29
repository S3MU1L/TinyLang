package com.tinylang.visualize.serialization;

import com.google.gson.*;
import com.tinylang.ast.Expr;

import java.lang.reflect.Type;

public class ExprSerializer implements JsonSerializer<Expr> {

    @Override
    public JsonElement serialize(Expr src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        if (src instanceof Expr.LiteralExpr lit) {
            obj.addProperty("type", "LiteralExpr");
            obj.add("value", context.serialize(lit.value));
        } else if (src instanceof Expr.BinaryExpr bin) {
            obj.addProperty("type", "BinaryExpr");
            obj.add("left", context.serialize(bin.left));
            obj.addProperty("operator", bin.operator.name());
            obj.add("right", context.serialize(bin.right));
        } else if (src instanceof Expr.UnaryExpr unary) {
            obj.addProperty("type", "UnaryExpr");
            obj.addProperty("operator", unary.operator.name());
            obj.add("right", context.serialize(unary.right));
        } else if (src instanceof Expr.VarExpr var) {
            obj.addProperty("type", "VarExpr");
            obj.addProperty("name", var.name);
        } else if (src instanceof Expr.AssignExpr assign) {
            obj.addProperty("type", "AssignExpr");
            obj.addProperty("name", assign.name);
            obj.add("value", context.serialize(assign.value));
        } else if (src instanceof Expr.CallExpr call) {
            obj.addProperty("type", "CallExpr");
            obj.add("callee", context.serialize(call.callee));
            obj.addProperty("paren", call.paren);
            obj.add("arguments", context.serialize(call.arguments));
        } else if (src instanceof Expr.GroupingExpr group) {
            obj.addProperty("type", "GroupingExpr");
            obj.add("expression", context.serialize(group.expression));
        } else if (src instanceof Expr.Function fn) {
            obj.addProperty("type", "Function");
            obj.add("params", context.serialize(fn.params));
            obj.add("body", context.serialize(fn.body));
        }
        return obj;
    }
}
