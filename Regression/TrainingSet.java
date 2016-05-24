package com.company;

/**
 * Created by rohankapur on 22/5/16.
 */
// A training set is a set of training examples.
public class TrainingSet {
    private int currentIndex = 0; // A counter that we use for collection iteration
    private TrainingCase[] trainingCases; // All the training examples

    private TrainingSet(int size) { // Initializes a new training set with a certain size
        this.trainingCases = new TrainingCase[size];
    }
    public TrainingSet(double[] x, double[] y) { // Initializes an array of x and y coordinates
        this(x.length);
        for (int i = 0; i < x.length; i++)
            trainingCases[i] = new TrainingCase(x[i], y[i]);
    }

    // Custom collection iteration methods
    public int size() { // The number of training examples
        return this.trainingCases.length;
    }
    public boolean hasNextCase() { // Checks if a next training case exists for the iteration
        if (trainingCases.length > currentIndex) // Training cases are left to iterate through if the counter has not reached the number of cases
            return true;
        else {
            currentIndex = 0; // Reset the currentIndex if a next training case does not exist
            return false;
        }
    }
    public TrainingCase getNextCase() { // Fetches the next training case in the current iteration, if exists
        TrainingCase nextCase = hasNextCase() ? trainingCases[currentIndex] : null;
        currentIndex++; // Increment the counter
        return nextCase;
    }
}
