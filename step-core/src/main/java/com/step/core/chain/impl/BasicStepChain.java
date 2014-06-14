package com.step.core.chain.impl;

import com.step.core.chain.StepChain;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.chain.jump.JumpDetails;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.utils.AnnotatedField;

import java.util.*;

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
    private Map<Class<?>, Class<?>> stepToJumpConditionMap = new HashMap<Class<?>, Class<?>>();
    private Map<Class<?>, Class<?>> stepToBreakConditionMap = new HashMap<Class<?>, Class<?>>();
    private Map<Class<?>, String> stepNames = new HashMap<Class<?>, String>();
    private Map<Class<?>, JumpDetails> stepJumpInfoMap = new HashMap<Class<?>, JumpDetails>();
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
    public void addStep(StepDefinitionHolder holder, String request) {
        if(!this.steps.contains(holder.getStepClass())){
            this.steps.add(holder.getStepClass());
            this.dependenciesMap.put(holder.getStepClass(), holder.getAnnotatedFields());
            this.stepNames.put(holder.getStepClass(), holder.getName());
            JumpDetails details = holder.getJumpDetails(request);
            if(details != null){
                this.stepToJumpConditionMap.put(holder.getStepClass(), details.getConditionClass());
                this.stepJumpInfoMap.put(holder.getStepClass(), details);
            }
            BreakDetails breakDetails = holder.getBreakDetails(request);
            if(breakDetails != null){
                this.stepToBreakConditionMap.put(holder.getStepClass(), breakDetails.getConditionClass());
            }
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

    @Override
    public String getStepName(Class<?> stepClass) {
        return stepNames.get(stepClass);
    }

    @Override
    public Class<?> getJumpConditionClassForStep(Class<?> step) {
        return this.stepToJumpConditionMap.get(step);
    }

    @Override
    public JumpDetails getJumpDetailsForStep(Class<?> step) {
        return stepJumpInfoMap.get(step);
    }

    @Override
    public Class<?> getBreakConditionClassForStep(Class<?> step) {
        return this.stepToBreakConditionMap.get(step);
    }
}
