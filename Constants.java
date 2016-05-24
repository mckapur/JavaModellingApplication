package com.company;

import javafx.scene.paint.Color;
import java.util.HashMap;

/**
 * Created by rohankapur on 18/5/16.
 */
// This file stores any sort of magic numbers or constants (ie. settings for the GUI) used throughout the application.
public class Constants {
    // Different model/function types supported by the system
    public static enum ModelType {
        ModelTypePolynomial,
        ModelTypeLogarithm,
        ModelTypeExponential,
        ModelTypeTrig
    }

    // A HashMap storing the number of parameters each model should have
    public final static HashMap<ModelType, Integer> numParams = new HashMap<ModelType, Integer>();
    public static void setupConstants() {
        numParams.put(ModelType.ModelTypeTrig, 4);
        numParams.put(ModelType.ModelTypeLogarithm, 4);
        numParams.put(ModelType.ModelTypeExponential, 4);
    }

    // Gradient descent hyperparameters
    public static double LEARNING_RATE = 0.00005;
    public static final double LEARNING_ITERS = 100000;

    // General GUI constants
    public static final Color GENERAL_SCREEN_BG_COLOR = Color.WHITE;
    public static final double INPUT_SCREEN_WIDTH = 400;
    public static final double INPUT_SCREEN_HEIGHT = 400;
    public static final double MODEL_SELECTION_SCREEN_WIDTH = 800;
    public static final double MODEL_SELECTION_SCREEN_HEIGHT = 175;
    public static final int GRAPH_PLOT_MAX_GRIDATION_X = 10;
    public static final int GRAPH_PLOT_MAX_GRIDATION_Y = 10;
    public static final double GRAPH_PLOT_DRAW_INPUT_INCREMENT = 0.01;
    public static final int GRAPH_PLOT_WIDTH = 800;
    public static final int GRAPH_PLOT_HEIGHT = 600;
    public static final Color GRAPH_STROKE_COLOR = Color.BLACK;
    public static final Color GRAPH_PLOT_DATA_POINT_COLOR = Color.BLUE;
    public static final int GRAPH_STROKE_WIDTH = 2;
    public static final double GRAPH_PLOT_WINDOW_PADDING = 20.0f;
    public static final double GRAPH_PLOT_DATA_POINT_RADIUS = 3.0f;
}
