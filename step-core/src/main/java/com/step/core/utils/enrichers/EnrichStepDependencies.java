package com.step.core.utils.enrichers;

import com.step.core.collector.StepDefinitionHolder;
import com.step.core.context.StepExecutionContext;
import com.step.core.utils.AnnotatedField;
import com.step.core.utils.StepEnricher;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by amishra on 1/9/15.
 */
public class EnrichStepDependencies implements StepEnricher {
    @Override
    public void enrichStep(Object step, StepDefinitionHolder definitionHolder, StepExecutionContext stepExecutionContext) {
        List<AnnotatedField> stepDependencies = definitionHolder.getAnnotatedFields();

        for(AnnotatedField field : stepDependencies){
            String name = field.getAnnotatedName();
            Object dependency = null;

            if(name != null && !name.isEmpty()){
                dependency = stepExecutionContext.getDependency(name);

            }else{
                dependency = stepExecutionContext.getDependency(field.getFieldClass());
            }

            try{
                Field f = step.getClass().getDeclaredField(field.getFieldName());
                f.setAccessible(true);
                f.set(step, dependency);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
