package com.step.core.io;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/21/13
 * Time: 8:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionResult<T> {
    private T resultObject;

    public ExecutionResult(T obj){
        this.resultObject = obj;
    }

    public T getResultObject(){
        return this.resultObject;
    }
}
