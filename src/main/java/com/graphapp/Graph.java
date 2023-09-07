package com.graphapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Graph {
    private int verticesNumber;
    private int edgesNumber;
    private int lower_bound;
    private int upper_bound;
    private int chromaticNumber;
    private boolean[][] vertexRelations;


    public Graph(int verticesNumber, int edgesNumber, boolean[][] vertexRelations) {
        this.verticesNumber = verticesNumber;
        this.edgesNumber = edgesNumber;
        this.vertexRelations = vertexRelations;

    }


    public void graphToFile(String path) throws IOException {
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
        writer.println("VERTICES = " + verticesNumber);
        writer.println("EDGES = " + edgesNumber);
        writer.println(edgesToText());
        writer.close();
    }


    private String edgesToText() {
        String edgeTable = "";

        for (int i = 0; i < verticesNumber; i++) {
            for (int j = i; j < verticesNumber; j++) {
                // System.out.print(connections[i][j] + " ");
                if (vertexRelations[i][j]) {
                    edgeTable += (i + 1) + " " + (j + 1) + "\n";
                }
            }
            // System.out.print("|");
        }
        System.out.println();
        return edgeTable;
    }


    public void setVerticesNumber(int verticesNumber) {
        this.verticesNumber = verticesNumber;
    }


    public void setEdgesNumber(int edgesNumber) {
        this.edgesNumber = edgesNumber;
    }


    public void setChromaticNumber(int chromaticNumber) {
        this.chromaticNumber = chromaticNumber;
    }


    public void setLower_bound(int lower_bound) {
        this.lower_bound = lower_bound;
    }


    public void setUpper_bound(int upper_bound) {
        this.upper_bound = upper_bound;
    }


    public void setVertexRelations(boolean[][] vertexRelations) {
        this.vertexRelations = vertexRelations;
    }


    public int getVerticesNumber() {
        return verticesNumber;
    }


    public int getEdgesNumber() {
        return edgesNumber;
    }


    public int getLower_bound() {
        return lower_bound;
    }


    public int getUpper_bound() {
        return upper_bound;
    }


    public int getChromaticNumber() {
        return chromaticNumber;
    }


    public boolean[][] getVertexRelations() {
        return vertexRelations;
    }
}
