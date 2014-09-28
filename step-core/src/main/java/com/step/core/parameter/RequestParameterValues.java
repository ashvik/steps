package com.step.core.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 9/27/14.
 */
public class RequestParameterValues {
    private List<String> values = new ArrayList<String>();

    public void addValue(String value){
        value = value.trim();
        this.values.add(value);
    }

    public List<String> getValues(){
        return this.values;
    }

    public int getSingleValueAsInt(){
        String value = values.get(0);
        return Integer.parseInt(value);
    }

    public String getSingleValueAsString(){
        return values.get(0);
    }

    public boolean getSingleValueAsBoolean(){
        return Boolean.parseBoolean(values.get(0));
    }

    public <T> List<T> getValueAsObjects(){
        List<T> objectValues = new ArrayList<T>();

        for(String val : values){
            try {
                Class<T> c = (Class<T>)Class.forName(val);
                objectValues.add(c.newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return objectValues;
    }

}
