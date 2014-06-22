package com.step.core.provider;

import com.step.core.chain.StepChain;

/**
 * Created by amishra on 6/20/14.
 */
public interface StepExecutionChainProvider {
    StepChain provideStepChainForRequest(String request);
}
