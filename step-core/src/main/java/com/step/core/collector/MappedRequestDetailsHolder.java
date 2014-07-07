package com.step.core.collector;

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
    private boolean canApplyGenericSteps = true;
    private List<String> preSteps = new ArrayList<String>();
    private List<String> postSteps = new ArrayList<String>();
    private Map<String, List<String>> pluginRequests = new HashMap<String, List<String>>();

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

    public List<String> getPluginsForRequest(String request) {
        return pluginRequests.get(request);
    }

    public void addPluginRequests(String mappedRequest, List<String> pluginRequests) {
        this.pluginRequests.put(mappedRequest, pluginRequests);
    }

    public void merge(MappedRequestDetailsHolder other){
        this.mappedRequest = other.mappedRequest == null ? this.mappedRequest : other.mappedRequest ;
        this.canApplyGenericSteps = !this.canApplyGenericSteps ? this.canApplyGenericSteps : other.canApplyGenericSteps;
        this.preSteps = other.preSteps.isEmpty() ? this.preSteps : other.preSteps;
        this.postSteps = other.postSteps.isEmpty() ? this.postSteps : other.postSteps;
        this.pluginRequests.putAll(other.pluginRequests);
    }

    public MappedRequestDetailsHolder cloneWithDifferentMappedRequest(String mappedRequest){
        MappedRequestDetailsHolder cloned = new MappedRequestDetailsHolder();
        cloned.mappedRequest = mappedRequest;
        cloned.canApplyGenericSteps = this.canApplyGenericSteps;
        cloned.preSteps = this.preSteps;
        cloned.postSteps = this.postSteps;
        cloned.pluginRequests = this.pluginRequests;

        return cloned;
    }
}
