package com.step.core.interceptor.event;

/**
 * Created by amishra on 1/2/15.
 */
public interface ExecutionDecisionEvent<T> extends StepEvent {
    boolean runEvent(ClassLoader classLoader);
    T getDecisionDetails();
}
