package com.step.core.annotations.collector;

import com.step.core.annotations.StepDependency;
import com.step.core.utils.AnnotatedDefinitionCollector;
import com.step.core.utils.AnnotatedField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/18/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStepAnnotationDefinitionCollector implements AnnotatedDefinitionCollector {
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

    protected String makeDefaultStepName(Class stepClass){
        String clsName = stepClass.getClass().getName();
        String defaultName = clsName.substring(clsName.lastIndexOf('.')+1, clsName.length());
        char[] chars = defaultName.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);

        return new String(chars, 0, chars.length);
    }
}
