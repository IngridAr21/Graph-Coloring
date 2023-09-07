package com.graphapp;

import java.util.*;

import static java.util.Arrays.sort;

public class GraphCalculator {
    private final int verticesNumber;
    private final boolean[][] vertexRelations;
    private final int[] vertexDegrees;
    private int[] vertexColors;
    private int upper_bound = 0;
    private int lower_bound = 0;
    private int chromaticNumberValue;



    public GraphCalculator(Graph graph){
        this.verticesNumber = graph.getVerticesNumber();
        //vertexRelations = new boolean[this.verticesNumber][this.verticesNumber]; // index represents No. of vertex, value represents whether they have a common edge
        vertexRelations = graph.getVertexRelations(); // index represents No. of vertex, value represents whether they have a common edge
        vertexColors = new int[this.verticesNumber]; // array with colors of each vertex, needed to write combinations of colors, different values represents different colors

        Arrays.fill(vertexColors, 0); // initially uncolored (all color values are 0)

        // for (int i = 0; i < edges.length; i++) {
        //     vertexRelations[edges[i].u - 1][edges[i].v - 1] = true;
        //     vertexRelations[edges[i].v - 1][edges[i].u - 1] = true;
        //     // assigning relations to vertexes
        // }

        vertexDegrees = graphDegree();
        chromaticNumberValue = graphTypeCheck(vertexDegrees, this.verticesNumber);
    }

    /**
        Method to calculate degrees of all vertices
        @return - returns array with all degrees of the graph
        // @param edges - array of all edges(ColEdge has two parameters, one for each vertex which has an edge between them)
        // @param edgesNumber - amount of edges in the graph
        // @param verticesNumber - amount of vertices in the graph
     */
    private int[] graphDegree(){
        // returns a sorted array of degrees of every vertex
        // Example cycle graph with 3 vertices: [2, 2, 2]

        int[] vertexDegrees = new int[verticesNumber];
        // Creates an array filled with 0 and length equal to number of vertices in the given graph

        for (int i = 0; i < verticesNumber; i++) {
            for (int j = 0; j < verticesNumber; j++) {
                if (vertexRelations[i][j]) {
                    vertexDegrees[i] += 1;
                }
            }
        }

//        for (int i = 0; i < edgesNumber; i++) {
//            vertexDegrees[edges[i].u - 1] += 1;
//            vertexDegrees[edges[i].v - 1] += 1;
//            // Each index represents one vertex and value written in the index represents degree of this vertex
//        }

        return vertexDegrees; // upper bound is equal to degree of the graph according to Brook's theorem
    }

    /**
        Method to calculate chromatic number of the special graph(even cycle, odd cycle, or complete graph) , or to evaluate whether the graph is not special
        @return - returns -1 if graph is not special
                - returns chromatic number if special graph detected
        @param degrees - degrees of all vertices
        @param verticesNumber - amount of vertices
     */
    private int graphTypeCheck(int[] degrees, int verticesNumber) {
        sort(degrees);

        if (degrees[0] == 2 && degrees[degrees.length - 1] == 2) {    // Checks for cycle in graph
            if (verticesNumber % 2 != 0) {                  // Checks whether it even or odd
                return 3;
            } else {
                return 2;
            }
        }

        if (degrees[0] == verticesNumber - 1 && degrees[degrees.length - 1] == verticesNumber - 1) { // Checks for complete graph
            return verticesNumber;
        }

        return -1;
    }


    /**
        Calculates approximate chromaticNumber.
        @param amountOfColors - amount of colors to check
        @param relations - two-dimensional array contains relations between vertices
        @param colors - current distribution of colors
        @param currentVertex - current vertex to check
     */
    private boolean chromaticNumber (int amountOfColors, boolean[][] relations, int[] colors, boolean[] mask, int currentVertex) {
        if (currentVertex == relations.length) {    // if all vertexes have been successfully checked - return true
            vertexColors = Arrays.copyOf(colors, colors.length);

            sort(colors); // sorting array of color distribution to get the biggest color used
            chromaticNumberValue = colors[colors.length - 1]; // last element is the biggest, because it is sorted, so it's chromatic number for this distribution

            return true;
        }

        if (mask[currentVertex]) { // if vertex is under the mask
            if (!availabilityCheck(relations, colors, currentVertex, colors[currentVertex])) { // check if in the current state graph is possible
                return false;
            }
            
            return chromaticNumber(amountOfColors, relations, colors, mask, currentVertex + 1); // skip this vertex and check this one
        }
        else {
            for (int i = 1; i <= amountOfColors; i++) {
                // creates branches for each possible color distribution
                if (availabilityCheck(relations, colors, currentVertex, i)) {
                    colors[currentVertex] = i;
                    // color of the <currentVertex> is set to i

                    if (chromaticNumber(amountOfColors, relations, colors, mask, currentVertex + 1)) {
                        // checks next vertex, if all vertexes in a branch received proper coloring recursion returns true
                        return true;
                    }

                    colors[currentVertex] = 0;
                    // if at some point combination of colors turned out to be impossible, recursion
                    // proceeds back to the point combination of colors was still true
                }

            }

            return false;
        }
    }


    /**
        Function to find all the sizes of max cliques
        @param currentClique - stores all vertexes which you need to check whether they are connected to the current vertex(for clique number algorithm)
        @param currentVertex - current vertex to check
        @param cliqueIndex - index of an element which is currently being added to currentClique
        @return size of the biggest clique of the graph
     */
    private int maxClique(int[] currentClique, int currentVertex, int cliqueIndex) {
        int max = 0;

        for (int i = currentVertex; i <= verticesNumber; i++) {
            if (!contains(currentClique, i)) { // if point is already added - don't check it
                if (isClique(currentClique, cliqueIndex, i - 1)) { // check whether this vertex can be added to clique
                    currentClique[cliqueIndex] = i; // if yes - add vertex to clique
                    max = Math.max(max, 1 + maxClique(currentClique, i, cliqueIndex + 1));
                    // launches recursion and contains the maximum relative clique number,
                    // so when code comes back to the first iteration after completing recursion it will contain the biggest clique number
                }
            }
        }
        return max; // return current max clique number
    }

    // below there are supportive methods used for calculations


    /**
     checks whether taken color can be assigned to taken vertex
     @return true - if color can be assigned
             false - if cannot
     @param vertexRelations - relations of all vertices
     @param colors - colors already assigned to the graph
     @param currentVertex - vertex in which color is being assigned
     @param colorToCheck - color which is going to be checked
     */
    public static boolean availabilityCheck(boolean[][] vertexRelations, int[] colors, int currentVertex, int colorToCheck){
        for (int i = 0; i < vertexRelations[0].length; i++) {
            // System.out.println(colors.length + " " + vertexRelations.length);
            if ((colors[i] == colorToCheck) && vertexRelations[currentVertex][i]) {
                // if color of the taken vertex is equal to color of another vertex, and they have an edge between them - color can not be assigned
                return false;
            }
        }
        return true; // otherwise color can be assigned
    }


    /**
     Checks whether given clique is valid or not
     @return true - if the given clique is valid
             false - if not
     @param clique - current clique to check
     */
    private boolean isClique(int[] clique, int index, int vertexToCheck) {
        for (int i = 0; i < index; i++) {
            if (!vertexRelations[vertexToCheck][clique[i] - 1]) {
                // if there is no edge between current vertex and at least one vertex which is already in the clique - return false
                return false;
            }
        }
        // otherwise - return true
        return true;
    }

    /**
        Supportive method that checks whether element is in points or not
        @return true - if points contain element
                false - if not
        @param points - all vertices already added to clique
        @param element - element to check
    */
    private static boolean contains(int[] points, int element) {
        for (int i = 0; i < points.length; i++) {
            if (element == points[i]) {
                // if element is already in clique - return true
                return true;
            }
        }
        // otherwise - false
        return false;
    }

    // Below there are get methods used to extract values


    /**
     Get the upper bound of the given graph
     @return upper bound value
     */
    public int getUpper_bound(){
        if (upper_bound == 0) {
            sort(vertexDegrees); // Sorting array from the smallest to largest to find the maximum degree later(the last element is the biggest one)
            upper_bound = vertexDegrees[vertexDegrees.length - 1] + 1;
        }

        return upper_bound;
    }


    /**
     Get the lower bound of the given graph
     @return lower bound value
     */
    public int getLower_bound(){
        if (lower_bound == 0) {
            lower_bound = maxClique(new int[this.verticesNumber], 1, 0);
        }

        return lower_bound;
    }



    /**
        Get the approximate chromatic number of the given graph
        @return value of chromatic number or chromatic number of special graph type
    */
    public int getChromaticNumber(){
        if (chromaticNumberValue == -1) {
            chromaticNumber(this.getUpper_bound(), vertexRelations, vertexColors, new boolean[verticesNumber], 0);
            return chromaticNumberValue;
        }

        return chromaticNumberValue;
    }


    /**
     Get the exact chromatic number of the given graph
     @return exact value of chromatic number or chromatic number of special graph type
     */
    public int getExactChromaticNumber(){
        if (chromaticNumberValue == -1) {
            for (int i = this.getLower_bound(); i <= this.getUpper_bound(); i++) {
                if (chromaticNumber(i, vertexRelations, vertexColors, new boolean[verticesNumber], 0)) {
                    break;
                }
            }
            return chromaticNumberValue;
        }
        return chromaticNumberValue;
    }


    public int getExactChromaticNumber(int[] colors, boolean[] mask){
            for (int i = getLower_bound(); i <= getUpper_bound(); i++) {
                if (chromaticNumber(i, vertexRelations, colors, mask, 0)) {
                    break;
                }
            }
            return chromaticNumberValue;
    }


    public int[] getVertexColors() {
        return vertexColors;
    }
}