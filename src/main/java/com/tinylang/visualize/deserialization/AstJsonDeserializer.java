package com.tinylang.visualize.deserialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;

import java.io.FileReader;
import java.util.List;

public class AstJsonDeserializer {

    public static List<Stmt> deserializeFromFileReader(FileReader reader) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(Stmt.class, new StmtDeserializer());
        builder.registerTypeHierarchyAdapter(Expr.class, new ExprDeserializer());
        Gson gson = builder.create();
        return gson.fromJson(reader, new TypeToken<List<Stmt>>(){}.getType());
    }
}
