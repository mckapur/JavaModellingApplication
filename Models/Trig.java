package com.company;

/**
 * Created by rohankapur on 22/5/16.
 */
// This class is an arbitrary function model of model type Trig. Specific trig functions need to subclass.
public abstract class Trig extends Model {
    public Trig() {
        super(Constants.numParams.get(Constants.ModelType.ModelTypeTrig).intValue());
        this.modelType = Constants.ModelType.ModelTypeTrig;
    }
}
