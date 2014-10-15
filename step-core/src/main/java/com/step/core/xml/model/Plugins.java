package com.step.core.xml.model;

/**
 * Created by amishra on 10/15/14.
 */
public class Plugins {
    private String plugin;
    private boolean applyAutomatically;

    public Plugins(String plugin){
        this.plugin = plugin;
    }

    public String getPlugin() {
        return plugin;
    }

    public boolean isApplyAutomatically() {
        return applyAutomatically;
    }

    public void setApplyAutomatically(boolean applyAutomatically) {
        this.applyAutomatically = applyAutomatically;
    }
}
