package com.step.core.example.entity;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/5/13
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class BusinessObject {
    private int id;
    private String name;
    private String value;
    private String description;

    public BusinessObject(int id, String name, String value, String description){
        this.id = id;
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public void setState(BusinessObject external){
        this.name = external.getName();
        this.value = external.getValue();
        this.description = external.getDescription();
    }

    public String toString(){
        return "ID: "+id+" | Name: "+name+" | Value: "+value+" | Description: "+description;
    }
}
