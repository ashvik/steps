package com.step.core.annotations.builder.impl;

import com.step.core.annotations.Plugin;
import com.step.core.annotations.StepDependency;
import com.step.core.annotations.builder.AnnotationDefinitionBuilder;
import com.step.core.utils.AnnotatedField;

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
            StepDependency sd = field.getAnnotation(StepDependency.class);
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
                annotatedFields.add(new AnnotatedField(field.getType(), field.getName(), plugin.request()));
            }
        }

        return annotatedFields;
    }
}
