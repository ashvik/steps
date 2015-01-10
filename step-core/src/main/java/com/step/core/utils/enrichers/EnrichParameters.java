package com.step.core.utils.enrichers;

import com.step.core.collector.StepDefinitionHolder;
import com.step.core.context.StepExecutionContext;
import com.step.core.enums.ParameterValueType;
import com.step.core.parameter.RequestParameterValues;
import com.step.core.utils.ParameterAnnotatedField;
import com.step.core.utils.StepEnricher;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by amishra on 1/9/15.
 */
public class EnrichParameters implements StepEnricher {
    @Override
    public void enrichStep(Object step, StepDefinitionHolder definitionHolder, StepExecutionContext stepExecutionContext) {
        List<ParameterAnnotatedField> parameters = definitionHolder.getParameterAnnotatedFields();

        for(ParameterAnnotatedField field : parameters){
            String parameterName = field.getName() == null || field.getName().isEmpty() ? field.getFieldClass().getSimpleName() : field.getName();
            ParameterValueType parameterValueType = field.getParameterValueType();
            Object value = null;
            try{
                RequestParameterValues requestParameterValues =  stepExecutionContext.getRequestParameterContainer().getRequestParameterValues(parameterName);

                switch(parameterValueType){
                    case STRING:
                        value = requestParameterValues.getSingleValueAsString();
                        break;
                    case BOOLEAN:
                        value = requestParameterValues.getSingleValueAsBoolean();
                        break;
                    case INTEGER:
                        value = requestParameterValues.getSingleValueAsInt();
                        break;
                    case OBJECT:
                        value = requestParameterValues.getValueAsObjects(stepExecutionContext.getClassLoader()).get(0);
                        break;
                    case COLLECTION_OF_STRINGS:
                        value = requestParameterValues.getValues();
                        break;
                    case COLLECTION_OF_OBJECTS:
                        value = requestParameterValues.getValueAsObjects(stepExecutionContext.getClassLoader());
                }

                Field f = step.getClass().getDeclaredField(field.getFieldName());
                f.setAccessible(true);
                f.set(step, value);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
