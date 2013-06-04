package com.step.core.factory.impl;

@SuppressWarnings("rawtypes")
public class ConstructorArgs {
	private Class[] args;
	
	public ConstructorArgs(Class[] args){
		this.args = args;
	}
	
	public Class[] getArgs(){
		return this.args;
	}
}
