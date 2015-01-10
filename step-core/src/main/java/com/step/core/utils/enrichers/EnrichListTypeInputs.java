package com.step.core.utils.enrichers;

import com.step.core.collector.StepDefinitionHolder;
import com.step.core.context.StepExecutionContext;
import com.step.core.utils.InputAsListAnnotatedField;
import com.step.core.utils.StepEnricher;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by amishra on 1/9/15.
 */
public class EnrichListTypeInputs implements StepEnricher {
    @Override
    public void enrichStep(Object step, StepDefinitionHolder definitionHolder, StepExecutionContext stepExecutionContext) {
        List<InputAsListAnnotatedField> inputsList = definitionHolder.getInputAsListAnnotatedFields();

        for(InputAsListAnnotatedField field : inputsList){
            Class<?> inputClass = field.getListOf();
            try{
                Object input = stepExecutionContext.getStepInput().getListTypeInput(inputClass);
                Field f = step.getClass().getDeclaredField(field.getFieldName());
                f.setAccessible(true);
                f.set(step, input);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
