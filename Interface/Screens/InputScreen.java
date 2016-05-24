package com.company;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by rohankapur on 24/5/16.
 */
// This class defines the screen which handles all input
class InputScreen extends Screen {
    TextField xTextField; // Where you input an x-coordinate
    TextField yTextField; // Where you input a y-coordinate
    // Other basic UI components
    Button addCoordinateButton;
    Button searchModelsButton;
    Label coordinateListLabel; // A list of all the added coordinates

    // A mutable array of the coordinates (unlimited dataset size)
    ArrayList<Double> xCoordinates;
    ArrayList<Double> yCoordinates;

    InputScreen(ScreenInterface delegate) {
        super("Input Coordinates", delegate);
        xCoordinates = new ArrayList<Double>();
        yCoordinates = new ArrayList<Double>();

        this.draw();
    }

    // Draw the components on screen
    public void draw() {
        this.setPrefWidth(Constants.INPUT_SCREEN_WIDTH);
        this.setPrefHeight(Constants.INPUT_SCREEN_HEIGHT);

        // The y-offset defines the initial y-coordinates of components added to the screen, which go in the vertical center (not always desired)
        final double yOffset = this.getPrefWidth() / 2;

        // We add our other components and place them in the correct position
        this.xTextField = new TextField();
        this.xTextField.setPromptText("Enter x-coordinate");
        this.xTextField.setTranslateY(50.0f - yOffset);
        this.yTextField = new TextField();
        this.yTextField.setPromptText("Enter y-coordinate");
        this.yTextField.setTranslateY(100.0f - yOffset);

        this.addCoordinateButton = new Button("Add Coordinate");
        this.searchModelsButton = new Button("Search Models");
        this.addCoordinateButton.setTranslateY(150.0f - yOffset);
        this.addCoordinateButton.setTranslateX(-75.0f);
        this.searchModelsButton.setTranslateY(150.0f - yOffset);
        this.searchModelsButton.setTranslateX(75.0f);

        this.coordinateListLabel = new Label("Coordinates: ");
        this.coordinateListLabel.setWrapText(true);
        this.coordinateListLabel.setPrefWidth(this.getPrefWidth() * 0.95); // Full width would lead to text being blocked
        this.coordinateListLabel.setPrefHeight(200.0f);
        this.coordinateListLabel.setTranslateY(275.0f - yOffset);

        // Add all the initialized components
        this.getChildren().add(this.xTextField);
        this.getChildren().add(this.yTextField);
        this.getChildren().add(this.addCoordinateButton);
        this.getChildren().add(this.searchModelsButton);
        this.getChildren().add(this.coordinateListLabel);
        this.createActionHandlers(); // Create the actions which occur when relevant buttons are clicked
    }
    // Actions that occur when buttons are clicked occur here
    public void createActionHandlers() {
        this.addCoordinateButton.setOnAction(new EventHandler<ActionEvent>() { // Action for adding a coordinate
            @Override
            public void handle(ActionEvent e) {
                // Get text in the coordinate input fields
                String xText = xTextField.getText();
                String yText = yTextField.getText();
                try {
                    // Try to parse out coordinate fields into doubles
                    Double x = Double.parseDouble(xText);
                    Double y = Double.parseDouble(yText);
                    if (!xCoordinates.contains(x)) { // If this is a new x-coordinate
                        coordinateListLabel.setText(coordinateListLabel.getText() + (xCoordinates.size() > 0 ? ", " : "") + " " + "(" + x + ", " + y + ")"); // Add a new coordinate in the format (a, b) to the list of coordinates
                        // Add the coordinates to our arrays of coordinates
                        xCoordinates.add(x);
                        yCoordinates.add(y);

                        // Reset behavior of text fields
                        xTextField.setText("");
                        yTextField.setText("");
                        xTextField.requestFocus(); // So user can immediately type in a new x-coordinate
                    }
                    else { // If this x-coordinate already exists, this dataset creates a many-to-one/many-to-many function, which is NOT a real function. Thus we disallow it.
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setContentText("Already added a coordinate with that x value; functions must be one-to-one or many-to-one only!");
                        alert.showAndWait();
                    }
                } catch (NumberFormatException err) { // If parsing failed, then the user did not enter a double/number!
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setContentText("Please type in (decimal) numbers only for the coordinates.");
                    alert.showAndWait();
                }
            }
        });
        // The action that occurs when the user clicked the search models button
        this.searchModelsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (xCoordinates.size() == 0) // If the dataset is empty, we ignore this request
                    return;
                // A simple loading screen
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Loading...");
                alert.setContentText("Please wait while we prepare your models.");
                alert.showAndWait();
                // Convert our ArrayList of coordinates into primitive arrays for handling in other classes
                double[] xCoordinatesPrimitive = new double[xCoordinates.size()];
                double[] yCoordinatesPrimitive = new double[yCoordinates.size()];
                for (int i = 0; i < xCoordinatesPrimitive.length; i++) {
                    xCoordinatesPrimitive[i] = xCoordinates.get(i).doubleValue();
                    yCoordinatesPrimitive[i] = yCoordinates.get(i).doubleValue();
                }
                delegate.modelSearchRequested(xCoordinatesPrimitive, yCoordinatesPrimitive); // Tell delegate (the main interface) that the user is requesting to find models with the dataset
                // Reset dataset
                coordinateListLabel.setText("Coordinates: ");
                xCoordinates.clear();
                yCoordinates.clear();
            }
        });
    }
}