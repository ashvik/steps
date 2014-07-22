package com.step.core.collector.impl;

import com.step.core.Configuration;
import com.step.core.collector.StepCollector;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.apply.AnnotationDefinitionCollectorApplier;
import com.step.core.collector.apply.impl.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/27/13
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class AnnotatedStepCollector implements StepCollector{
    private AnnotationDefinitionCollectorApplier applyStepDefinitionCollector = new ApplyStepDefinitionCollector();

    private AnnotationDefinitionCollectorApplier applyStepJumperCollector = new ApplyStepJumperCollector();

    private AnnotationDefinitionCollectorApplier applyChainBreakerCollector = new ApplyChainBreakerCollector();

    private AnnotationDefinitionCollectorApplier applyRepeaterCollector = new ApplyStepRepeaterCollector();

    private AnnotationDefinitionCollectorApplier applyMultiScopedCollector = new ApplyMultiScopedCollector();

    @Override
    public List<StepDefinitionHolder> collect(Configuration conf) {
        List<StepDefinitionHolder> definitions = new ArrayList<StepDefinitionHolder>();
        String[] stepPackages = conf.getStepPackages();
        definitions.addAll(applyStepDefinitionCollector.apply(stepPackages));
        definitions.addAll(applyStepJumperCollector.apply(stepPackages));
        definitions.addAll(applyChainBreakerCollector.apply(stepPackages));
        definitions.addAll(applyRepeaterCollector.apply(stepPackages));
        definitions.addAll(applyMultiScopedCollector.apply(stepPackages));

        return definitions;
    }
}
