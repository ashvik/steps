package com.step.core.container;

import com.step.core.Configuration;
import com.step.core.factory.ObjectFactory;
import com.step.core.io.ExecutionResult;
import com.step.core.io.StepInput;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/22/13
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepContainer {
    ExecutionResult submit(StepInput input);
    void setConfiguration(Configuration configuration);
    void setObjectFactory(ObjectFactory objectFactory);
    void init();
}
