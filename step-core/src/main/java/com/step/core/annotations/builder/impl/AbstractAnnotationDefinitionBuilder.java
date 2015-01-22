package com.step.core.annotations.builder.impl;

import com.step.core.annotations.*;
import com.step.core.annotations.builder.AnnotationDefinitionBuilder;
import com.step.core.utils.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 6/14/14.
 */
public abstract class AbstractAnnotationDefinitionBuilder implements AnnotationDefinitionBuilder {
    protected String makeDefaultStepName(Class stepClass){
        String clsName = stepClass.getName();
        String defaultName = clsName.substring(clsName.lastIndexOf('.')+1, clsName.length());
        char[] chars = defaultName.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);

        return new String(chars, 0, chars.length);
    }

    protected List<AnnotatedField> collectAnnotatedFields(Class<?> root){
        List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
        Field[] fields = root.getDeclaredFields();

        for(Field field : fields){
            ExternalDependency sd = field.getAnnotation(ExternalDependency.class);
            if(sd != null){
                annotatedFields.add(new AnnotatedField(field.getType(), field.getName(), sd.name()));
            }
        }

        return annotatedFields;
    }

    protected List<AnnotatedField> collectPluginAnnotatedFields(Class<?> root){
        List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
        Field[] fields = root.getDeclaredFields();

        for(Field field : fields){
            Plugin plugin = field.getAnnotation(Plugin.class);

            if(plugin != null){
                String request = plugin.request();
                boolean applyGenericSteps = plugin.applyGenericSteps();
                boolean passCurrentInputs = plugin.passCurrentInputs();
                PluginAnnotatedField pluginAnnotatedField = new PluginAnnotatedField(field.getType(), field.getName(), null);
                pluginAnnotatedField.setRequest(request);
                pluginAnnotatedField.setApplyGenericSteps(applyGenericSteps);
                pluginAnnotatedField.setPassCurrentInputs(passCurrentInputs);
                annotatedFields.add(pluginAnnotatedField);
            }
        }

        return annotatedFields;
    }

    protected List<AnnotatedField> collectInputAnnotatedFields(Class<?> root){
        List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
        Field[] fields = root.getDeclaredFields();

        for(Field field : fields){
            Input input = field.getAnnotation(Input.class);
            if(input != null){
                annotatedFields.add(new AnnotatedField(field.getType(), field.getName(), null));
            }
        }

        return annotatedFields;
    }

    protected List<AnnotatedField> collectInputAsListAnnotatedFields(Class<?> root){
        List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
        Field[] fields = root.getDeclaredFields();

        for(Field field : fields){
            InputAsList inputAsList = field.getAnnotation(InputAsList.class);
            if(inputAsList != null){
                InputAsListAnnotatedField annotatedField = new InputAsListAnnotatedField(field.getType(), field.getName(), null);
                annotatedField.setListOf(inputAsList.listOf());
                annotatedFields.add(annotatedField);
            }
        }

        return annotatedFields;
    }

    protected List<AnnotatedField> collectInputAsSetAnnotatedFields(Class<?> root){
        List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
        Field[] fields = root.getDeclaredFields();

        for(Field field : fields){
            InputAsSet inputAsSet = field.getAnnotation(InputAsSet.class);
            if(inputAsSet != null){
                InputAsSetAnnotatedField annotatedField = new InputAsSetAnnotatedField(field.getType(), field.getName(), null);
                annotatedField.setSetOf(inputAsSet.setOf());
                annotatedFields.add(annotatedField);
            }
        }

        return annotatedFields;
    }

    protected List<AnnotatedField> collectParameterAnnotatedFields(Class<?> root){
        List<AnnotatedField> annotatedFields = new ArrayList<AnnotatedField>();
        Field[] fields = root.getDeclaredFields();

        for(Field field : fields){
            Parameter parameter = field.getAnnotation(Parameter.class);
            if(parameter != null){
                ParameterAnnotatedField annotatedField = new ParameterAnnotatedField(field.getType(), field.getName(), null);
                annotatedField.setName(parameter.name());
                annotatedField.setParameterValueType(parameter.valueType());
                annotatedFields.add(annotatedField);
            }
        }

        return annotatedFields;
    }
}
