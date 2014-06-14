package com.step.core.context.impl;

import com.step.core.Attributes;
import com.step.core.context.StepExecutionContext;
import com.step.core.factory.ObjectFactory;
import com.step.core.io.StepInput;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/25/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicStepExecutionContext implements StepExecutionContext {
    private Map<String, Object> cache = new HashMap<String, Object>();
    private StepInput input;
    private ObjectFactory objectFactory;
    private Attributes attributes = new Attributes();
    private boolean breakStepChain;

    @Override
    public void put(String name, Object obj) {
        this.cache.put(name, obj);
    }

    @Override
    public Object get(String name) {
        return this.cache.get(name);
    }

    @Override
    public void setStepInput(StepInput input) {
       this.input = input;
    }

    @Override
    public StepInput getStepInput() {
        return this.input;
    }

    public <T> T getInput(Class<T> inputClass){
        return (T)this.input.getInput(inputClass);
    }

    @Override
    public <T> T getDependency(Class<T> dependency) {
        return this.objectFactory.fetch(dependency);
    }

    @Override
    public Object getDependency(String dependency) {
        return this.objectFactory.fetch(dependency);
    }

    @Override
    public void setObjectFactory(ObjectFactory factory) {
        this.objectFactory = factory;
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.getAttribute(name);
    }

    @Override
    public Attributes getAttributes() {
        return this.attributes;
    }

    @Override
    public void putAttribute(String name, Object value) {
        this.attributes.addAttribute(name, value);
    }

    @Override
    public void breakStepChainExecution() {
        breakStepChain = true;
    }

    @Override
    public boolean hasStepChainExecutionBroken() {
        return breakStepChain;
    }
}
