package com.graphapp;

import javafx.application.Platform;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Arrays.sort;


public class GameMode1Controller {
    @FXML
    public FlowPane root;
    @FXML
    public Pane graphArea;
    @FXML
    public Button drawBtn;
    @FXML
    public Button save;
    @FXML
    public Button backBtn;
    @FXML
    public Label graphProperty;
    @FXML
    public Button calcBtn;
    @FXML
    public Button hintBtn;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public Label messageLabel;
    @FXML
    public Label timerLabel;
    @FXML
    public Label scoreLabel;
    private Graph graph;
    private Graph2D graphComponent;
    private GraphCalculator calculator;
    private boolean isFinished;
    private boolean playerGraph;
    private boolean isShown;
    private int upperBoundVertices;
    private Timer timer;
    private int timerCounter = 0; // timer value in seconds
    private int timerSeconds;
    private int timerMinutes;
    private int hintCounter;


    @FXML
    public void toMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }



    @FXML
    public void graphTouched() {
        if (graphComponent == null) { // checks whether graph already exists
            messageLabel.setText("Generate graph first");
            return;
        }

        if (calculator == null) { //checks whether calculator already exists
            calculator = new GraphCalculator(graph);
        }



        if (!isGraphColored()) { // if graph is not fully colored
            return;
        }


        int playerChromaticNumber = getPlayerChromaticNumber();

        if (calculator.getExactChromaticNumber() < playerChromaticNumber) {
            messageLabel.setText("You didn't reach the minimal chromatic number yet. Try once more");
            return;
        }

        messageLabel.setText("You've got it! Congratulations! Your chromatic number: " + playerChromaticNumber);

        timer.cancel();
        timer.purge();

        isFinished = true;

        scoreLabel.setText("Score: " + calculateScore());
    }


    @FXML
    public void drawGraph() {
        graphArea.getChildren().clear(); // clear previous graph
        colorPicker.getCustomColors().clear();

        isFinished = false;
        hintCounter = 0;

        scoreLabel.setText("Score: ");
        graphProperty.setText("");

        if (!playerGraph) {
            GraphGenerator generator = new GraphGenerator(upperBoundVertices); // create new generator
            graph = new Graph(generator.getVerticesNumber(), generator.getEdgesNumber(), generator.getConnections()); // create new graph object
            messageLabel.setText("Random graph was generated");
        }
        else {
            messageLabel.setText("Custom graph was loaded");
        }

        calculator = new GraphCalculator(graph); // create new calculator
        graphComponent = new Graph2D(graph, graphArea, colorPicker.getValue()); // create new 2D component of the graph

        graphArea.getChildren().addAll(graphComponent.draw()); // add graph2D object to playable area

        if (timer != null) {
            timer.cancel();
        }

        playerGraph = false;

        timerCounter = 0;
        timer();
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


    @FXML
    public void hint() {
        if (graphComponent == null) { // checks whether graph already exists
            messageLabel.setText("Generate graph first");
            return;
        }

        if (isFinished) {
            return;
        }

        if (calculator == null) { //checks whether calculator already exists
            calculator = new GraphCalculator(graph);
        }

        hintCounter++;

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
            userChromNumber = getPlayerChromaticNumber();

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

        for (int i = 0; i < mask.length; i++) { // picks first vertex with not the same color as in the best color distribution
            if (playerColors[i] != idealColors[i]) {

                MouseEvent fakeEvent = new MouseEvent(
                        MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY,
                        5, true, true, true, true, true, true, true, true, true, true, null
                ); // creates artificial event to trigger event handler for a vertex

                if (idealColors[i] > graphComponent.getColorList().size()) { // if there is no such color yet to fulfill chromatic number algorithm
                    Color randColor = Color.color(Math.random(), Math.random(), Math.random()); // create random color

                    colorPicker.getCustomColors().add(randColor);
                    colorPicker.setValue(randColor);

                    graphComponent.setCurrentColor(randColor); // set is as a current one

                }
                else {
                    colorPicker.setValue(graphComponent.getColorList().get(idealColors[i] - 1));

                    graphComponent.setCurrentColor(graphComponent.getColorList().get(idealColors[i] - 1)); // otherwise take already existent color
                }


                vertexArray[i].fireEvent(fakeEvent); // trigger event handler to change color of the vertex
                break;
            }
        }
    }


    @FXML
    public void timer() {
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timerCounter++;
                    timerSeconds = timerCounter % 60;
                    timerMinutes = timerCounter / 60;

                    if((timerSeconds <  10) && (timerMinutes < 10)){
                        timerLabel.setText("0" + timerMinutes + ":0" + timerSeconds);
                    }
                    else if (timerMinutes < 10){
                        timerLabel.setText("0" + timerMinutes + ":" + timerSeconds);
                    }
                    else{
                        timerLabel.setText(timerMinutes + ":" + timerSeconds);

                    }
                });
            }
        }, 0, 1000);
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


    private double calculateScore() {
        if (graph.getChromaticNumber() == 0) {
            graph.setChromaticNumber(calculator.getExactChromaticNumber());
        }

        return Math.max(50 * graph.getVerticesNumber() - timerCounter - (100 * hintCounter), 0);
    }


    private boolean isGraphColored() {
        int[] graphColors = graphComponent.getIdOfColors();

        for (int i = 0; i < graphColors.length; i++) {
            if (graphColors[i] == 0) {
                return false;
            }
        }

        return true;
    }


    private int getPlayerChromaticNumber() {
        int[] colors = graphComponent.getIdOfColors(); // colors of vertices at the point when Hint button was clicked

        int[] colorsClone = colors.clone();
        Arrays.sort(colorsClone);

        return colorsClone[colorsClone.length - 1];
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