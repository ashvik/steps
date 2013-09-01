package com.step.core.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/24/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapRequest {
    private String request;
    private String rootStep;
    private boolean applyGenericSteps = true;
    private String onSuccess;
    private String onFailure;
    private List<String> preSteps = new ArrayList<String>();
    private List<String> postSteps = new ArrayList<String>();

    public void addPreSteps(String step){
        this.preSteps.add(step);
    }

    public List<String> getPreSteps(){
        return this.preSteps;
    }

    public void addPostSteps(String step){
        this.postSteps.add(step);
    }

    public List<String> getPostSteps(){
        return this.postSteps;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRootStep() {
        return rootStep;
    }

    public void setRootStep(String rootStep) {
        this.rootStep = rootStep;
    }

    public boolean isApplyGenericSteps() {
        return applyGenericSteps;
    }

    public void setApplyGenericSteps(boolean applyGenericSteps) {
        this.applyGenericSteps = applyGenericSteps;
    }

    public String getOnSuccess() {
        return onSuccess;
    }

    public void setOnSuccess(String onSuccess) {
        this.onSuccess = onSuccess;
    }

    public String getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(String onFailure) {
        this.onFailure = onFailure;
    }
}
