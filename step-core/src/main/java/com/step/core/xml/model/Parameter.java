package com.step.core.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 9/27/14.
 */
public class Parameter {
    private String name;
    private List<String> values = new ArrayList<String>();

    public Parameter(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addValue(String value){
        this.values.add(value);
    }

    public List<String> getValues(){
        return this.values;
    }
}
