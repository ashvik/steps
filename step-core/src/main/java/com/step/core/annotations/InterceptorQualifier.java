package com.step.core.annotations;

import com.step.core.enums.InterceptorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/29/13
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface InterceptorQualifier {
    InterceptorType type();
}
