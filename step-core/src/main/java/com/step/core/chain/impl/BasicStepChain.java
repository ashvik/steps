package com.step.core.chain.impl;

import com.step.core.chain.StepChain;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.utils.AnnotatedField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Class<?>, List<AnnotatedField>> dependenciesMap =
            new HashMap<Class<?>, List<AnnotatedField>>();

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
    public List<AnnotatedField> getDependenciesForStep(Class<?> stepClass) {
        List<AnnotatedField> fields = this.dependenciesMap.get(stepClass);
        return fields == null ? Collections.EMPTY_LIST : fields;
    }

    @Override
    public void addStep(StepDefinitionHolder holder) {
        if(!this.steps.contains(holder.getStepClass())){
            this.steps.add(holder.getStepClass());
            this.dependenciesMap.put(holder.getStepClass(), holder.getAnnotatedFields());
        }
    }

    @Override
    public void addInterceptorStep(StepDefinitionHolder stepDefinitionHolder, boolean isPreStep) {
        if(isPreStep){
            this.preSteps.add(stepDefinitionHolder.getStepClass());
        }else{
            this.postSteps.add(stepDefinitionHolder.getStepClass());
        }

        this.dependenciesMap.put(stepDefinitionHolder.getStepClass(), stepDefinitionHolder.getAnnotatedFields());
    }
}
