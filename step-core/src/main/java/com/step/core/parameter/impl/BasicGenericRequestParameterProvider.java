package com.step.core.parameter.impl;

import com.step.core.parameter.GenericRequestParameterProvider;
import com.step.core.parameter.ParameterNameValueHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amishra on 9/28/14.
 */
public class BasicGenericRequestParameterProvider implements GenericRequestParameterProvider {
    private Map<String, List<ParameterNameValueHolder>> genericParamsMap = new HashMap<String, List<ParameterNameValueHolder>>();


    @Override
    public void addRequestParameterNameValue(String name, ParameterNameValueHolder parameterNameValueHolder) {
        List<ParameterNameValueHolder> params = genericParamsMap.get(name);

        if(params == null){
            params = new ArrayList<ParameterNameValueHolder>();
            genericParamsMap.put(name, params);
        }

        params.add(parameterNameValueHolder);
    }

    @Override
    public List<ParameterNameValueHolder> getRequestParameterNameValuePairs(String name) {
        return genericParamsMap.get(name);
    }
}
