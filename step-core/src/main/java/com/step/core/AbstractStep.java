package com.step.core;

import com.step.core.exceptions.RequestParameterNotFoundException;
import com.step.core.io.ExecutionResult;
import com.step.core.parameter.RequestParameterContainer;
import com.step.core.parameter.RequestParameterValues;

import java.util.List;
import java.util.Set;

/**
 * Created by ashish on 13-07-2014.
 */
public abstract class AbstractStep extends AbstractStepExecutionContextAware{
    protected ExecutionResult runPluginRequest(String req, Object... in) throws Exception {
        return getStepExecutionContext().applyPluginRequest(req, in);
    }

    protected <I> I getInput(Class<I> clazz) {
        return getStepExecutionContext().getInput(clazz);
    }

    protected <T> List<T> getListTypeInput(Class<T> type){
        return getStepExecutionContext().getStepInput().getListTypeInput(type);
    }

    protected <T> Set<T> getSetTypeInput(Class<T> type){
        return getStepExecutionContext().getStepInput().getSetTypeInput(type);
    }

    protected int getParameterAsInt(String name){
        RequestParameterValues requestParameterValues = getRequestParameterValues(name);
        return requestParameterValues.getSingleValueAsInt();
    }

    protected String getParameterAsString(String name){
        RequestParameterValues requestParameterValues = getRequestParameterValues(name);
        return requestParameterValues.getSingleValueAsString();
    }

    protected Boolean getParameterAsBoolean(String name){
        RequestParameterValues requestParameterValues = getRequestParameterValues(name);
        return requestParameterValues.getSingleValueAsBoolean();
    }

    protected <T> List<T> getParameterAsObjects(String name){
        RequestParameterValues requestParameterValues = getRequestParameterValues(name);
        return requestParameterValues.getValueAsObjects();
    }

    protected <T> List<T> getParameterAsObjects(Class<T> cls){
        RequestParameterValues requestParameterValues = getRequestParameterValues(cls.getSimpleName());
        return requestParameterValues.getValueAsObjects();
    }

    protected <T> T getParameterAsSingleObject(String name){
        RequestParameterValues requestParameterValues = getRequestParameterValues(name);
        return (T)requestParameterValues.getValueAsObjects().get(0);
    }

    protected <T> T getParameterAsSingleObject(Class<T> cls){
        RequestParameterValues requestParameterValues = getRequestParameterValues(cls.getSimpleName());
        return (T)requestParameterValues.getValueAsObjects().get(0);
    }


    protected List<String> getParameters(String name){
        RequestParameterValues requestParameterValues = getRequestParameterValues(name);
        return requestParameterValues.getValues();
    }

    private RequestParameterValues getRequestParameterValues(String name){
        RequestParameterContainer requestParameterContainer = getStepExecutionContext().getRequestParameterContainer();
        if(requestParameterContainer != null){
            RequestParameterValues requestParameterValues = requestParameterContainer.getRequestParameterValues(name);

            if(requestParameterValues != null){
                return requestParameterValues;
            }
        }

        throw new RequestParameterNotFoundException(
                String.format("Parameter %s is not configured for current request.", name));
    }
}
