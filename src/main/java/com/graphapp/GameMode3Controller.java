package com.graphapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.util.Arrays.sort;

/** Controls the PROTOTYPE SCENE on which the graph is shown
 */
public class GameMode3Controller {
    @FXML
    public FlowPane root;
    @FXML
    public Pane graphArea;
    @FXML
    public Button drawBtn;
    @FXML
    public Button backBtn;
    @FXML
    public Button submitButton;
    @FXML
    public Button hintButton;
    @FXML
    public Label graphProperty;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public Label submitLabel;
    @FXML
    public Label messageLabel;
    @FXML
    public Label scoreLabel;
    @FXML
    public Button save;
    private boolean playerGraph;
    private boolean isShown;
    private Graph graph;
    private Graph2D graphComponent;
    private GraphCalculator calculator;
    private int upperBoundVertices;
    private int[] randomOrderArray;
    private int counterForSubmit = 0;
    private int hintCounter;
    private double score;
    private double maxScore;




    @FXML
    public void toMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void drawGraph() {
        graphArea.getChildren().clear();
        colorPicker.getCustomColors().clear();

        graphArea.getChildren().clear(); // clear previous graph
        colorPicker.getCustomColors().clear();

        hintCounter = 0;

        scoreLabel.setText("Score: ");
        messageLabel.setText("");

        if (!playerGraph) {
            GraphGenerator generator = new GraphGenerator(upperBoundVertices); // create new generator
            graph = new Graph(generator.getVerticesNumber(), generator.getEdgesNumber(), generator.getConnections()); // create new graph object
            messageLabel.setText("Random graph was generated");
        }
        else {
            messageLabel.setText("Custom graph was loaded");
        }

        calculator = new GraphCalculator(graph); // create new calculator
        graphComponent = new Graph2D(graph, graphArea, colorPicker.getValue());


        ArrayRandomizer arrayRandomizer = new ArrayRandomizer();
        randomOrderArray = arrayRandomizer.getRandomArray(graph.getVerticesNumber());

        setVertexColorable(false);
        hintCounter = 0;
        counterForSubmit = 0;

        graphArea.getChildren().addAll(graphComponent.draw());

        // Starting the game as well:

        submitLabel.setText("Try vertex no. " + randomOrderArray[0] + " and click submit!");
        // Changes the vertex event to be NOT-colorable of the vertex at index counterForSubmit in the randomOrderArray:
        graphComponent.getVertexArray()[randomOrderArray[0]].setColorable(true);
        // Changes the stroke to CYAN of the vertex at index counterForSubmit in the randomOrderArray
        graphComponent.getVertexArray()[randomOrderArray[0]].setStroke(Color.CYAN);
        graphComponent.getVertexArray()[randomOrderArray[0]].setStrokeWidth(2);
    }


    /** Submit coloring for current vertex in game mode 3
     *
     * */
    @FXML
    public void submit(){

        if(graphComponent == null){
            messageLabel.setText("Generate graph first!");
        }
        else{

            graphVertex2D currentVertex = graphComponent.getVertexArray()[randomOrderArray[counterForSubmit]];

            if(counterForSubmit != (graphComponent.getVertexArray().length - 1)) {

                graphVertex2D nextVertex = graphComponent.getVertexArray()[randomOrderArray[counterForSubmit + 1]];

                if (currentVertex.getFill().equals((Paint) Color.WHITE)) {
                    submitLabel.setText("Cannot leave vertex no. " + randomOrderArray[counterForSubmit] + " uncolored!");
                } else if (!availabilityCheck(randomOrderArray[counterForSubmit], (Color) graphComponent.getVertexArray()[randomOrderArray[counterForSubmit]].getFill())) {
                    submitLabel.setText("Cannot assign chosen colour! Try again!");
                } else {
                    submitLabel.setText("Try vertex no. " + randomOrderArray[counterForSubmit + 1] + "!");
                    currentVertex.setColorable(false); //You can't change the colour anymore
                    currentVertex.setStroke(Color.BLACK);
                    nextVertex.setColorable(true);
                    nextVertex.setStroke(Color.CYAN);
                    nextVertex.setStrokeWidth(2);
                    counterForSubmit++;
                }
            }
            else{
                if (currentVertex.getFill().equals((Paint) Color.WHITE)) {
                    submitLabel.setText("Cannot leave vertex no. " + randomOrderArray[counterForSubmit] + " uncolored!");
                }
                else if (!availabilityCheck(randomOrderArray[counterForSubmit], (Color) graphComponent.getVertexArray()[randomOrderArray[counterForSubmit]].getFill())) {
                    submitLabel.setText("Cannot assign chosen colour! Try again!");
                }
                else {

                    for(int i = 0; i < graph.getVerticesNumber(); i++){ //sets the strokes the same (for the case that the strokes were changed due to hints)
                        graphComponent.getVertexArray()[i].setStroke((Paint) Color.BLACK);
                        graphComponent.getVertexArray()[i].setStrokeWidth(2);
                    }

                    calculateScore();

                    messageLabel.setText("Your chromatic number is: " + getPlayerChromNumber() + "\nThe actual chromatic number is: " + calculator.getChromaticNumber());
                    scoreLabel.setText("Score: " + score + " / " + maxScore);

                    currentVertex.setColorable(false);
                    currentVertex.setStroke(Color.BLACK);
                }
            }
        }
    }


    @FXML
    public void hint() {

        if (graphComponent == null) { // checks whether graph already exists
            messageLabel.setText("Generate graph first");
            return;
        }

        if (calculator == null) { //checks whether calculator already exists
            calculator = new GraphCalculator(graph);
        }

        boolean[] mask = new boolean[graph.getVerticesNumber()]; // contains true values for vertices which were colored by the player
        int[] playerColors = graphComponent.getIdOfColors(); // colors of vertices at the point when Hint button was clicked
        graphVertex2D[] vertexArray = graphComponent.getVertexArray(); // array of vertices

        boolean[] maskCheck = new boolean[mask.length]; // array for checking whether mask is filled with true values
        Arrays.fill(maskCheck, true);

        int userChromNumber;
        int[] idealColors;

        for (int i = 0; i < vertexArray.length; i++) {
            if (playerColors[i] != 0) { // if color is assigned mark it in mask array
                mask[i] = true;
            }
        }

        if (Arrays.equals(mask, maskCheck)) { // calculates minimal chromatic number and player chromatic number if graph is fully colored
            userChromNumber = getPlayerChromNumber();

            if (userChromNumber == calculator.getExactChromaticNumber()) {
                messageLabel.setText("You're almost there, you're chromatic number is: " + userChromNumber +  ", but minimal is: " + calculator.getExactChromaticNumber());
            }

            return; // to enable modifying fully colored graphs - delete this return statement
        }
        else {
            // calculates chromatic number and color distribution relatively to player input (if chromatic number can't be found returns -1)
            userChromNumber = calculator.getExactChromaticNumber(playerColors.clone(), mask);
        }

        idealColors = calculator.getVertexColors(); // best color distribution gotten from chromatic number algorithm


        if (userChromNumber == -1) {
            messageLabel.setText("Chromatic number is unavailable");
            return;
        }


        if (playerColors[randomOrderArray[counterForSubmit]] != idealColors[randomOrderArray[counterForSubmit]]) {

            MouseEvent fakeEvent = new MouseEvent(
                    MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY,
                    5, true, true, true, true, true, true, true, true, true, true, null
            ); // creates artificial event to trigger event handler for a vertex

            if (idealColors[randomOrderArray[counterForSubmit]] > graphComponent.getColorList().size()) { // if there is no such color yet to fulfill chromatic number algorithm
                Color randColor = Color.color(Math.random(), Math.random(), Math.random()); // create random color

                colorPicker.getCustomColors().add(randColor);
                colorPicker.setValue(randColor);

                graphComponent.setCurrentColor(randColor); // set is as the current one
                hintCounter++;

            }
            else {
                Color currentColor = graphComponent.getColorList().get(idealColors[randomOrderArray[counterForSubmit]] - 1);

                colorPicker.setValue(currentColor);

                graphComponent.setCurrentColor(currentColor); // otherwise take already existent color
                hintCounter++;

            }

            vertexArray[randomOrderArray[counterForSubmit]].fireEvent(fakeEvent); // trigger event handler to change color of the vertex
        }
    }


    @FXML
    public void saveGraph() throws IOException { // saves graph to file
        if (graph == null) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);

        if (file != null){
            if (file.getPath().endsWith(".txt")) {
                graph.graphToFile(file.getPath());
            }
            else {
                graph.graphToFile(file.getPath() + ".txt");
            }
        }
    }


    @FXML
    public void calculateGraph() {
        if (graphComponent == null) {
            messageLabel.setText("Generate graph first");
            return;
        }

        if (calculator == null) {
            calculator = new GraphCalculator(graph);
        }


        if (!isShown) {
            isShown = true;

            graph.setChromaticNumber(calculator.getExactChromaticNumber());
            graph.setUpper_bound(calculator.getUpper_bound());
            graph.setLower_bound(calculator.getLower_bound());

            graphProperty.setText("Chromatic number: " + graph.getChromaticNumber() + "\n" + "Upper bound: " + graph.getUpper_bound() + "\n" + "Lower bound: " + graph.getLower_bound());
        }
        else {
            isShown = false;
            graphProperty.setText("");
        }
    }


    public void calculateScore(){
        if (graph.getChromaticNumber() == 0) {
            graph.setChromaticNumber(calculator.getExactChromaticNumber());
        }


        maxScore = 100 * graph.getVerticesNumber();
        score = Math.max(Math.round(((30.0 * graph.getVerticesNumber() + 70.0 * graph.getVerticesNumber() / (getPlayerChromNumber() - graph.getChromaticNumber() + 1)) - hintCounter * 150) * 100.0 ) / 100.0, 0);
    }


    @FXML
    public void updateColor() {
        if (graphComponent == null) {
            messageLabel.setText("Generate graph first");
            return;
        }

        if (colorPicker.getCustomColors().size() == 0) {
            colorPicker.getCustomColors().add(graphComponent.getCurrentColor());
        }

        if (!colorPicker.getCustomColors().contains(colorPicker.getValue())) {
            colorPicker.getCustomColors().add(colorPicker.getValue());
        }

        graphComponent.setCurrentColor(colorPicker.getValue());
    }


    /**
     *
     * @return Returns the player's chromatic number
     */
    private int getPlayerChromNumber(){

        int[] playerColors = new int[graph.getVerticesNumber()]; // colors of vertices at the point when Hint button was clicked
        graphVertex2D[] vertexArray = graphComponent.getVertexArray(); // array of vertices

        for (int i = 0; i < vertexArray.length; i++) {
            playerColors[i] = vertexArray[i].getColorId(); // gets numerical value of the color assigned to each vertex (no color = 0)
        }
        sort(playerColors);
        int chromNumber = playerColors[playerColors.length - 1];
        return  chromNumber;
    }


    /** Changes all verteces of a graph to be either colourable or not
     *
     * @param isColorable True if change to all colorable, false if all not colourable
     */
    private void setVertexColorable(boolean isColorable){

        int length = graphComponent.getVertexEventArray().size();
        for(int i = 0; i < length; i++){
            graphComponent.getVertexArray()[i].setColorable(isColorable);
        }
    }

    private boolean availabilityCheck(int currentVertex, Color colorToCheck){

        for(int i = 0; i < graph.getVertexRelations().length; i++){

            if(i == currentVertex){} // Don't check availability with the current vertex
            // if color of the taken vertex is equal to color of another vertex, and they are connected - color can not be assigned:
            else if(graph.getVertexRelations()[currentVertex][i]  &&  graphComponent.getVertexArray()[i].getFill().equals( (Paint) colorToCheck)){
                return false;
            }
        }
        return true; // otherwise color can be assigned
    }


    public void setVerticesUpperBound(int verticesNumber) {
        this.upperBoundVertices = verticesNumber;
    }


    public void setPlayerGraph(String path) {
        playerGraph = true;
        graph = ReadGraph.read(path);
        upperBoundVertices = graph.getVerticesNumber();

        if (graph == null) {
            messageLabel.setText("Graph cannot be read. Try generating random one");
            playerGraph = false;
        }
    }
}
