package com.tinylang.visualize.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class NodeView extends Group {

    public NodeView(String label, double x, double y) {
        Circle circle = new Circle(0, 0, 20, Color.LIGHTGRAY);
        circle.setStroke(Color.BLACK);

        Text text = new Text(label);
        text.setX(-text.getLayoutBounds().getWidth() / 2);
        text.setY(text.getLayoutBounds().getHeight() / 4);

        getChildren().addAll(circle, text);
        setLayoutX(x);
        setLayoutY(y);
    }
}