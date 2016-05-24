package com.company;

public class Main {

    public static void main(String[] args) {
        Constants.setupConstants(); // Setup the global constants in the constants file
        RegressionInterface.main(args); // Move responsibility over to main regression interface
    }
}
