package com.tinylang.visualize.deserialization;

import com.google.gson.*;
import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;
import com.tinylang.token.TokenType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExprDeserializer implements JsonDeserializer<Expr> {

    @Override
    public Expr deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String exprType = obj.get("type").getAsString();

        switch (exprType) {
            case "LiteralExpr":
                Object value = context.deserialize(obj.get("value"), Object.class);
                return new Expr.LiteralExpr(value);
            case "BinaryExpr":
                Expr left = context.deserialize(obj.get("left"), Expr.class);
                TokenType operator = TokenType.valueOf(obj.get("operator").getAsString());
                Expr right = context.deserialize(obj.get("right"), Expr.class);
                return new Expr.BinaryExpr(left, operator, right);
            case "UnaryExpr":
                TokenType unaryOp = TokenType.valueOf(obj.get("operator").getAsString());
                Expr unaryRight = context.deserialize(obj.get("right"), Expr.class);
                return new Expr.UnaryExpr(unaryOp, unaryRight);
            case "VarExpr":
                String name = obj.get("name").getAsString();
                return new Expr.VarExpr(name);
            case "AssignExpr":
                String assignName = obj.get("name").getAsString();
                Expr assignValue = context.deserialize(obj.get("value"), Expr.class);
                return new Expr.AssignExpr(assignName, assignValue);
            case "CallExpr":
                Expr callee = context.deserialize(obj.get("callee"), Expr.class);
                String paren = obj.get("paren").getAsString();
                List<Expr> arguments = new ArrayList<>();
                JsonArray argsArray = obj.getAsJsonArray("arguments");
                for (JsonElement arg : argsArray) {
                    arguments.add(context.deserialize(arg, Expr.class));
                }
                return new Expr.CallExpr(callee, paren, arguments);
            case "GroupingExpr":
                Expr expression = context.deserialize(obj.get("expression"), Expr.class);
                return new Expr.GroupingExpr(expression);
            case "Function":
                List<String> params = new ArrayList<>();
                JsonArray paramsArray = obj.getAsJsonArray("params");
                for (JsonElement param : paramsArray) {
                    params.add(param.getAsString());
                }
                Stmt body = context.deserialize(obj.get("body"), Stmt.class);
                return new Expr.Function(params, body);
            default:
                throw new JsonParseException("Unknown Expr type: " + exprType);
        }
    }
}
