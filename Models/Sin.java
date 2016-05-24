package com.company;

/**
 * Created by rohankapur on 22/5/16.
 */
// This class is a specialized sine function model of model type Trig.
public class Sin extends Trig implements Comparable {
    Sin() {
        super(); // The Trig class handles generic model member initialization
    }

    public double compute(double input) {
        // Output: a + bsin(c + dx)
        return this.getParameters()[0].weight + this.getParameters()[1].weight * Math.sin(this.getParameters()[2].weight + this.getParameters()[3].weight * input);
    }
    public void computeWeightDerivatives() {
        // Compute the derivatives for the sin function w.r.t. each weight... use the cos(x) -> -sin(x) derivative rule and chain rule
        double[] numericalDerivatives = new double[this.getParameters().length];
        numericalDerivatives[0] = 1;
        numericalDerivatives[1] = Math.sin(this.getParameters()[2].weight + this.getParameters()[3].weight * this.input);
        numericalDerivatives[2] = this.getParameters()[1].weight * Math.cos(this.getParameters()[2].weight + this.getParameters()[3].weight * this.input);
        numericalDerivatives[3] = this.getParameters()[1].weight * this.input * Math.cos(this.getParameters()[2].weight + this.getParameters()[3].weight * this.input);
        this.updateParameterGradients(numericalDerivatives);
    }

    public String toString() {
        // Format: a + bsin(c + dx)
        return "" + this.getParameters()[0].weight + " + " + this.getParameters()[1].weight + "sin(" + this.getParameters()[2].weight + " + " + this.getParameters()[3].weight + "x)";
    }
    public int compareTo(Object T) { // A model ranks higher in search on the basis of a lower cost
        return this.cost < ((Model) T).cost ? 1 : -1;
    }
}
