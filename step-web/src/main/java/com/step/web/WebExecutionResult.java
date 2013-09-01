package com.step.web;

import com.step.core.Attributes;
import com.step.core.io.ExecutionResult;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/23/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebExecutionResult extends ExecutionResult {
    private String resource;

    public WebExecutionResult(Object input, String resource, Attributes attributes){
        super(input, attributes);
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
