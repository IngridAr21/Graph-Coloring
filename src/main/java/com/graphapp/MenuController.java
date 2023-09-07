package com.graphapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/** Controller for the Scene with the main menu on it
 *
 */
public class MenuController implements Initializable {
    @FXML
    private Button buttonPLAY;
    @FXML
    private TextField vertexField;
    @FXML
    private Label vertexLbl;
    @FXML
    private TextField fileField;
    @FXML
    private Label fileLabel;
    @FXML
    private RadioButton vertexBtn;
    @FXML
    private RadioButton fileBtn;
    @FXML
    public ComboBox<String> gameModesBox;
    @FXML
    private Label errorLabel;
    private final ObservableList<String> gameModes = FXCollections.observableArrayList("To The Bitter End", "Fixed time", "Random order");
    private int verticesUpperBound;
    private String filePath;
    private boolean playerGraph;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameModesBox.setValue(gameModes.get(0));
        gameModesBox.setItems(gameModes);

        fileField.setEditable(false);
        fileField.setDisable(true);

    }


    @FXML
    public void switchScene() {
        Parent root;

        try {
            if (vertexBtn.isSelected()) {
                playerGraph = false;
                verticesUpperBound = Integer.parseInt(vertexField.getText());

            } else if (fileBtn.isSelected()) {
                playerGraph = true;
                 filePath = fileField.getText();
            }

            if (gameModesBox.getValue().equals("To The Bitter End")) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMode1Controller.fxml"));
                root = fxmlLoader.load(); // important to load fxmlLoader before instantiating controller, because otherwise controller wouldn't be loaded

                GameMode1Controller controller = fxmlLoader.getController();

                if (playerGraph) {
                    controller.setPlayerGraph(filePath);
                }
                else {
                    controller.setVerticesUpperBound(verticesUpperBound);
                }

            } else if (gameModesBox.getValue().equals("Fixed time")) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMode2Controller.fxml"));
                root = fxmlLoader.load(); // important to load fxmlLoader before instantiating controller, because otherwise controller wouldn't be loaded

                GameMode2Controller controller = fxmlLoader.getController();

                if (playerGraph) {
                    controller.setPlayerGraph(filePath);
                }
                else {
                    controller.setVerticesUpperBound(verticesUpperBound);
                }

            }
            else {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMode3Controller.fxml"));
                root = fxmlLoader.load(); // important to load fxmlLoader before instantiating controller, because otherwise controller wouldn't be loaded

                GameMode3Controller controller = fxmlLoader.getController();

                if (playerGraph) {
                    controller.setPlayerGraph(filePath);
                }
                else {
                    controller.setVerticesUpperBound(verticesUpperBound);
                }

            }

            Scene scene = new Scene(root);

            Stage stage = (Stage) buttonPLAY.getScene().getWindow();

            stage.setTitle(gameModesBox.getValue());
            stage.setScene(scene);

            stage.setOnCloseRequest(event -> {
                event.consume();
                System.exit(0);
            });

            stage.show();
        }
        catch (Exception err) {
            if (err instanceof NumberFormatException) {
                errorLabel.setText("Only numerical values are accepted. Try again");

            } else if (err instanceof IllegalArgumentException) {
                errorLabel.setText("Upper bound OR custom graph must be chosen. Not both and not none of them");
            }
            else {
                err.getStackTrace();
            }

        }
    }


    @FXML
    public void fileChoose() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            errorLabel.setText("File not found");
            return;
        }

        if (!file.getName().endsWith(".txt")) {
            errorLabel.setText("Illegal file format. Only .txt files are accepted");
            return;
        }

        fileField.setText(String.valueOf(file));
    }


    @FXML
    public void radioSwitch() {
        if (vertexBtn.isSelected()) {
            fileLabel.setDisable(true);
            fileField.setDisable(true);

            vertexLbl.setDisable(false);
            vertexField.setDisable(false);

        } else if (fileBtn.isSelected()) {
            vertexLbl.setDisable(true);
            vertexField.setDisable(true);

            fileLabel.setDisable(false);
            fileField.setDisable(false);
        }
    }

}
