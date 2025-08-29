package com.tinylang.visualize;

import com.tinylang.ast.Expr;
import com.tinylang.ast.Stmt;
import com.tinylang.visualize.deserialization.AstJsonDeserializer;
import com.tinylang.visualize.view.NodeView;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.FileReader;
import java.util.List;

/* TODO: this is experimental */
public class AstNodeViewVisualizer extends Application {

    private static final double H_SPACING = 150;
    private static final double V_SPACING = 100;
    private final Group root = new Group();
    private final StmtVisitor stmtVisitor = new StmtVisitor();
    private final ExprVisitor exprVisitor = new ExprVisitor();

    @Override
    public void start(Stage primaryStage) throws Exception {
        double currentY = 50;
        try (FileReader reader = new FileReader("ast_output.json")) {
            List<Stmt> statements = AstJsonDeserializer.deserializeFromFileReader(reader);
            for (Stmt stmt : statements) {
                stmtVisitor.visit(stmt, 50, currentY);
                currentY += V_SPACING * 2; // Spacing between top-level statements
            }
        }

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 800, 600);
        primaryStage.setTitle("TinyLang AST Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class StmtVisitor {
        public NodeView visit(Stmt stmt, double x, double y) {
            if (stmt instanceof Stmt.Let let) return visitLetStmt(let, x, y);
            if (stmt instanceof Stmt.Expression expression) return visitExpressionStmt(expression, x, y);
            if (stmt instanceof Stmt.If ifStmt) return visitIfStmt(ifStmt, x, y);
            if (stmt instanceof Stmt.While whileStmt) return visitWhileStmt(whileStmt, x, y);
            if (stmt instanceof Stmt.Return returnStmt) return visitReturnStmt(returnStmt, x, y);
            if (stmt instanceof Stmt.Block block) return visitBlockStmt(block, x, y);
            return null;
        }

        public NodeView visitLetStmt(Stmt.Let stmt, double x, double y) {
            NodeView node = createNode("let " + stmt.name, x, y);
            if (stmt.initializer != null) {
                NodeView initNode = exprVisitor.visit(stmt.initializer, x + H_SPACING, y);
                createEdge(node, initNode, Color.BLUE);
            }
            return node;
        }

        public NodeView visitExpressionStmt(Stmt.Expression stmt, double x, double y) {
            return exprVisitor.visit(stmt.expression, x, y);
        }

        public NodeView visitIfStmt(Stmt.If stmt, double x, double y) {
            NodeView node = createNode("if", x, y);
            NodeView conditionNode = exprVisitor.visit(stmt.condition, x + H_SPACING, y - V_SPACING / 2);
            createEdge(node, conditionNode, Color.RED);

            if (stmt.thenBranch != null) {
                NodeView thenNode = visit(stmt.thenBranch, x + H_SPACING, y + V_SPACING / 2);
                createEdge(node, thenNode, Color.GREEN);
            }
            if (stmt.elseBranch != null) {
                NodeView elseNode = visit(stmt.elseBranch, x + H_SPACING, y + V_SPACING * 1.5);
                createEdge(node, elseNode, Color.ORANGE);
            }
            return node;
        }

        public NodeView visitWhileStmt(Stmt.While stmt, double x, double y) {
            NodeView node = createNode("while", x, y);
            NodeView conditionNode = exprVisitor.visit(stmt.condition, x + H_SPACING, y - V_SPACING / 2);
            createEdge(node, conditionNode, Color.RED);
            if (stmt.body != null) {
                NodeView bodyNode = visit(stmt.body, x + H_SPACING, y + V_SPACING / 2);
                createEdge(node, bodyNode, Color.GREEN);
            }
            return node;
        }

        public NodeView visitReturnStmt(Stmt.Return stmt, double x, double y) {
            NodeView node = createNode("return", x, y);
            if (stmt.value != null) {
                NodeView valueNode = exprVisitor.visit(stmt.value, x + H_SPACING, y);
                createEdge(node, valueNode, Color.BLUE);
            }
            return node;
        }

        public NodeView visitBlockStmt(Stmt.Block stmt, double x, double y) {
            NodeView node = createNode("Block", x, y);
            double currentY = y;
            for (Stmt s : stmt.statements) {
                NodeView childNode = visit(s, x + H_SPACING, currentY);
                createEdge(node, childNode, Color.GRAY);
                currentY += V_SPACING;
            }
            return node;
        }
    }

    private class ExprVisitor {
        public NodeView visit(Expr expr, double x, double y) {
            if (expr instanceof Expr.BinaryExpr binaryExpr) return visitBinaryExpr(binaryExpr, x, y);
            if (expr instanceof Expr.UnaryExpr unaryExpr) return visitUnaryExpr(unaryExpr, x, y);
            if (expr instanceof Expr.VarExpr varExpr) return visitVarExpr(varExpr, x, y);
            if (expr instanceof Expr.AssignExpr assignExpr) return visitAssignExpr(assignExpr, x, y);
            if (expr instanceof Expr.CallExpr callExpr) return visitCallExpr(callExpr, x, y);
            if (expr instanceof Expr.GroupingExpr groupingExpr) return visitGroupingExpr(groupingExpr, x, y);
            if (expr instanceof Expr.LiteralExpr literalExpr) return visitLiteralExpr(literalExpr, x, y);
            if (expr instanceof Expr.Function function) return visitFunctionExpr(function, x, y);
            return null;
        }

        public NodeView visitBinaryExpr(Expr.BinaryExpr expr, double x, double y) {
            NodeView node = createNode(expr.operator.name(), x, y);
            NodeView leftNode = visit(expr.left, x + H_SPACING, y - V_SPACING / 2);
            NodeView rightNode = visit(expr.right, x + H_SPACING, y + V_SPACING / 2);
            createEdge(node, leftNode, Color.BLUE);
            createEdge(node, rightNode, Color.BLUE);
            return node;
        }

        public NodeView visitUnaryExpr(Expr.UnaryExpr expr, double x, double y) {
            NodeView node = createNode(expr.operator.name(), x, y);
            NodeView rightNode = visit(expr.right, x + H_SPACING, y);
            createEdge(node, rightNode, Color.BLUE);
            return node;
        }

        public NodeView visitVarExpr(Expr.VarExpr expr, double x, double y) {
            return createNode(expr.name, x, y);
        }

        public NodeView visitAssignExpr(Expr.AssignExpr expr, double x, double y) {
            NodeView node = createNode(expr.name + " =", x, y);
            NodeView valueNode = visit(expr.value, x + H_SPACING, y);
            createEdge(node, valueNode, Color.BLUE);
            return node;
        }

        public NodeView visitCallExpr(Expr.CallExpr expr, double x, double y) {
            NodeView node = createNode("call", x, y);
            NodeView calleeNode = visit(expr.callee, x + H_SPACING, y - V_SPACING);
            createEdge(node, calleeNode, Color.BLUE);
            double currentY = y;
            for (Expr arg : expr.arguments) {
                NodeView argNode = visit(arg, x + H_SPACING, currentY);
                createEdge(node, argNode, Color.BLUE);
                currentY += V_SPACING;
            }
            return node;
        }

        public NodeView visitGroupingExpr(Expr.GroupingExpr expr, double x, double y) {
            return visit(expr.expression, x, y);
        }

        public NodeView visitLiteralExpr(Expr.LiteralExpr expr, double x, double y) {
            return createNode(expr.value.toString(), x, y);
        }

        public NodeView visitFunctionExpr(Expr.Function expr, double x, double y) {
            NodeView node = createNode("fn", x, y);
            double currentY = y;
            for (String param : expr.params) {
                NodeView paramNode = createNode(param, x + H_SPACING, currentY);
                createEdge(node, paramNode, Color.DARKCYAN);
                currentY += V_SPACING / 2;
            }
            NodeView bodyNode = stmtVisitor.visit(expr.body, x + H_SPACING, currentY);
            createEdge(node, bodyNode, Color.GREEN);
            return node;
        }
    }

    private NodeView createNode(String label, double x, double y) {
        NodeView node = new NodeView(label, x, y);
        root.getChildren().add(node);
        return node;
    }

    private void createEdge(NodeView from, NodeView to, Color color) {
        if (from == null || to == null) return;
        Line edge = new Line();
        edge.startXProperty().bind(from.layoutXProperty().add(from.layoutXProperty().divide(2)));
        edge.startYProperty().bind(from.layoutYProperty().add(from.layoutYProperty().divide(2)));
        edge.endXProperty().bind(to.layoutXProperty().add(to.layoutXProperty().divide(2)));
        edge.endYProperty().bind(to.layoutYProperty().add(to.layoutYProperty().divide(2)));
        edge.setStroke(color);
        edge.setStrokeWidth(2);
        root.getChildren().add(edge);
        edge.toBack(); // Pushes the edge behind the nodes
    }

    public static void main(String[] args) {
        launch(args);
    }
}