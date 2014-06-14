package com.step.core.conditions;

import com.step.core.context.StepContextAware;

/**
 * Created by amishra on 6/14/14.
 */
public interface BreakCondition extends StepContextAware{
    boolean check();
}
