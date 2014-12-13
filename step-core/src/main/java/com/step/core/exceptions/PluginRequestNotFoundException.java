package com.step.core.exceptions;

/**
 * Created by amishra on 12/13/14.
 */
public class PluginRequestNotFoundException extends RuntimeException{
    public PluginRequestNotFoundException(String message){
        super(message);
    }
}
