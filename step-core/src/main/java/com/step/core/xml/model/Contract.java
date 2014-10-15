package com.step.core.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amishra on 10/10/14.
 */
public class Contract {
    private List<String> inputTypes = new ArrayList<String>();
    private String expectedOutCome;

    public void addInputType(String inputType){
        this.inputTypes.add(inputType);
    }

    public List<String> getInputTypes(){
        return inputTypes;
    }

    public String getExpectedOutCome() {
        return expectedOutCome;
    }

    public void setExpectedOutCome(String expectedOutCome) {
        this.expectedOutCome = expectedOutCome.isEmpty() ? null : expectedOutCome;
    }
}
