package com.step.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/23/13
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Attributes {
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public void addAttribute(String name, Object value){
        this.attributes.put(name, value);
    }

    public Object getAttribute(String name){
       return this.attributes;
    }

    public Set<String> getAttributeName(){
        return this.attributes.keySet();
    }
}
