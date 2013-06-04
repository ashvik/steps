package com.step.core.chain.impl;

import com.step.core.chain.StepChain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/26/13
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class BasicStepChain implements StepChain {
    private List<Class<?>> steps = new ArrayList<Class<?>>();
    private List<Class<?>> preSteps = new ArrayList<Class<?>>();
    private List<Class<?>> postSteps = new ArrayList<Class<?>>();

    @Override
    public List<Class<?>> getPreSteps() {
        return this.preSteps;
    }

    @Override
    public List<Class<?>> getPostStep() {
        return this.postSteps;
    }

    @Override
    public List<Class<?>> getSteps() {
        return this.steps;
    }

    @Override
    public void addStep(Class<?> stepClass) {
        if(!this.steps.contains(stepClass)){
            this.steps.add(stepClass);
        }
    }

    @Override
    public void addInterceptorStep(Class<?> stepClass, boolean isPreStep) {
        if(isPreStep){
            this.preSteps.add(stepClass);
        }else{
            this.postSteps.add(stepClass);
        }
    }
}
