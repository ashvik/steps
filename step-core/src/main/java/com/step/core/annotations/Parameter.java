package com.step.core.annotations;

import com.step.core.enums.ParameterValueType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by amishra on 1/9/15.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Parameter {
 String name() default "";
 ParameterValueType valueType() default ParameterValueType.STRING;
}
