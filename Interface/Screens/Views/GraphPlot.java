package com.company;

import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import java.util.function.Function;

/**
 * Created by rohankapur on 23/5/16.
 */
// This class implements the GUI functions of a graph plot
public class GraphPlot extends Pane {
    private double inputIncrement; // A curve is a series of (theoretically infinite) lines. However, this is not possible in computers, so we set a very small distance value at which we draw a line connecting two points.
    private double xDrawStart; // Where to start drawing the curve (useful for logarithms)
    private Path path; // The function curve
    GraphAxes graphAxes; // The axes of the plot (x-axis and y-axis)
    Function<Double, Double> function; // The function we use as a lambda expression

    public GraphPlot(int resolutionWidth, int resolutionHeight, double xDrawStart, int xMin, int xMax, double xDrawInterval, int yMin, int yMax, double yDrawInterval, Function<Double, Double> function, double inputIncrement) {
        this(function, inputIncrement);
        this.xDrawStart = xDrawStart;
        this.graphAxes = new GraphAxes(resolutionWidth, resolutionHeight, xMin, xMax, xDrawInterval, yMin, yMax, yDrawInterval);

        this.setPlotOpts();
        this.iterativelyDrawFunction();
    }
    public GraphPlot(Function<Double, Double> function, double inputIncrement) {
        this.inputIncrement = inputIncrement;
        this.function = function;
        this.path = new Path();
    }

    public void setPlotOpts() {
        // Set basic settings for the function curve
        this.path.setStroke(Constants.GRAPH_STROKE_COLOR);
        this.path.setStrokeWidth(Constants.GRAPH_STROKE_WIDTH);
        this.path.setClip(new Rectangle(0, 0, this.graphAxes.getPrefWidth(), this.graphAxes.getPrefHeight()));

        // Set size of this interface
        setMinSize(this.graphAxes.getMinWidth(), this.graphAxes.getMinHeight());
        setPrefSize(this.graphAxes.getPrefWidth(), this.graphAxes.getPrefHeight());
        setMaxSize(this.graphAxes.getMaxWidth(), this.graphAxes.getMaxHeight());
    }

    // This function draws a curve using a series of small lines that connect together
    public void iterativelyDrawFunction() {
        double x = this.xDrawStart;
        double y = this.function.apply(x); // Get starting x and y

        // We map the x and y coordinates from their literal values to their pixel coordinates on screen, then we "move" the path to the mapped starting point to begin drawing.
        this.path.getElements().add(new MoveTo(mapX(x), mapY(y)));
        while (x < this.graphAxes.getXAxis().getUpperBound()) { // Draw a line until we reach the end of the x-axis
            x += this.inputIncrement; // Increment x (by a small number) so we draw a new line
            y = this.function.apply(x); // Find new y from incremented x
            this.path.getElements().add(new LineTo(mapX(x), mapY(y))); // Get screen-mapped coordinates and add new line to the path
        }

        getChildren().setAll(this.graphAxes, this.path);
    }

    /* These methods map the x and y literal dataset coordinates to positions on the interface plot on the computer screen. Scaling and translation of the coordinates occur. */
    public double mapX(double x) {
        double xTranslate = this.graphAxes.getPrefWidth() / 2;
        double xScale = this.graphAxes.getPrefWidth() / (this.graphAxes.getXAxis().getUpperBound() - this.graphAxes.getXAxis().getLowerBound());
        return x * xScale + xTranslate;
    }
    public double mapY(double y) {
        double yTranslate = this.graphAxes.getPrefHeight() / 2;
        double yScale = this.graphAxes.getPrefHeight() / (this.graphAxes.getYAxis().getUpperBound() - this.graphAxes.getYAxis().getLowerBound());
        return -y * yScale + yTranslate; // -y because a positive translation is downward
    }
}

// This is a private class (not relevant outside of here) that represents the GUI of a set of axes
class GraphAxes extends Pane {
    private int resolutionWidth, resolutionHeight, xMin, xMax, yMin, yMax; // Ranges on the axes, sizes, etc.
    private double xDrawInterval, yDrawInterval; // This is the gridation interval
    // The actual axes
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    public GraphAxes(int resolutionWidth, int resolutionHeight, int xMin, int xMax, double xDrawInterval, int yMin, int yMax, double yDrawInterval) {
        this.resolutionWidth = resolutionWidth;
        this.resolutionHeight = resolutionHeight;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.xDrawInterval = xDrawInterval;
        this.yDrawInterval = yDrawInterval;

        this.drawAxes();
        this.setAxesOpts();
    }

    // Draw in the axes with their appropriate settings
    private void drawAxes() {
        xAxis = new NumberAxis(xMin, xMax, xDrawInterval);
        xAxis.setSide(Side.BOTTOM);
        xAxis.setMinorTickVisible(false); // We don't want to see too much gridation
        xAxis.setPrefWidth(resolutionWidth);
        xAxis.setLayoutY(resolutionHeight / 2); // Position the x-axis coorectly (in the vertical center)

        yAxis = new NumberAxis(yMin, yMax, yDrawInterval);
        yAxis.setSide(Side.RIGHT);
        yAxis.setMinorTickVisible(false);
        yAxis.setPrefHeight(resolutionHeight);
        yAxis.setLayoutX(resolutionWidth / 2); // Position the y-axis correctly (in the horizonal center)

        this.getChildren().setAll(xAxis, yAxis);
    }
    private void setAxesOpts() {
        // Set interface sizes
        this.setPrefSize(resolutionWidth, resolutionHeight);
        this.setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        this.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
    }

    // Accessor methods
    public NumberAxis getXAxis() {
        return xAxis;
    }
    public NumberAxis getYAxis() {
        return yAxis;
    }
}