package com.graphapp;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

/** Component of the visual representation of the graph
 *
 */
public class Graph2D {

    private final int verticesNumber; // amount of vertices
    private final int edgesNumber; // amount of edges
    private final boolean[][] vertexRelations; // relations between vertices
    private final Pane parent;
    private double centerX;
    private double centerY;
    private final Node[] componentsArray; // contains all displayable components of 2D graph
    private final graphVertex2D[] vertexArray; // contains all vertices of the graph
    private final graphEdge2D[] edgesArray; // contains all edges of the graph
    private final ArrayList<VertexEvent> vertexEventArray; // contains vertexEvents of all verteces for access later
    private final ArrayList<Color> colorList = new ArrayList<>(); // list of all possible colors, with index representing their numerical value
    private Color currentColor;
    private final double parentWidth;
    private final double parentHeight;
    private final double mainRadius;
    private final double minVertexRadius;


    class VertexEvent implements EventHandler<MouseEvent>{
        graphVertex2D vertex;
        public VertexEvent(graphVertex2D vertex){
            this.vertex = vertex;
        }


        @Override
        public void handle(MouseEvent event) {
            if(vertex.isColorable()) {

                for (int i = 0; i < vertex.getNeighbours().size(); i++) {
                    if (vertex.getNeighbours().get(i).getFill() == currentColor) {
                        System.out.println("No");
                        return;
                    }
                }


                if (vertex.getColorId() != 0) { // if vertex already has a color
                    boolean isPrevColorUsed = false; // flag identifying is this color used in other vertices other than current one

                    for (int i = 0; i < vertexArray.length; i++) {
                        if (vertexArray[i] != vertex && vertexArray[i].getColorId() == vertex.getColorId()) { // if used - flag = true
                            isPrevColorUsed = true;
                            break;
                        }
                    }

                    if (!isPrevColorUsed) { // if this color is not used anywhere
                        colorList.remove((Color) vertex.getFill()); // delete it from list of all colors

                        for (int i = 0; i < vertexArray.length; i++) { // updates all colorId's after deletion
                            int colorId = colorList.indexOf((Color) vertexArray[i].getFill());

                            if (colorId != -1) {
                                vertexArray[i].setColorId(colorId+ 1);
                            }
                        }
                    }

                }

                if (!colorList.contains(currentColor)) { // if new color is not in list
                    colorList.add(currentColor); // add it
                }

                vertex.setFill(currentColor); // set new color to current vertex
                vertex.setStroke(Color.BLACK);
                vertex.setStrokeWidth(2);

                vertex.setColorId(colorList.indexOf(currentColor) + 1); // set numerical id of the new color

                for (int i = 0; i < edgesArray.length; i++) {
                    if (edgesArray[i].getVertexFrom() == vertex || edgesArray[i].getVertexTo() == vertex) {
                        edgesArray[i].setStroke(currentColor);
                    }
                    else {
                        edgesArray[i].setStroke(Color.BLACK);
                    }
                }
            }
            else{
                System.out.println("Cannot change color of vertex no. " + vertex.getId());
            }

        }
    }


    public Graph2D(Graph graph, Pane parent, Color currentColor) {
        this.verticesNumber = graph.getVerticesNumber();
        this.edgesNumber = graph.getEdgesNumber();
        this.vertexRelations = graph.getVertexRelations();
        this.parent = parent;
        this.currentColor = currentColor;

        parentWidth = this.parent.getWidth();
        parentHeight = this.parent.getHeight();

        centerX = parentWidth / 2;
        centerY = parentHeight / 2;

        this.componentsArray = new Node[verticesNumber + edgesNumber];
        this.vertexArray = new graphVertex2D[verticesNumber];
        this.edgesArray = new graphEdge2D[edgesNumber];

        this.vertexEventArray = new ArrayList<>();

        this.mainRadius = Math.min(parentHeight, parentWidth) / 4 + 40; // radius of main circle(dependent on scene scale)
        this.minVertexRadius = Math.PI * mainRadius / 6 - 5; // minimal radius for vertex

        this.createGraph(); // initializes creation of the graph
    }


    /**
     *  Creates the ellipse / circle object
     * @param i Iteration of i
     * @param x x coordinate of the center of current circle
     * @param y y coordinate of the center of current circle
     * @return Ellipse
     */

    private graphVertex2D createVertex(int i, double x, double y) {
        graphVertex2D vertex = new graphVertex2D(null);
        vertex.setFill(Color.WHITE);
        vertex.setStroke(Color.BLACK);
        vertex.setId(Integer.toString(i));  // id is needed to point to specific shapes in the setOnMouseClicked method

        vertex.setCenterX(x);
        vertex.setCenterY(y);

        double radius = Math.min(Math.max((Math.PI * mainRadius / (verticesNumber + 1) - 5), 5), minVertexRadius);
        vertex.setRadiusX(radius);
        vertex.setRadiusY(radius);

        Graph2D.VertexEvent vertexEvent = this.new VertexEvent(vertex);
        vertex.setOnMouseClicked(vertexEvent);
        vertexEventArray.add(vertexEvent);

        return vertex;
    }


    private graphEdge2D createEdge(graphVertex2D vertexFrom, graphVertex2D vertexTo) {
        graphEdge2D edge = new graphEdge2D(vertexFrom, vertexTo);
        edge.setStroke(Color.BLACK);
        edge.setStrokeWidth(2);

        return edge;
    }


    private void createGraph() {
        for(int i = 0; i < verticesNumber; i++ ){
            // Create potential coordinates of the next vertex:
            double x = centerX + mainRadius * Math.cos(i*2*Math.PI / verticesNumber);
            double y = centerY + mainRadius * Math.sin(i*2*Math.PI / verticesNumber);

            graphVertex2D vertex = createVertex(i, x, y);
            vertexArray[i] = vertex;
            vertex.setId(Integer.toString(i)); // sets id of the vertex equal to it's index in the vertexArray

        }

        int edgeCounter = 0; // Temporal counter to track edgesArray index
        for (int i = 0; i < verticesNumber; i++) {
            for (int j = 0; j < verticesNumber; j++) {
                if (vertexRelations[i][j]) {
                    vertexArray[i].addNeighbour(vertexArray[j]); // adds neighbour to graphVertex2D object

                    if (edgeCounter < edgesArray.length) { // if not all edges has been added yet
                        if (Integer.parseInt(vertexArray[i].getId()) < Integer.parseInt(vertexArray[j].getId())) { // and edge between such vertices hasn't been added yet
                            edgesArray[edgeCounter] = createEdge(vertexArray[i], vertexArray[j]); // creates edge and adds it to array
                            edgeCounter++;
                        }
                    }

                }
            }
        }
    }


    /** Draws the vertices in a circle
     *
     * @return array of graphVertex objects.
     */
    public Node[] draw(){
        for (int i = 0; i < componentsArray.length; i++) { // adds all vertices and edges to component array to return it and display afterwards
            if (i < edgesArray.length) {
                componentsArray[i] = edgesArray[i];
            }
            else {
                componentsArray[i] = vertexArray[i - edgesArray.length];
            }
        }

        return componentsArray;
    }


    public int[] getIdOfColors() {
        int[] idOfColors = new int[verticesNumber];

        for (int i = 0; i < vertexArray.length; i++) {
            idOfColors[i] = vertexArray[i].getColorId(); // gets numerical value of the color assigned to each vertex (no color = 0)
        }

        return idOfColors;
    }


    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }


    public void addColor(Color color) {
        colorList.add(color);
    }


    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }


    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }


    public Color getCurrentColor() {
        return currentColor;
    }


    public ArrayList<Color> getColorList() {
        return colorList;
    }



    public graphVertex2D[] getVertexArray(){
        return this.vertexArray;
    }


    public ArrayList<VertexEvent> getVertexEventArray() {
        return this.vertexEventArray;
    }


    public double getCenterX() {
        return centerX;
    }


    public double getCenterY() {
        return centerY;
    }
}