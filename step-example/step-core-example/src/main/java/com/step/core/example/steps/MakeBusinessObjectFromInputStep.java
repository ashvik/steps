package com.step.core.example.steps;

import com.step.core.AbstractResponsiveStep;
import com.step.core.annotations.StepDefinition;
import com.step.core.example.entity.BusinessObject;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/6/13
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */

@StepDefinition
public class MakeBusinessObjectFromInputStep extends AbstractResponsiveStep<BusinessObject> {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public BusinessObject execute() {
        Map<String, String> in = getInput(Map.class);
        BusinessObject bo = new BusinessObject(Integer.parseInt(in.get("id")),
                in.get("name"), in.get("value"), in.get("description"));

        log.info("Made object from input["+bo+"]");
        return bo;
    }
}
