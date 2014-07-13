package com.step.core;

import com.step.core.io.ExecutionResult;

/**
 * Created by ashish on 13-07-2014.
 */
public abstract class AbstractStep extends AbstractStepExecutionContextAware{
    protected ExecutionResult runPluginRequest(String req, Object in) throws Exception {
        return getStepExecutionContext().applyPluginRequest(req, in);
    }

    protected <I> I getInput(Class<I> clazz) {
        return getStepExecutionContext().getInput(clazz);
    }
}
