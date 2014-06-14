package com.step.core.utils;

import com.step.core.context.StepExecutionContext;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/18/13
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class StepExecutionUtil {
    public static void makeRichStepObject(Object stepObject, List<AnnotatedField> fields, StepExecutionContext context){
        for(AnnotatedField field : fields){
            String name = field.getAnnotatedName();
            Object dependency = null;

            if(name != null && !name.isEmpty()){
                dependency = context.getDependency(name);

            }else{
                dependency = context.getDependency(field.getFieldClass());
            }

            try{
                Field f =stepObject.getClass().getDeclaredField(field.getFieldName());
                f.setAccessible(true);
                f.set(stepObject, dependency);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
