package com.company;

/**
 * Created by rohankapur on 18/5/16.
 */
// This class defines a single parameter for the model that can be trained/fitted correctly.
public class Parameter {
    public double weight = 1; // The value of the weight/coefficient as a scalar
    public double gradient; // The tentative gradient of an arbitrary model output w.r.t. to the weight
}