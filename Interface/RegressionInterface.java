package com.company;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by rohankapur on 23/5/16.
 */
// This class is the central screen for the regression
public class RegressionInterface extends Application implements ScreenInterface {
    InputScreen inputScreen; // The screen for inputting data
    ModelSelectionScreen modelSelectionScreen; // The screen for selecting a model
    ModelViewScreen modelViewScreen; // The screen for viewing a model
    // Dataset
    double[] xCoordinates;
    double[] yCoordinates;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        this.inputScreen = new InputScreen(this);
        this.presentScreen(this.inputScreen);
    }

    // Presents an arbitrary screen by creating a new stage with a new scene of that screen and presenting the stage
    public void presentScreen(Screen screen) {
        Stage stage = new Stage();
        stage.setTitle(screen.title);
        if (screen.getScene() != null) {
            screen.getScene().setRoot(screen);
            stage.setScene(screen.getScene());
        }
        else
            stage.setScene(new Scene(screen, screen.backgroundColor));
        stage.show();
    }

    // Called when the input screen requests to find models to fit to a given inputted dataset
    public void modelSearchRequested(double[] xCoordinates, double[] yCoordinates) {
        // Set dataset and create training set
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;
        TrainingSet trainingSet = new TrainingSet(this.xCoordinates, this.yCoordinates);

        // Perform optimization on multiple types of models with different hyperparameters (but same training set)

        Optimizer optimizerPoly1 = new Optimizer(new Polynomial(1), trainingSet, 0.001, 20000);
        final Model poly1Model = optimizerPoly1.train();
        poly1Model.cost = optimizerPoly1.computeCost();

        Optimizer optimizerPoly2 = new Optimizer(new Polynomial(2), trainingSet);
        final Model poly2Model = optimizerPoly2.train();
        poly2Model.cost = optimizerPoly2.computeCost();

        Optimizer optimizerPoly3 = new Optimizer(new Polynomial(3), trainingSet);
        final Model poly3Model = optimizerPoly3.train();
        poly3Model.cost = optimizerPoly3.computeCost();

        Optimizer optimizerSin = new Optimizer(new Sin(), trainingSet, 0.001);
        final Model sinModel = optimizerSin.train();
        sinModel.cost = optimizerSin.computeCost();

        Optimizer optimizerCos = new Optimizer(new Cos(), trainingSet, 0.001);
        final Model cosModel = optimizerCos.train();
        cosModel.cost = optimizerCos.computeCost();

        Optimizer optimizerLogarithm = new Optimizer(new Logarithm(), trainingSet, 0.0001, 1000000);
        final Model logModel = optimizerLogarithm.train();
        logModel.cost = optimizerLogarithm.computeCost();

        Optimizer optimizerExponential = new Optimizer(new Exponential(), trainingSet, 0.000001, 50000);
        final Model expModel = optimizerExponential.train();
        expModel.cost = optimizerExponential.computeCost();

        // Create the model selection screen from the optimized models and present it
        this.modelSelectionScreen = new ModelSelectionScreen(new Model[]{poly1Model, poly2Model, poly3Model, sinModel, cosModel, logModel, expModel}, this);
        this.presentScreen(this.modelSelectionScreen);
    }

    // Called when the model selection screen asks to present a specific model
    public void modelPresentationRequested(final Model model) {
        // Create and present a model view screen for a specific model
        this.modelViewScreen = new ModelViewScreen(model, this.xCoordinates, this.yCoordinates, this);
        this.presentScreen(this.modelViewScreen);
    }
}
