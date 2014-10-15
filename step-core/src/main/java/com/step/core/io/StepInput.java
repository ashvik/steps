package com.step.core.io;

import com.step.core.Attributes;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/21/13
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepInput {
    private String request;
    private List<Object> collectedInputs = new ArrayList<Object>();
    private List<Class<?>> collectedInputClass = new ArrayList<Class<?>>();
    private Map<Class<?>, Collection<?>> collectedCollections = new HashMap<Class<?>, Collection<?>>();
    private Attributes attributes = new Attributes();

    public StepInput(String request, Object input){
        this.collectedInputs.add(input);
        this.request = request;
        this.collectedInputClass.add(input.getClass());
    }

    public StepInput(String request, Attributes attributes){
        this.request = request;
        this.attributes = attributes;
    }

    public StepInput(String request){
        this.request = request;
    }

    public StepInput(Object input){
        this.collectedInputs.add(input);
    }

    public void setInput(Object input){
        if(input != null && input instanceof Collection){
            if(!((Collection) input).isEmpty()){
                setCollectionTypeInput((Collection)input);
            }
        }else if(input != null && !this.collectedInputClass.contains(input.getClass())){
            this.collectedInputs.add(input);
            this.collectedInputClass.add(input.getClass());
        }
    }

    public <T> T getInput(Class<T> inputType){
        for(Object input : collectedInputs){
            if(inputType.isAssignableFrom(input.getClass())){
                return (T)input;
            }
        }

        throw new IllegalStateException("No input found for type: "+inputType.getName());
    }

    public <T extends Collection> void setCollectionTypeInput(Collection input){
        Object obj = input.iterator().next();
        if(!collectedCollections.containsKey(obj.getClass())){
            collectedCollections.put(obj.getClass(), input);
        }
    }

    public <T extends List> T getListTypeInput(Class type){
        return getCollectionType(type, Collections.EMPTY_LIST);
    }

    public <T extends Set> T getSetTypeInput(Class type){
        return getCollectionType(type, Collections.EMPTY_SET);
    }

    private <T extends Collection> T getCollectionType(Class type, Collection emptyType){
        Class keyType = null;
        for(Class key : collectedCollections.keySet()){
            if(type.isAssignableFrom(key)){
                keyType = key;
            }
        }
        return (T)collectedCollections.get(keyType == null ? type : keyType) == null ? (T)emptyType : (T)collectedCollections.get(keyType == null ? type : keyType);
    }

    public String getRequest(){
        return this.request;
    }

    public Object getAttribute(String name){
        return attributes.getAttribute(name);
    }

    public void fromExternalInput(StepInput external){
        this.collectedInputClass.addAll(external.collectedInputClass);
        this.collectedInputs.addAll(external.collectedInputs);
        this.collectedCollections.putAll(external.collectedCollections);
    }
}
