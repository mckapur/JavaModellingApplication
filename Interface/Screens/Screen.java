package com.company;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Created by rohankapur on 24/5/16.
 */
// Creates a new arbitrary, custom screen for the application. Subclasses will provide specific screens.
abstract class Screen extends StackPane {
    public ScreenInterface delegate; // Each screen has a delegate which responds to certain methods in the ScreenInterface interface
    public Color backgroundColor; // Each screen has a background color
    public String title; // Each screen has a title

    abstract public void draw(); // Each screen has a draw method

    public Screen(String title, ScreenInterface delegate) {
        this.backgroundColor = Constants.GENERAL_SCREEN_BG_COLOR;
        this.title = title;
        this.delegate = delegate;
    }
}

// This is an interface which is used to create "delegates" that define a working relationship between a screen and a main multiple-screen handler class. When an action occurs on a screen that has functionality/logic outside its realm, it sends a message to the delegate (which should be the owner).
interface ScreenInterface {
    public void modelSearchRequested(double[] xCoordinates, double[] yCoordinates);
    public void modelPresentationRequested(final Model model);
}
