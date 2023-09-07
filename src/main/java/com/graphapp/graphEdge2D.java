package com.graphapp;

import javafx.scene.shape.Line;

public class graphEdge2D extends Line {
    graphVertex2D vertexFrom;
    graphVertex2D vertexTo;


    public graphEdge2D(graphVertex2D vertexFrom, graphVertex2D vertexTo) {
        super(vertexFrom.getCenterX(), vertexFrom.getCenterY(), vertexTo.getCenterX(), vertexTo.getCenterY());
        this.vertexFrom = vertexFrom;
        this.vertexTo = vertexTo;
    }


    public void setVertexFrom(graphVertex2D vertexFrom) {
        this.vertexFrom = vertexFrom;
    }


    public void setVertexTo(graphVertex2D vertexTo) {
        this.vertexTo = vertexTo;
    }


    public graphVertex2D getVertexFrom() {
        return vertexFrom;
    }


    public graphVertex2D getVertexTo() {
        return vertexTo;
    }
}
