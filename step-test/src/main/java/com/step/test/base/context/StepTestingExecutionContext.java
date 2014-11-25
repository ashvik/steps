package com.step.test.base.context;

import com.step.core.context.impl.BasicStepExecutionContext;
import com.step.core.io.ExecutionResult;

import java.util.Map;

/**
 * Created by amishra on 11/21/14.
 */
public class StepTestingExecutionContext extends BasicStepExecutionContext {
    private Map<String, Object> pluginRequestResults;

    @Override
    public ExecutionResult applyPluginRequest(String request, Object... input) throws Exception{
        Object result = pluginRequestResults.get(request);
        ExecutionResult executionResult = new ExecutionResult(result, null);
        return executionResult;
    }

    @Override
    public ExecutionResult applyPluginRequest(String request, boolean allowInputTransfer, boolean applyGenericSteps, Object... input) throws Exception{
        return applyPluginRequest(request);
    }

    public void setPluginRequest(Map<String, Object> pluginRequestResults){
        this.pluginRequestResults = pluginRequestResults;
    }
}
