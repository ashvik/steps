package com.step.core.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 10/15/14.
 */
public class Plugins {
    private String step;
    private List<String> plugins = new ArrayList<String>();
    private String type;

    public Plugins(String step){
        this.step = step;
    }

    public String getStep(){
        return step;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public void addPlugin(String plugIn){
        this.plugins.add(plugIn);
    }

    public void setType(String type){
        this.type = type == null || type.isEmpty() ? "PRE" : type;
    }

    public String getType(){
        return this.type;
    }
}
