package com.company;

/**
 * Created by rohankapur on 20/5/16.
 */
// This class is a specialized polynomial function model of model type Polynomial.
public class Polynomial extends Model implements Comparable {
    public int degree; // The degree of the polynomial - the highest order of x we use

    // Initialize a polynomial with a given degree
    public Polynomial(int _degree) {
        super(_degree + 1); // We add one because we have a constant term
        this.degree = _degree;
        this.modelType = Constants.ModelType.ModelTypePolynomial;
    }

    public double compute(double input) {
        // Our polynomial is a sum of terms of coefficient * an order of x
        double result = 0;
        for (int i = 0 ; i <= degree; i++) // Iterate through each order of x, multiply by respective weight, add
            result += (this.getParameters()[i].weight * Math.pow(input, i)); // We add on a term of the weight at i multiplied by input to the power of i
        return result;
    }
    public void computeWeightDerivatives() {
        // Derivatives of each weight at i is just the input to the order i - linear combination
        double[] numericalDerivatives = new double[this.getParameters().length];
        for (int i = 0; i < this.getParameters().length; i++)
            numericalDerivatives[i] = Math.pow(this.input, i);
        this.updateParameterGradients(numericalDerivatives); // Update the tentative output w.r.t. weight gradients in the parameters
    }

    public String toString() {
        // Format: a + bx + cx^2 + ...  + zx^n
        String toString = "";
        for (int i = 0 ; i <= degree; i++) {
            toString += ("" + this.getParameters()[i].weight);
            if (i > 1)
                toString += ("x^" + i);
            else if (i == 1)
                toString += "x";
            toString += (i == degree ? "" : " + ");
        }
        return toString;
    }
    public int compareTo(Object T) { // A model ranks higher in search on the basis of a lower cost
        return this.cost < ((Model) T).cost ? 1 : -1;
    }
}
