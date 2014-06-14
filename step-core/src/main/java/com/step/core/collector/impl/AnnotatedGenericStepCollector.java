package com.step.core.collector.impl;

import com.step.core.Configuration;
import com.step.core.collector.StepCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.apply.AnnotationDefinitionCollectorApplier;
import com.step.core.collector.apply.impl.ApplyGenericStepCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnnotatedGenericStepCollector implements StepCollector {
    @Override
    public List<StepDefinitionHolder> collect(Configuration conf) {
        List<StepDefinitionHolder> definitions = new ArrayList<StepDefinitionHolder>();
        String[] stepPackages = conf.getStepPackages();
        AnnotationDefinitionCollectorApplier applier = new ApplyGenericStepCollector();
        definitions.addAll(applier.apply(stepPackages));

        return definitions;
    }
}
