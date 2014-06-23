package com.step.informar.flat;

import com.step.informar.flat.visitor.FlatInfoVisitor;

/**
 * Created by amishra on 6/21/14.
 */
public class RepeatInfo implements FlatInfo{
    private String startRepeatFrom, repeatUpTo;
    private ConditionInfo condition;

    public String getStartRepeatFrom() {
        return startRepeatFrom;
    }

    public void setStartRepeatFrom(String startRepeatFrom) {
        this.startRepeatFrom = startRepeatFrom;
    }

    public String getRepeatUpTo() {
        return repeatUpTo;
    }

    public void setRepeatUpTo(String repeatUpTo) {
        this.repeatUpTo = repeatUpTo;
    }

    public ConditionInfo getCondition() {
        return condition;
    }

    public void setCondition(ConditionInfo condition) {
        this.condition = condition;
    }

    @Override
    public void accept(FlatInfoVisitor visitor) {
        visitor.visitRepeatInfo(this);
    }
}
