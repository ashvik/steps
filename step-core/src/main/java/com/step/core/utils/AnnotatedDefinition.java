package com.step.core.utils;

import java.util.HashMap;
import java.util.Map;

public class AnnotatedDefinition {
	private Class<?> annotatedClass;
	private Map<String, Object> definitionMap = new HashMap<String, Object>();
	
	public AnnotatedDefinition(Class<?> annotatedClass){
		this.annotatedClass = annotatedClass;
	}
	
	public void addDefinition(String name, Object val){
		this.definitionMap.put(name, val);
	}
	
	public Object getDefinition(String name){
		return this.definitionMap.get(name);
	}
	
	public Class<?> getAnnotatedClass(){
		return this.annotatedClass;
	}
}
