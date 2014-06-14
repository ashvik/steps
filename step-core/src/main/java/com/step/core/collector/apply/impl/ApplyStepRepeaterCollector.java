package com.step.core.collector.apply.impl;

import com.step.core.annotations.builder.impl.StepRepeaterAnnotationDefinitionBuilder;
import com.step.core.annotations.collector.BasicAnnotationDefinitionCollector;
import com.step.core.chain.repeater.RepeatDetails;
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
public class ApplyStepRepeaterCollector implements AnnotationDefinitionCollectorApplier {
    @Override
    public List<StepDefinitionHolder> apply(String[] stepPkgs) {
        Set<AnnotatedDefinition> annoDefinitions = new HashSet<AnnotatedDefinition>();
        List<StepDefinitionHolder> defs = new ArrayList<StepDefinitionHolder>();
        for(String pkg : stepPkgs){
            AnnotatedDefinitionCollector collector = new BasicAnnotationDefinitionCollector();
            annoDefinitions.addAll(collector.collect(pkg, new StepRepeaterAnnotationDefinitionBuilder()));
        }

        for(AnnotatedDefinition ad : annoDefinitions){
            String name = (String)ad.getDefinition("name");
            String request = (String)ad.getDefinition("request");
            String condition = (String)ad.getDefinition("conditionClass");
            String repeatFromStep = (String)ad.getDefinition("repeatFromStep");
            RepeatDetails details = new RepeatDetails();

            try {
                details.setConditionClass(Class.forName(condition));
                details.setRepeatFromStep(repeatFromStep);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            StepDefinitionHolder holder = new StepDefinitionHolder(name, ad.getAnnotatedClass());
            holder.addRepeatDetails(request, details);

            defs.add(holder);
        }

        return defs;
    }
}
