package com.step.core;

import com.step.core.factory.ObjectFactory;

/**
 * Created by amishra on 11/25/14.
 */
public class InitializeStepFramework {
    private Configuration configuration;

    private ObjectFactory objectFactory;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public void initialize(){
        StepExecutionContainerProvider.INSTANCE.init(configuration, objectFactory);
    }
}
