package com.step.core.conditions;

import com.step.core.context.StepContextAware;

/**
 * Created by amishra on 6/13/14.
 */
public interface JumpCondition extends StepContextAware{
    boolean check();
}
