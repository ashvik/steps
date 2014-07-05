package com.step.core.example.steps;

import com.step.core.conditions.abstr.AbstractBreakCondition;

import java.util.Map;

/**
 * Created by amishra on 6/14/14.
 */
public class TestBreakConditionNew extends AbstractBreakCondition {
    @Override
    public boolean check() {
        System.out.println("BREAKNEW");
        return getStepExecutionContext().getInput(Map.class).get("breakNew")!=null;
    }
}
