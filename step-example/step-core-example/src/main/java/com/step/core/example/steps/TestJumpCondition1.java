package com.step.core.example.steps;

import com.step.core.conditions.JumpCondition;
import com.step.core.context.StepExecutionContext;

import java.util.Map;

/**
 * Created by amishra on 7/5/14.
 */
public class TestJumpCondition1 implements JumpCondition {
    StepExecutionContext context;

    @Override
    public boolean check() {
        Map map = getStepExecutionContext().getInput(Map.class);
        return map.get("test1") != null;
    }

    @Override
    public void setStepExecutionContext(StepExecutionContext context) {
        this.context = context;
    }

    @Override
    public StepExecutionContext getStepExecutionContext() {
        return this.context;
    }
}
