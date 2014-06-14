package com.step.core.collector.apply;

import com.step.core.collector.StepDefinitionHolder;

import java.util.List;

/**
 * Created by amishra on 6/14/14.
 */
public interface AnnotationDefinitionCollectorApplier {
    List<StepDefinitionHolder> apply(String[] stepPkgs);
}
