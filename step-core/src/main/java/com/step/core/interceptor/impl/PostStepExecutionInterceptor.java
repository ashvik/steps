package com.step.core.interceptor.impl;

import com.step.core.chain.StepChain;
import com.step.core.context.StepExecutionContext;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/30/13
 * Time: 12:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class PostStepExecutionInterceptor extends AbstractStepExecutionInterceptor {
    @Override
    public void intercept(StepChain chain, StepExecutionContext context) {
        executeInterceptorSteps(chain.getPostStep(), chain, context);
    }
}
