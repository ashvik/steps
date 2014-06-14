package com.step.core.conditions;

import com.step.core.context.StepExecutionContextAware;

/**
 * Created by amishra on 6/14/14.
 */
public interface BreakCondition extends StepExecutionContextAware {
    boolean check();
}
