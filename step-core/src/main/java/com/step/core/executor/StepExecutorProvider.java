package com.step.core.executor;

import com.step.core.Configuration;
import com.step.core.chain.StepChain;
import com.step.core.context.StepContext;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/29/13
 * Time: 11:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepExecutorProvider {
    StepExecutor provide(StepChain chain, StepContext context);
    void initInterceptors(Configuration conf);
}
