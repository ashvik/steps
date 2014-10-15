package com.step.core.collector;

import com.step.core.PluginRequest;
import com.step.core.parameter.RequestParameterContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amishra on 7/4/14.
 */
public class MappedRequestDetailsHolder {
    private String mappedRequest;
    private String onSuccess;
    private String onFailure;
    private String expectedOutCome;
    private String stepExceptionHandler;
    private boolean canApplyGenericSteps = true;
    private List<String> preSteps = new ArrayList<String>();
    private List<String> postSteps = new ArrayList<String>();
    private RequestParameterContainer requestParameterContainer;
    private List<String> genericParameters = new ArrayList<String>();
    private List<String> inputTypes = new ArrayList<String>();
    private Map<String, List<PluginRequest>> pluginRequest = new HashMap<String, List<PluginRequest>>();

    public List<String> getPostSteps() {
        return postSteps;
    }

    public void setPostSteps(List<String> postSteps) {
        this.postSteps = postSteps;
    }

    public List<String> getPreSteps() {
        return preSteps;
    }

    public void setPreSteps(List<String> preSteps) {
        this.preSteps = preSteps;
    }

    public boolean isCanApplyGenericSteps() {
        return canApplyGenericSteps;
    }

    public void setCanApplyGenericSteps(boolean canApplyGenericSteps) {
        this.canApplyGenericSteps = canApplyGenericSteps;
    }

    public String getMappedRequest() {
        return mappedRequest;
    }

    public void setMappedRequest(String mappedRequest) {
        this.mappedRequest = mappedRequest;
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

    public List<PluginRequest> getPluginsForRequest(String request) {
        return pluginRequest.get(request);
    }

    public void addPluginRequests(String mappedRequest, List<PluginRequest> pluginRequests) {
        this.pluginRequest.put(mappedRequest, pluginRequests);
    }

    public RequestParameterContainer getRequestParameterContainer() {
        return requestParameterContainer;
    }

    public void setRequestParameterContainer(RequestParameterContainer requestParameterContainer) {
        this.requestParameterContainer = requestParameterContainer;
    }

    public void addGenericParameter(List<String> param){
        this.genericParameters = param;
    }

    public List<String> getGenericParameters(){
        return this.genericParameters;
    }

    public String getExpectedOutCome() {
        return expectedOutCome;
    }

    public void setExpectedOutCome(String expectedOutCome) {
        this.expectedOutCome = expectedOutCome;
    }

    public String getStepExceptionHandler() {
        return stepExceptionHandler;
    }

    public void setStepExceptionHandler(String stepExceptionHandler) {
        this.stepExceptionHandler = stepExceptionHandler;
    }

    public List<String> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(List<String> inputTypes) {
        this.inputTypes = inputTypes;
    }

    public void merge(MappedRequestDetailsHolder other){
        this.mappedRequest = other.mappedRequest == null ? this.mappedRequest : other.mappedRequest ;
        this.canApplyGenericSteps = !this.canApplyGenericSteps ? this.canApplyGenericSteps : other.canApplyGenericSteps;
        this.preSteps = other.preSteps.isEmpty() ? this.preSteps : other.preSteps;
        this.postSteps = other.postSteps.isEmpty() ? this.postSteps : other.postSteps;
        this.genericParameters = other.genericParameters.isEmpty() ? this.genericParameters : other.genericParameters;
        this.inputTypes = other.inputTypes.isEmpty() ? this.inputTypes : other.inputTypes;
        this.pluginRequest.putAll(other.pluginRequest);
        this.requestParameterContainer = other.requestParameterContainer == null ? this.requestParameterContainer : other.requestParameterContainer;
        this.expectedOutCome = other.expectedOutCome == null ? this.expectedOutCome : other.expectedOutCome;
        this.stepExceptionHandler = other.stepExceptionHandler == null ? this.stepExceptionHandler : other.stepExceptionHandler;
    }

    public MappedRequestDetailsHolder cloneWithDifferentMappedRequest(String mappedRequest){
        MappedRequestDetailsHolder cloned = new MappedRequestDetailsHolder();
        cloned.mappedRequest = mappedRequest;
        cloned.canApplyGenericSteps = this.canApplyGenericSteps;
        cloned.preSteps = this.preSteps;
        cloned.postSteps = this.postSteps;
        cloned.genericParameters = this.genericParameters;
        cloned.inputTypes = this.inputTypes;
        cloned.pluginRequest = this.pluginRequest;
        cloned.requestParameterContainer = this.requestParameterContainer;
        cloned.expectedOutCome = this.expectedOutCome;
        cloned.stepExceptionHandler = this.stepExceptionHandler;

        return cloned;
    }
}
