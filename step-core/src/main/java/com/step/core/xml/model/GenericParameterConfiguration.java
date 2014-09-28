package com.step.core.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 9/28/14.
 */
public class GenericParameterConfiguration {
    private String name;
    private List<Parameter> parameters = new ArrayList<Parameter>();

    public GenericParameterConfiguration(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addParameter(Parameter parameter){
        this.parameters.add(parameter);
    }

    public List<Parameter> getParameters(){
        return parameters;
    }
}
