package com.step.core.utils;

import com.step.core.annotations.builder.AnnotationDefinitionBuilder;

import java.util.Set;

public interface AnnotatedDefinitionCollector {
	Set<AnnotatedDefinition> collect(String pack, AnnotationDefinitionBuilder builder);
}
