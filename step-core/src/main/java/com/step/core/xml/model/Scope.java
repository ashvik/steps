package com.step.core.xml.model;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/30/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Scope {
    private String request;
    private String nextStep;

    public Scope(String request, String nextStep){
        this.request = request;
        this.nextStep = nextStep;
    }

    public String getRequest() {
        return request;
    }

    public String getNextStep() {
        return nextStep;
    }
}
