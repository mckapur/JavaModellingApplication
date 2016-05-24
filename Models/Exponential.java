package com.company;

/**
 * Created by rohankapur on 21/5/16.
 */
// This class implements a specialized exponential function model.
public class Exponential extends Model implements Comparable {
    public Exponential() {
        super(Constants.numParams.get(Constants.ModelType.ModelTypeExponential).intValue());
        this.modelType = Constants.ModelType.ModelTypeExponential;
    }

    public double compute(double input) {
        // Output: a + be^(c+dx)
        return this.getParameters()[0].weight + this.getParameters()[1].weight * Math.exp(this.getParameters()[2].weight + this.getParameters()[3].weight * input);
    }
    public void computeWeightDerivatives() {
        // When finding derivatives w.r.t. weights, use e^x -> e^x rule and chain rule
        double[] numericalDerivatives = new double[this.getParameters().length];
        numericalDerivatives[0] = 1;
        numericalDerivatives[1] = Math.exp(this.getParameters()[2].weight + this.getParameters()[3].weight * this.input);
        numericalDerivatives[2] = this.getParameters()[1].weight * Math.exp(this.getParameters()[2].weight + this.getParameters()[3].weight * this.input);
        numericalDerivatives[3] = this.getParameters()[1].weight * this.input * Math.exp(this.getParameters()[2].weight + this.getParameters()[3].weight * this.input);
        this.updateParameterGradients(numericalDerivatives);
    }

    public String toString() {
        // Output: a + be^(c+dx)
        return "" + this.getParameters()[0].weight + " + " + this.getParameters()[1].weight + "e^(" + this.getParameters()[2].weight + " + " + this.getParameters()[3].weight + "x)";
    }
    public int compareTo(Object T) { // A model ranks higher in search on the basis of a lower cost
        return this.cost < ((Model) T).cost ? 1 : -1;
    }
}
