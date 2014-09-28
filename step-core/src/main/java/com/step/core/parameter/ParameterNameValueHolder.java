package com.step.core.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 9/28/14.
 */
public class ParameterNameValueHolder {
    private String name;
    private List<String> values = new ArrayList<String>();

    public ParameterNameValueHolder(String name, List<String> values){
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }
}
