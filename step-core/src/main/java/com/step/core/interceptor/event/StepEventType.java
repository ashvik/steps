package com.step.core.interceptor.event;

/**
 * Created by amishra on 1/2/15.
 */
public enum StepEventType {
    PRE_EVENT("PRE"), POST_EVENT("POST");

    private String code;

    StepEventType(String code){
        this.code = code;
    }

    public static StepEventType getEventTypeFromCode(String code){
        for(StepEventType stepEventType : values()){
            if(stepEventType.code.equals(code)){
                return stepEventType;
            }
        }

        return null;
    }
}
