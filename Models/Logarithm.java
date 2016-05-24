package com.company;

/**
 * Created by rohankapur on 21/5/16.
 */
// This class defines a specialized logarithm model.
public class Logarithm extends Model implements Comparable {
    public Logarithm() {
        super(Constants.numParams.get(Constants.ModelType.ModelTypeLogarithm).intValue());
        this.modelType = Constants.ModelType.ModelTypeLogarithm;
    }


    public double compute(double input) {
        // Format: a + bln(c + dx)
        return this.getParameters()[0].weight + this.getParameters()[1].weight * Math.log(this.getParameters()[2].weight + this.getParameters()[3].weight * input);
    }
    public void computeWeightDerivatives() {
        // When computing derivatives w.r.t. weights, use ln(x) -> 1/x derivative rule and chain rule
        double[] numericalDerivatives = new double[this.getParameters().length];
        numericalDerivatives[0] = 1;
        numericalDerivatives[1] = Math.log(this.getParameters()[2].weight + this.getParameters()[3].weight * this.input);
        double derivDivide = (this.getParameters()[2].weight + this.getParameters()[3].weight * this.input);
        numericalDerivatives[2] = this.getParameters()[1].weight / derivDivide;
        numericalDerivatives[3] = (this.getParameters()[1].weight * this.input) / derivDivide;
        this.updateParameterGradients(numericalDerivatives);
    }

    public String toString() {
        // Output: a + bln(c + dx)
        return "" + this.getParameters()[0].weight + " + " + this.getParameters()[1].weight + "ln(" + this.getParameters()[2].weight + " + " + this.getParameters()[3].weight + "x)";
    }
    public int compareTo(Object T) { // A model ranks higher in search on the basis of a lower cost
        return this.cost < ((Model) T).cost ? 1 : -1;
    }
}