package com.step.core.annotations.builder.impl;

import com.step.core.annotations.InterceptorQualifier;
import com.step.core.enums.InterceptorType;
import com.step.core.utils.AnnotatedDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class InterceptorQualifierAnnotationDefinitionBuilder extends AbstractAnnotationDefinitionBuilder{
    @Override
    public Set<AnnotatedDefinition> build(Class... classes) {
        Set<AnnotatedDefinition> definitions = new HashSet<AnnotatedDefinition>();

        for(Class cls : classes){
            try{
                InterceptorQualifier ac = (InterceptorQualifier) cls.getAnnotation(InterceptorQualifier.class);
                if(ac != null){
                    AnnotatedDefinition definition = new AnnotatedDefinition(cls);
                    InterceptorType type = ac.type();

                    definition.addDefinition("type", type);
                    definitions.add(definition);
                }
            }catch(Exception e){
            }
        }
        return definitions;
    }
}
