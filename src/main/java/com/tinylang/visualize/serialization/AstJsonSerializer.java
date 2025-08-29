package com.tinylang.visualize.serialization;

import com.google.gson.GsonBuilder;
import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AstJsonSerializer {

    public static void serializeToFile(List<Stmt> statements, String filePath) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(Stmt.class, new StmtSerializer());
        builder.registerTypeHierarchyAdapter(Expr.class, new ExprSerializer());
        try (FileWriter file = new FileWriter(filePath)) {
            builder.create().toJson(statements, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
