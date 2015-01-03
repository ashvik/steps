package com.step.core.interceptor.event;

/**
 * Created by amishra on 1/2/15.
 */
public interface PluginEvent<T> extends StepEvent{
    void runPlugins();
    T getPluginDetails();
}
