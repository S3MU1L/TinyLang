package com.tinylang.visualize.deserialization;

import com.google.gson.*;
import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StmtDeserializer implements JsonDeserializer<Stmt> {

    @Override
    public Stmt deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        if (!obj.has("type")) {
            throw new JsonParseException("Missing 'type' field in Stmt JSON object");
        }

        String stmtType = obj.get("type").getAsString();
        switch (stmtType) {
            case "Let":
                String name = obj.get("name").getAsString();
                Expr initializer = obj.has("initializer") && !obj.get("initializer").isJsonNull()
                        ? context.deserialize(obj.get("initializer"), Expr.class)
                        : null;
                return new Stmt.Let(name, initializer);
            case "Expression":
                Expr expr = context.deserialize(obj.get("expression"), Expr.class);
                return new Stmt.Expression(expr);
            case "If":
                Expr condition = context.deserialize(obj.get("condition"), Expr.class);
                Stmt thenBranch = context.deserialize(obj.get("thenBranch"), Stmt.class);
                Stmt elseBranch = obj.has("elseBranch") && !obj.get("elseBranch").isJsonNull()
                        ? context.deserialize(obj.get("elseBranch"), Stmt.class)
                        : null;
                return new Stmt.If(condition, thenBranch, elseBranch);
            case "While":
                Expr whileCond = context.deserialize(obj.get("condition"), Expr.class);
                Stmt body = context.deserialize(obj.get("body"), Stmt.class);
                return new Stmt.While(whileCond, body);
            case "Return":
                Expr value = obj.has("value") && !obj.get("value").isJsonNull()
                        ? context.deserialize(obj.get("value"), Expr.class)
                        : null;
                return new Stmt.Return(value);
            case "Block":
                List<Stmt> statements = new ArrayList<>();
                JsonArray stmtsArray = obj.getAsJsonArray("statements");
                for (JsonElement stmtElem : stmtsArray) {
                    statements.add(context.deserialize(stmtElem, Stmt.class));
                }
                return new Stmt.Block(statements);
            default:
                throw new JsonParseException("Unknown Stmt type: " + stmtType);
        }
    }
}
