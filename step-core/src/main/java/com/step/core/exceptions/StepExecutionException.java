package com.step.core.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/3/13
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepExecutionException extends RuntimeException{
    private Class<?> stepClass;
    private Throwable throwable;

    public StepExecutionException(Class<?> stepClass, Throwable throwable){
        super(throwable);
        this.stepClass = stepClass;
    }

    public String failedStep(){
        return this.stepClass.getName();
    }
}
