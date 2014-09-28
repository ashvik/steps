package com.step.core.exceptions;

/**
 * Created by amishra on 9/27/14.
 */
public class RequestParameterNotFoundException extends RuntimeException{
    public RequestParameterNotFoundException(String message){
        super(message);
    }
}
