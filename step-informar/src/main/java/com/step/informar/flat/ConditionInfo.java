package com.step.informar.flat;

/**
 * Created by amishra on 6/21/14.
 */
public class ConditionInfo{
    private String condition;
    private String conditionDescription;

    public ConditionInfo(){}

    public ConditionInfo(String condition){
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
    }

    /*public ConditionInfo(String condition, String conditionDescription){
        this.condition = condition;
        this.conditionDescription = conditionDescription;
    }

    public String getCondition() {
        return condition;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }*/
}
