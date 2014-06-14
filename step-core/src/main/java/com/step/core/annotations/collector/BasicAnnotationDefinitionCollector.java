package com.step.core.annotations.collector;

import com.step.core.annotations.builder.AnnotationDefinitionBuilder;
import com.step.core.utils.AnnotatedDefinition;

import java.util.Collections;
import java.util.Set;

/**
 * Created by amishra on 6/14/14.
 */
public class BasicAnnotationDefinitionCollector extends AbstractStepAnnotationDefinitionCollector{
    @Override
    public Set<AnnotatedDefinition> collect(String pack, AnnotationDefinitionBuilder builder) {
        Class[] bds = findClassesInPackage(pack);

        if(bds != null){
            return builder.build(bds);
        }

        return Collections.EMPTY_SET;
    }
}
