package com.step.core.example.steps;

import com.step.core.conditions.abstr.AbstractBreakCondition;

import java.util.Map;

/**
 * Created by amishra on 6/14/14.
 */
public class TestBreakCondition extends AbstractBreakCondition{
    @Override
    public boolean check() {
        return getStepContext().getInput(Map.class).get("break")!=null;
    }
}
