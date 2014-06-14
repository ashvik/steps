package com.step.core.example.steps;

import com.step.core.conditions.JumpCondition;
import com.step.core.context.StepContext;

import java.util.Map;

/**
 * Created by amishra on 6/13/14.
 */
public class TestJumpCondition implements JumpCondition{
    StepContext context;

    @Override
    public boolean check() {
        Map map = getStepContext().getInput(Map.class);
        return map.get("test") != null;
    }

    @Override
    public void setStepContext(StepContext context) {
        this.context = context;
    }

    @Override
    public StepContext getStepContext() {
        return this.context;
    }
}
