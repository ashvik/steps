package com.step.core.annotations.builder;

import com.step.core.utils.AnnotatedDefinition;

import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public interface AnnotationDefinitionBuilder {
    Set<AnnotatedDefinition> build(Class... classes);
}
