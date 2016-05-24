package com.company;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by rohankapur on 24/5/16.
 */
// This class defines the screen that handles the toggle-ability of the different models found.
class ModelSelectionScreen extends Screen {
    Model[] models; // The models to present to the users
    Model selectedModel; // The model that is currently selected

    public ModelSelectionScreen(Model[] models, ScreenInterface delegate) {
        super("Select Model", delegate);

        // Parse out only the valid models (no NaN parameters - divergence during gradient descent)
        ArrayList<Model> validModels = new ArrayList<Model>(); // Define temporary array to store valid models
        for (Model model : models) { // Iterate through our models
            if (!Double.isNaN(model.cost)) // Only add model if cost is not NaN
                validModels.add(model);
        }
        this.models = validModels.toArray(new Model[validModels.size()]); // Conver the ArrayList to primitive
        Arrays.sort(this.models, Collections.reverseOrder()); // Sort the models by increasing cost/error

        this.draw();
    }

    // Draw in the basic screen components
    public void draw() {
        // Set basic sizes
        this.setPrefWidth(Constants.MODEL_SELECTION_SCREEN_WIDTH);
        this.setPrefHeight(Constants.MODEL_SELECTION_SCREEN_HEIGHT);
        double yOffset = this.getPrefHeight() / 2; // y-offset is the default (vertical center) y-coordinate of every added item to the interface

        // Create an array of model "descriptions" - a human readable version of the model so the user can select
        ArrayList<String> modelDescriptions = new ArrayList<String>();
        for (Model model : models)
            modelDescriptions.add("Function: " + model.toString() + " | Average error: " + model.cost); // We use each model's toString() function and concatenate the model error to taht
        // Create a combo box with these descriptions
        ObservableList<String> options = FXCollections.observableArrayList(modelDescriptions);
        ComboBox comboBox = new ComboBox(options);
        comboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() { // Observe when the selected model in the combo box changes
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { // Member observation
                // Update the selected model in the models array based on the index of selection of the combo box
                selectedModel = models[newValue.intValue()];
            }
        });
        comboBox.setTranslateY(50.0f - yOffset);

        Button copyModel = new Button("Copy Model");
        Button showModel = new Button("Show Model");
        copyModel.setTranslateY(100.0f - yOffset);
        showModel.setTranslateY(100.0f - yOffset);
        copyModel.setTranslateX(-75.0f);
        showModel.setTranslateX(75.0f);

        copyModel.setOnAction(new EventHandler<ActionEvent>() { // The event handler for the Copy Model button
            @Override
            public void handle(ActionEvent e) {
                if (selectedModel != null) { // Make sure that there is a selected model to copy
                    // Copy the model description to the clipboard
                    StringSelection stringSelection = new StringSelection(selectedModel.toString());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
            }
        });
        showModel.setOnAction(new EventHandler<ActionEvent>() { // Event handler for the Show Model button
            @Override
            public void handle(ActionEvent e) {
                if (selectedModel != null) // Make sure that there is a selected model to copy
                    delegate.modelPresentationRequested(selectedModel); // Forward responsibility to delegates to present the plot/screen of a specific model
            }
        });

        // Add components to screen
        this.getChildren().add(comboBox);
        this.getChildren().add(copyModel);
        this.getChildren().add(showModel);
    }
}