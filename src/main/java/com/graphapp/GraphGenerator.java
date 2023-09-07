package com.graphapp;

import java.util.Arrays;
import java.util.Random;

public class GraphGenerator {
    private int verticesNumber;
    private int edgesNumber;
    private final int upperBoundVertices;
    private boolean[][] connections;
    private final static String COMMENT = "//";
    private final Random rand = new Random(System.currentTimeMillis());


    public GraphGenerator(int upperBoundVertices) {
        this.upperBoundVertices = upperBoundVertices;

        this.connections = this.generate();
        System.out.println("finished");
    }


    private boolean[][] generate() {
        this.verticesNumber = rand.nextInt(3, upperBoundVertices); // random amount of vertices
        connections = new boolean[verticesNumber][verticesNumber];

        int amountOfEdges = 2; // adjustable variable, defines how much initial edges for each vertex will be generated
        int connectedVertex; // to which vertex current one will be connected
        int counter = 0; // counter of edges

        StringBuilder edgesText = new StringBuilder();

        for (int i = 0; i < verticesNumber; i++) {

            for (int j = 0; j < amountOfEdges; j++) {

                do {
                    connectedVertex = rand.nextInt(verticesNumber);
                }
                while (connectedVertex == i); // chooses vertex to connect current vertex to

                if (!connections[i][connectedVertex]) { // if this edge hasn't been counted yet, count it
                    connections[i][connectedVertex] = true;
                    connections[connectedVertex][i] = true;
                    counter++;

                    edgesText.append(COMMENT + "Edge: " + (i + 1) + " " + (j + 1) + "\n");
                }
            }
        }

        this.edgesNumber = counter;

        System.out.println(COMMENT + "Number of edges: " + counter);
        System.out.println(COMMENT + "Number of vertices: " + verticesNumber + "\n");
        System.out.println(edgesText);

        return connections;
    }

    public boolean[][] getConnections() {
        return connections;
    }


    public int getVerticesNumber() {
        return verticesNumber;
    }


    public int getEdgesNumber() {
        return edgesNumber;
    }
}