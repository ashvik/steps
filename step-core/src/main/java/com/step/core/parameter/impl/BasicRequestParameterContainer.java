package com.step.core.parameter.impl;

import com.step.core.parameter.RequestParameterContainer;
import com.step.core.parameter.RequestParameterValues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amishra on 9/27/14.
 */
public class BasicRequestParameterContainer implements RequestParameterContainer {
   private Map<String, RequestParameterValues> parameterValuesMap = new HashMap<String, RequestParameterValues>();

    @Override
    public void addRequestParameter(String name, List<String> values) {
        RequestParameterValues parameterValues = new RequestParameterValues();

        for(String value : values){
            parameterValues.addValue(value);
        }

        parameterValuesMap.put(name, parameterValues);
    }

    @Override
    public RequestParameterValues getRequestParameterValues(String name) {
        return parameterValuesMap.get(name);
    }
}
