package com.step.test.base.step;

import com.step.core.parameter.RequestParameterContainer;
import com.step.core.parameter.impl.BasicRequestParameterContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amishra on 11/21/14.
 */
public class StepPrerequisitesHolder {
    private Map<String, Object> pluginRequestsMap = new HashMap<String, Object>();
    private Map<String, List<String>> requestParameterMap = new HashMap<String, List<String>>();

    public StepPrerequisitesHolder addPluginRequest(String plugin, Object result){
        this.pluginRequestsMap.put(plugin, result);
        return this;
    }

    public StepPrerequisitesHolder addRequestParameter(String paramName, String paramValue){
        List<String> params = requestParameterMap.get(paramName);
        if(params == null){
            params = new ArrayList<String>();
            requestParameterMap.put(paramName, params);
        }
        params.add(paramValue);
        return this;
    }

    public Map<String, Object> getPluginRequests(){
        return this.pluginRequestsMap;
    }

    public RequestParameterContainer getRequestParameterContainer(){
        RequestParameterContainer requestParameterContainer = new BasicRequestParameterContainer();
        for(String paramName : requestParameterMap.keySet()){
            requestParameterContainer.addRequestParameter(paramName, requestParameterMap.get(paramName));
        }

        return requestParameterContainer;
    }
}
