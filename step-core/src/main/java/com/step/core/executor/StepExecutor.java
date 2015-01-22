package com.step.core.executor;

import com.step.core.context.StepExecutionContext;
import com.step.core.io.ExecutionResult;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/18/13
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepExecutor {
    ExecutionResult execute(StepExecutionContext context) throws Exception;
}
