package com.company;

import javafx.geometry.Insets;
import javafx.scene.shape.Circle;

/**
 * Created by rohankapur on 24/5/16.
 */
// This view represents the screen that shows a specific model using a plot.
public class ModelViewScreen extends Screen {
    // The inputted dataset to show and the fitted model to overlay
    double[] xCoordinates;
    double[] yCoordinates;
    Model model;

    public ModelViewScreen(Model model, double[] xCoordinates, double[] yCoordinates, ScreenInterface delegate) {
        super("y = " + model.toString(), delegate);
        this.model = model;
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;

        this.draw();
    }

    // Draw in the visualization components
    public void draw() {
        int[] normalizedRange = this.getNormalizedRange(); // Though we have a dataset, we want to position it around zero so we put the y-axis in the center. This function ensures we have a normalized range (minX and maxX, minY and maxY) on the axes that achieves this.
        int xRange = normalizedRange[1] - normalizedRange[0]; // The range of x-coordinates
        int yRange = normalizedRange[3] - normalizedRange[2]; // The range of y-coordinates
        // The div factors decide when we want to draw a single gridation line, ie. every interval of x = 10 and y = 20? These values are decided based on the dataset.
        int xDivFactor = xRange >= Constants.GRAPH_PLOT_MAX_GRIDATION_X ? Constants.GRAPH_PLOT_MAX_GRIDATION_X : xRange; // The max limit we go to is every 10, otherwise it is the range
        int yDivFactor = yRange >= Constants.GRAPH_PLOT_MAX_GRIDATION_Y ? Constants.GRAPH_PLOT_MAX_GRIDATION_Y : yRange; // Same logic for the y-axis
        if (xDivFactor == 0) xDivFactor = 1; // So NaN does not occur!
        if (yDivFactor == 0) yDivFactor = 1;
        double xDrawStart = normalizedRange[0]; // Where we start drawing the model is, by default, where the x-axis begins
        if (this.model.modelType == Constants.ModelType.ModelTypeLogarithm) // However logarithms have a restricted domain (cannot take input <= 0)
            xDrawStart = (int)Math.floor(-this.model.getParameters()[2].weight / this.model.getParameters()[3].weight) + 1; // So based on the logarithm parameters we ensure that we start where the input > 0, with a +1 buffer because it makes drawing easier (asymptote causes drawing issues)

        // Create the graph plot using the GraphPlot class and all the settings: window sizes, ranges, etc.
        GraphPlot graphPlot = new GraphPlot(Constants.GRAPH_PLOT_WIDTH, Constants.GRAPH_PLOT_HEIGHT, xDrawStart, normalizedRange[0], normalizedRange[1], (int)(xRange / xDivFactor), normalizedRange[2], normalizedRange[3], (int)(yRange / yDivFactor), x -> model.compute(x) /* Lambda expression can be replaced by our model computation function */, Constants.GRAPH_PLOT_DRAW_INPUT_INCREMENT);

        // Draw in the inputted data points
        for (int i = 0; i < this.xCoordinates.length; i++) {
            Circle dataPoint = new Circle(Constants.GRAPH_PLOT_DATA_POINT_RADIUS); // Each data point is a circle shape
            dataPoint.setFill(Constants.GRAPH_PLOT_DATA_POINT_COLOR);
            // Translate them to the screen-mapped coordinates of their literal coordinates (we subtract by origin because translations translate from current position)
            dataPoint.setTranslateX(graphPlot.mapX(this.xCoordinates[i]) - graphPlot.mapX(0.0f));
            dataPoint.setTranslateY(graphPlot.mapY(this.yCoordinates[i]) - graphPlot.mapY(0.0f));
            this.getChildren().add(dataPoint); // Add the data points
        }
        this.getChildren().add(graphPlot); // Add the graph plot

        // Set window sizes and options eg. padding
        this.setPadding(new Insets(Constants.GRAPH_PLOT_WINDOW_PADDING));
        this.setPrefWidth(graphPlot.getPrefWidth());
        this.setPrefHeight(graphPlot.getPrefHeight());
    }

    // Converts a datasets inherent range (ie. x=4 to x=12) to a normalized version around the origin (ie. to x=-12, x=12) for both x-axis and y-axis
    public int[] getNormalizedRange() {
        // Get the max and min values of x and y in our dataset. Perform a simple sequential search.
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int i = 0; i < this.xCoordinates.length; i++) {
            int currX = (int)Math.floor(this.xCoordinates[i]);
            int currY = (int)Math.floor(this.yCoordinates[i]);
            if (currX < minX)
                minX = currX;
            else if (currX > maxX)
                maxX = currX;
            if (currY < minY)
                minY = currY;
            else if (currY > maxY)
                maxY = currY;
        }
        // Logic: we normalize around the "largest" in magnitude (disregarding sign). So for x=-30 and x=9, the largest in magnitude is x=-30, so we normalize by giong from x=-30 to x=30.
        if (Math.abs(minX) > Math.abs(maxX))
            maxX = -minX; // If minX is larger in magnitude than maxX, then maxX becomes the negative (so it becomes positive) of the minX -> only possible if minX is negative.
        else
            minX = -maxX; // Logic is switched around.
        // Logic applied for y
        if (Math.abs(minY) > Math.abs(maxY))
            maxY = -minY;
        else
            minY = -maxY;
        return new int[]{minX, maxX, minY, maxY};
    }
}
