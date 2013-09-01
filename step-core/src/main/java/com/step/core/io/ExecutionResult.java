package com.step.core.io;

import com.step.core.Attributes;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/21/13
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionResult<T> {
    private T resultObject;
    private Attributes attributes;

    public ExecutionResult(T obj, Attributes attributes){
        this.resultObject = obj;
        this.attributes = attributes;
    }

    public T getResultObject(){
        return this.resultObject;
    }

    public Attributes getAttributes() {
        return attributes;
    }
}
