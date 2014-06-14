package com.step.core.collector.apply.impl;

import com.step.core.annotations.collector.ChainBreakerAnnotationDefinitionCollector;
import com.step.core.chain.breaker.BreakDetails;
import com.step.core.collector.StepDefinitionHolder;
import com.step.core.collector.apply.AnnotationDefinitionCollectorApplier;
import com.step.core.utils.AnnotatedDefinition;
import com.step.core.utils.AnnotatedDefinitionCollector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class ApplyChainBreakerCollector implements AnnotationDefinitionCollectorApplier{
    @Override
    public List<StepDefinitionHolder> apply(String[] stepPkgs) {
        Set<AnnotatedDefinition> annoDefinitions = new HashSet<AnnotatedDefinition>();
        List<StepDefinitionHolder> defs = new ArrayList<StepDefinitionHolder>();
        for(String pkg : stepPkgs){
            AnnotatedDefinitionCollector collector = new ChainBreakerAnnotationDefinitionCollector();
            annoDefinitions.addAll(collector.collect(pkg));
        }

        for(AnnotatedDefinition ad : annoDefinitions){
            String name = (String)ad.getDefinition("name");
            String request = (String)ad.getDefinition("request");
            String condition = (String)ad.getDefinition("conditionClass");
            BreakDetails details = new BreakDetails();

            try {
                details.setConditionClass(Class.forName(condition));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            StepDefinitionHolder holder = new StepDefinitionHolder(name, ad.getAnnotatedClass());
            holder.addBreakDetails(request, details);

            defs.add(holder);
        }

        return defs;
    }
}
