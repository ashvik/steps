package com.step.core.xml.model;

/**
 * Created by amishra on 12/23/14.
 */
public class Alias {
    private String name;
    private String request;

    public Alias(String name, String request){
        this.name = name;
        this.request = request;
    }

    public String getName() {
        return name;
    }

    public String getRequest() {
        return request;
    }
}
