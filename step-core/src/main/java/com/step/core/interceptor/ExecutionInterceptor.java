package com.step.core.interceptor;

import com.step.core.chain.StepChain;
import com.step.core.context.StepExecutionContext;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/29/13
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExecutionInterceptor {
    void intercept(StepChain chain, StepExecutionContext context);
}
