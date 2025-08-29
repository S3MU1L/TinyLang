package com.tinylang.visualize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;

/* TODO: this is experimental */
public class AstTreeViewVisualizer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        JsonElement astJson = loadJson("ast_output.json");
        TreeItem<String> root = buildTree("AST", astJson);
        TreeView<String> treeView = new TreeView<>(root);
        treeView.setShowRoot(true);

        VBox layout = new VBox(treeView);
        Scene scene = new Scene(layout, 800, 600);

        primaryStage.setTitle("TinyLang AST Tree Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private TreeItem<String> buildTree(String key, JsonElement element) {
        TreeItem<String> item = new TreeItem<>(key);
        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            for (String k : obj.keySet()) {
                item.getChildren().add(buildTree(k, obj.get(k)));
            }
        } else if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            int idx = 0;
            for (JsonElement el : arr) {
                item.getChildren().add(buildTree("[" + idx++ + "]", el));
            }
        } else {
            item.getChildren().add(new TreeItem<>(element.toString()));
        }
        return item;
    }

    private JsonElement loadJson(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private TreeItem<String> createStmtTreeItem(Stmt stmt, int index) {
        return stmt.accept(new StmtTreeVisitor(index));
    }

    private TreeItem<String> createExprTreeItem(Expr expr) {
        return expr.accept(new ExprTreeVisitor());
    }

    private class StmtTreeVisitor implements Stmt.Visitor<TreeItem<String>> {
        private final int index;

        public StmtTreeVisitor(int index) {
            this.index = index;
        }

        @Override
        public TreeItem<String> visitLetStmt(Stmt.Let stmt) {
            TreeItem<String> item = new TreeItem<>(String.format("[%d] Let: %s", index, stmt.name));
            item.setExpanded(true);

            if (stmt.initializer != null) {
                TreeItem<String> initItem = createExprTreeItem(stmt.initializer);
                item.getChildren().add(initItem);
            } else {
                item.getChildren().add(new TreeItem<>("initializer: null"));
            }

            return item;
        }

        @Override
        public TreeItem<String> visitExpressionStmt(Stmt.Expression stmt) {
            TreeItem<String> item = new TreeItem<>(String.format("[%d] Expression Statement", index));
            item.setExpanded(true);

            TreeItem<String> exprItem = createExprTreeItem(stmt.expression);
            item.getChildren().add(exprItem);

            return item;
        }

        @Override
        public TreeItem<String> visitIfStmt(Stmt.If stmt) {
            TreeItem<String> item = new TreeItem<>(String.format("[%d] If Statement", index));
            item.setExpanded(true);

            TreeItem<String> conditionItem = new TreeItem<>("condition:");
            conditionItem.getChildren().add(createExprTreeItem(stmt.condition));
            item.getChildren().add(conditionItem);

            TreeItem<String> thenItem = new TreeItem<>("then:");
            thenItem.getChildren().add(createStmtTreeItem(stmt.thenBranch, 0));
            item.getChildren().add(thenItem);

            if (stmt.elseBranch != null) {
                TreeItem<String> elseItem = new TreeItem<>("else:");
                elseItem.getChildren().add(createStmtTreeItem(stmt.elseBranch, 0));
                item.getChildren().add(elseItem);
            }

            return item;
        }

        @Override
        public TreeItem<String> visitWhileStmt(Stmt.While stmt) {
            TreeItem<String> item = new TreeItem<>(String.format("[%d] While Statement", index));
            item.setExpanded(true);

            TreeItem<String> conditionItem = new TreeItem<>("condition:");
            conditionItem.getChildren().add(createExprTreeItem(stmt.condition));
            item.getChildren().add(conditionItem);

            TreeItem<String> bodyItem = new TreeItem<>("body:");
            bodyItem.getChildren().add(createStmtTreeItem(stmt.body, 0));
            item.getChildren().add(bodyItem);

            return item;
        }

        @Override
        public TreeItem<String> visitReturnStmt(Stmt.Return stmt) {
            TreeItem<String> item = new TreeItem<>(String.format("[%d] Return Statement", index));
            item.setExpanded(true);

            if (stmt.value != null) {
                TreeItem<String> valueItem = new TreeItem<>("value:");
                valueItem.getChildren().add(createExprTreeItem(stmt.value));
                item.getChildren().add(valueItem);
            } else {
                item.getChildren().add(new TreeItem<>("value: null"));
            }

            return item;
        }

        @Override
        public TreeItem<String> visitBlockStmt(Stmt.Block stmt) {
            TreeItem<String> item = new TreeItem<>(String.format("[%d] Block Statement (%d statements)", index, stmt.statements.size()));
            item.setExpanded(true);

            for (int i = 0; i < stmt.statements.size(); i++) {
                TreeItem<String> stmtItem = createStmtTreeItem(stmt.statements.get(i), i);
                item.getChildren().add(stmtItem);
            }

            return item;
        }
    }

    private class ExprTreeVisitor implements Expr.Visitor<TreeItem<String>> {
        @Override
        public TreeItem<String> visitBinaryExpr(Expr.BinaryExpr expr) {
            TreeItem<String> item = new TreeItem<>("Binary: " + expr.operator.toString());
            item.setExpanded(true);

            TreeItem<String> leftItem = new TreeItem<>("left:");
            leftItem.getChildren().add(createExprTreeItem(expr.left));
            item.getChildren().add(leftItem);

            TreeItem<String> rightItem = new TreeItem<>("right:");
            rightItem.getChildren().add(createExprTreeItem(expr.right));
            item.getChildren().add(rightItem);

            return item;
        }

        @Override
        public TreeItem<String> visitUnaryExpr(Expr.UnaryExpr expr) {
            TreeItem<String> item = new TreeItem<>("Unary: " + expr.operator.toString());
            item.setExpanded(true);

            TreeItem<String> rightItem = new TreeItem<>("operand:");
            rightItem.getChildren().add(createExprTreeItem(expr.right));
            item.getChildren().add(rightItem);

            return item;
        }

        @Override
        public TreeItem<String> visitVarExpr(Expr.VarExpr expr) {
            return new TreeItem<>("Variable: " + expr.name);
        }

        @Override
        public TreeItem<String> visitAssignExpr(Expr.AssignExpr expr) {
            TreeItem<String> item = new TreeItem<>("Assignment: " + expr.name);
            item.setExpanded(true);

            TreeItem<String> valueItem = new TreeItem<>("value:");
            valueItem.getChildren().add(createExprTreeItem(expr.value));
            item.getChildren().add(valueItem);

            return item;
        }

        @Override
        public TreeItem<String> visitCallExpr(Expr.CallExpr expr) {
            TreeItem<String> item = new TreeItem<>("Call Expression");
            item.setExpanded(true);

            TreeItem<String> calleeItem = new TreeItem<>("callee:");
            calleeItem.getChildren().add(createExprTreeItem(expr.callee));
            item.getChildren().add(calleeItem);

            if (!expr.arguments.isEmpty()) {
                TreeItem<String> argsItem = new TreeItem<>("arguments:");
                for (int i = 0; i < expr.arguments.size(); i++) {
                    TreeItem<String> argItem = new TreeItem<>("arg[" + i + "]:");
                    argItem.getChildren().add(createExprTreeItem(expr.arguments.get(i)));
                    argsItem.getChildren().add(argItem);
                }
                item.getChildren().add(argsItem);
            }

            return item;
        }

        @Override
        public TreeItem<String> visitGroupingExpr(Expr.GroupingExpr expr) {
            TreeItem<String> item = new TreeItem<>("Grouping");
            item.setExpanded(true);

            TreeItem<String> exprItem = new TreeItem<>("expression:");
            exprItem.getChildren().add(createExprTreeItem(expr.expression));
            item.getChildren().add(exprItem);

            return item;
        }

        @Override
        public TreeItem<String> visitLiteralExpr(Expr.LiteralExpr expr) {
            String value = expr.value == null ? "null" : expr.value.toString();
            return new TreeItem<>("Literal: " + value);
        }

        @Override
        public TreeItem<String> visitFunctionExpr(Expr.Function expr) {
            TreeItem<String> item = new TreeItem<>("Function");
            item.setExpanded(true);

            TreeItem<String> paramsItem = new TreeItem<>("parameters:");
            for (String param : expr.params) {
                paramsItem.getChildren().add(new TreeItem<>(param));
            }
            item.getChildren().add(paramsItem);

            TreeItem<String> bodyItem = new TreeItem<>("body:");
            bodyItem.getChildren().add(createStmtTreeItem(expr.body, 0));
            item.getChildren().add(bodyItem);

            return item;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}