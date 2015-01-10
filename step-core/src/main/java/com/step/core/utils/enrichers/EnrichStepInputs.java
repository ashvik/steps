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
public class EnrichStepInputs implements StepEnricher {
    @Override
    public void enrichStep(Object step, StepDefinitionHolder definitionHolder, StepExecutionContext stepExecutionContext) {
        List<AnnotatedField> inputs = definitionHolder.getAnnotatedInputs();

        for(AnnotatedField field : inputs){
            Class<?> inputClass = field.getFieldClass();
            try{
                Object input = stepExecutionContext.getInput(inputClass);
                Field f = step.getClass().getDeclaredField(field.getFieldName());
                f.setAccessible(true);
                f.set(step, input);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
