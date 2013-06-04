package com.step.core.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/21/13
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepInput {
    private String request;
    private List<Object> collectedInputs = new ArrayList<Object>();
    private List<Class<?>> collectedInputClass = new ArrayList<Class<?>>();

    public StepInput(String request, Object input){
        this.collectedInputs.add(input);
        this.request = request;
        this.collectedInputClass.add(input.getClass());
    }

    public StepInput(String request){
        this.request = request;
    }

    public StepInput(Object input){
        this.collectedInputs.add(input);
    }

    public void setInput(Object input){
        if(!this.collectedInputClass.contains(input.getClass())){
            this.collectedInputs.add(input);
            this.collectedInputClass.add(input.getClass());
        }
    }

    public <T> T getInput(Class<T> inputType){
        for(Object input : collectedInputs){
            if(inputType.isAssignableFrom(input.getClass())){
                return (T)input;
            }
        }

        throw new IllegalStateException("No input found for type: "+inputType.getName());
    }

    public String getRequest(){
        return this.request;
    }
}
