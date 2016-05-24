package com.company;

/**
 * Created by rohankapur on 18/5/16.
 */
// This class performs optimization by taking a model and a dataset and finding the optimal parameters for that dataset.
public class Optimizer {
    // Hyperparameters for the gradient descent algorithm
    double alpha = Constants.LEARNING_RATE; // Step size hyperparameter for gradient descent
    double iters = Constants.LEARNING_ITERS; // Number of iterations to perform gradient descent over
    TrainingSet trainingSet; // Instances of dataset (x-coord and y-coord) to train on
    Model model; // The model we are training

    // Constructors with support for hyperparameters and no hyperparameters
    public Optimizer(Model _model, TrainingSet _trainingSet) {
        this.trainingSet = _trainingSet;
        this.model = _model;
    }
    public Optimizer(Model _model, TrainingSet _trainingSet, double alpha) {
        this(_model, _trainingSet);
        this.alpha = alpha;
    }
    public Optimizer(Model _model, TrainingSet _trainingSet, double alpha, double iters) {
        this(_model, _trainingSet, alpha);
        this.iters = iters;
    }

    // Compute the cost/error of the model parameters by using Cost = 1/(2m) * sum[(hypothesis(Params) - actual)^2], where m is the number of coordinates
    public double computeCost() {
        int m = this.trainingSet.size();
        double cost = 0.0;
        while (this.trainingSet.hasNextCase()) { // Iterate through all raining cases
            TrainingCase currentCase = this.trainingSet.getNextCase();
            this.model.input = currentCase.input; // Set hypothesis model input to the training case input
            cost += Math.pow(this.model.output() - currentCase.output, 2); // Get the square descrepency between training case output and hypothesis output
        }
        cost /= (2.0 * m);
        return cost;
    }
    // Compute the numerical derivatives of the cost/error function w.r.t. weights.
    // Derivation: d/dParams [1/(2m) * sum[(hypothesis(Params) - actual)^2]] = 1/m * sum(hypothesis(Params) - actual) * d/dParams(hypothesis(Params))
    // We already know d/dParams(hypothesis(Params)) from the individual model parameter gradients
    private double[] computeNumericalErrorDerivatives() {
        int m = this.trainingSet.size();
        double derivatives[] = new double[this.model.getParameters().length];
        // We get gradients for each of the weights
        for (int i = 0; i < derivatives.length; i++) {
            double numericalDerivative = 0;
            while (this.trainingSet.hasNextCase()) { // Iterate through all training cases
                TrainingCase currentCase = this.trainingSet.getNextCase();
                this.model.input = currentCase.input;
                this.model.computeWeightDerivatives();
                numericalDerivative += (this.model.output() - currentCase.output) * this.model.getParameters()[i].gradient; // Add discrepancy onto the individual parameter, multiplied by parameter gradient using chain rule
            }
            numericalDerivative /= m;
            derivatives[i] = numericalDerivative;
        }
        return derivatives;
    }
    // Train the model by getting weight derivatives and updating weights over a large number of iterations
    public Model train() {
        if (this.model == null) // If no current model, return null
            return null;
        for (int j = 0; j < iters; j++) { // Go through all the gradient descent iterations
            double[] numericalErrorDerivatives = this.computeNumericalErrorDerivatives(); // Get gradients for all parameters/weights
            for (int i = 0; i < numericalErrorDerivatives.length; i++) { // Go through each gradient
                // Make update: Param_i := Param_i - alpha * dCost/dParam_j
                double updateStep = -(alpha * numericalErrorDerivatives[i]); // Define the update value
                this.model.perturbParameter(i, updateStep); // Use perturbation function in the model
            }
        }
        // After all these iterations, we assume that the model has been trained with optimum parameters
        return this.model; // Return the trained model
    }

    // Prediction process: set model input and compute output
    public double predict(double x) {
        this.model.input = x;
        return this.model.output();
    }
}
