package com.company;

/**
 * Created by rohankapur on 18/5/16.
 */
// This class defines an abstract model of arbitrary function type
public abstract class Model {
    public Constants.ModelType modelType; // The type of function
    public double cost; // The tentative cost (error) of the model with the respective parameters and an assumed dataset
    private Parameter[] parameters; // The parameters (weights) of the model
    public double input; // The tentative input to the model

    public Model(int paramSize) { // Model subclasses pass the parameter size
        // Initialize all the parameters as empty parameters
        this.parameters = new Parameter[paramSize];
        for (int i = 0; i < this.parameters.length; i++)
            this.parameters[i] = new Parameter();
    }

    public Parameter[] getParameters() { // Accessor method
        return this.parameters;
    }
    // This function slightly "moves" a parameter weight by some small value, mainly used in gradient descent
    public void perturbParameter(int paramIndex, double perturbWeight) {
        this.parameters[paramIndex].weight += perturbWeight; // Update a weight at a specific index by a specific value
    }

    public abstract double compute(double input); // Computes the output of a function from any given input - depends on the function type and parameters
    public abstract void computeWeightDerivatives(); // Computes the numerical derivatives of the output w.r.t. each weight using differentiation rules
    public abstract String toString(); // Output the model in a human readable form (ie. f(x) = 3 + 4x)

    public double output() { // Computes the output of a function from the input member
        return this.compute(this.input);
    }
    public void updateParameterGradients(double[] weightDerivatives) { // Updates the gradients of each of the parameters (the numerical derivative of the output w.r.t. each parameter), mainly used during gradient descent
        for (int i = 0; i < this.parameters.length; i++)
            this.parameters[i].gradient = weightDerivatives[i];
    }
}
