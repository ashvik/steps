package com.step.core;

/**
 * Created by amishra on 10/14/14.
 */
public class PluginRequest {
    private String request;
    private boolean runAutomatically;

    public PluginRequest(String request, boolean runAutomatically){
        this.request = request;
        this.runAutomatically = runAutomatically;
    }

    public String getRequest() {
        return request;
    }

    public boolean isRunAutomatically() {
        return runAutomatically;
    }
}
