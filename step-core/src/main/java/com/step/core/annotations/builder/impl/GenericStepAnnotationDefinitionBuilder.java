package com.step.core.annotations.builder.impl;

import com.step.core.annotations.GenericStepDefinition;
import com.step.core.enums.GenericStepType;
import com.step.core.utils.AnnotatedDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class GenericStepAnnotationDefinitionBuilder extends AbstractAnnotationDefinitionBuilder{
    @Override
    public Set<AnnotatedDefinition> build(Class... classes) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();

        for(Class cls : classes){
            try{
                GenericStepDefinition ac = (GenericStepDefinition) cls.getAnnotation(GenericStepDefinition.class);
                if(ac != null){
                    AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                    String name = ac.name();
                    GenericStepType type = ac.stepType();
                    int priority = ac.stepPriority();

                    definition.addDefinition("name", name);
                    definition.addDefinition("type", type);
                    definition.addDefinition("priority", priority);
                    definition.addDefinition("dependencies", collectAnnotatedFields(cls));
                    definition.addDefinition("plugins", collectPluginAnnotatedFields(cls));
                    definition.addDefinition("inputs", collectInputAnnotatedFields(cls));
                    definition.addDefinition("inputsAsList", collectInputAsListAnnotatedFields(cls));
                    definition.addDefinition("inputsAsSet", collectInputAsSetAnnotatedFields(cls));
                    definition.addDefinition("parameters", collectParameterAnnotatedFields(cls));

                    definitions.add(definition);
                }
            }catch(Exception e){
            }
        }
        return definitions;
    }
}
