package com.step.core.utils;

import com.step.core.collector.StepDefinitionHolder;
import com.step.core.context.StepExecutionContext;

/**
 * Created by amishra on 1/9/15.
 */
public interface StepEnricher {
    void enrichStep(Object step, StepDefinitionHolder definitionHolder, StepExecutionContext stepExecutionContext);
}
