package com.step.core.conditions;

import com.step.core.context.StepExecutionContextAware;

/**
 * Created by amishra on 6/13/14.
 */
public interface JumpCondition extends StepExecutionContextAware {
    boolean check();
}
