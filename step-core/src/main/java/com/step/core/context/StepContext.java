package com.step.core.context;

import com.step.core.Attributes;
import com.step.core.factory.ObjectFactory;
import com.step.core.io.StepInput;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/18/13
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepContext {
    void put(String name, Object obj);
    Object get(String name);
    void setStepInput(StepInput input);
    StepInput getStepInput();
    <T> T getInput(Class<T> inputClass);
    <T> T getDependency(Class<T> dependency);
    Object getDependency(String dependency);
    void setObjectFactory(ObjectFactory factory);
    Object getAttribute(String name);
    Attributes getAttributes();
    void putAttribute(String name, Object value);
}
