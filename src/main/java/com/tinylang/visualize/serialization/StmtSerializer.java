package com.tinylang.visualize.serialization;

import com.google.gson.*;
import com.tinylang.ast.Stmt;

import java.lang.reflect.Type;

public class StmtSerializer implements JsonSerializer<Stmt> {

    @Override
    public JsonElement serialize(Stmt src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        if (src instanceof Stmt.Let let) {
            obj.addProperty("type", "Let");
            obj.addProperty("name", let.name);
            obj.add("initializer", context.serialize(let.initializer));
        } else if (src instanceof Stmt.Expression expr) {
            obj.addProperty("type", "Expression");
            obj.add("expression", context.serialize(expr.expression));
        } else if (src instanceof Stmt.If ifStmt) {
            obj.addProperty("type", "If");
            obj.add("condition", context.serialize(ifStmt.condition));
            obj.add("thenBranch", context.serialize(ifStmt.thenBranch));
            obj.add("elseBranch", context.serialize(ifStmt.elseBranch));
        } else if (src instanceof Stmt.While whileStmt) {
            obj.addProperty("type", "While");
            obj.add("condition", context.serialize(whileStmt.condition));
            obj.add("body", context.serialize(whileStmt.body));
        } else if (src instanceof Stmt.Return ret) {
            obj.addProperty("type", "Return");
            obj.add("value", context.serialize(ret.value));
        } else if (src instanceof Stmt.Block block) {
            obj.addProperty("type", "Block");
            obj.add("statements", context.serialize(block.statements));
        }
        return obj;
    }
}