package com.step.core.annotations.builder.impl;

import com.step.core.annotations.StepRepeater;
import com.step.core.utils.AnnotatedDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class StepRepeaterAnnotationDefinitionBuilder extends AbstractAnnotationDefinitionBuilder{
    @Override
    public Set<AnnotatedDefinition> build(Class... classes) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();

        for(Class cls : classes){
            try{
                StepRepeater ac = (StepRepeater) cls.getAnnotation(StepRepeater.class);
                if(ac != null){
                    AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                    String conditionClass = ac.conditionClass();
                    String repeatFromStep = ac.repeatFromStep();
                    String request = ac.forRequest();

                    definition.addDefinition("name", makeDefaultStepName(cls));
                    definition.addDefinition("request", request);
                    definition.addDefinition("conditionClass", conditionClass);
                    definition.addDefinition("repeatFromStep", repeatFromStep);
                    definitions.add(definition);
                }
            }catch(Exception e){
            }
        }

        return definitions;
    }
}
