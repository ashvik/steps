package com.step.core.example.steps;

import com.step.core.conditions.abstr.AbstractRepeatBreakCondition;

import java.util.List;

/**
 * Created by amishra on 6/14/14.
 */
public class TestRepeatBreakCondition extends AbstractRepeatBreakCondition{
    //private int count;

    @Override
    public boolean check() {
        List<Integer> list = getStepExecutionContext().getInput(List.class);
        list.remove(0);

        if(list.isEmpty()){
            return true;
        }
        //count++;
        return false;
    }
}
