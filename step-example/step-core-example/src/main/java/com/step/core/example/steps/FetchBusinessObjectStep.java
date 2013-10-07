package com.step.core.example.steps;

import com.step.core.AbstractResponsiveStep;
import com.step.core.annotations.StepDefinition;
import com.step.core.annotations.StepDependency;
import com.step.core.example.entity.BusinessObject;
import com.step.core.example.services.DBService;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/6/13
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */

@StepDefinition
public class FetchBusinessObjectStep extends AbstractResponsiveStep<BusinessObject> {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @StepDependency
    private DBService dbService;

    @Override
    public BusinessObject execute() {
        BusinessObject bo = getInput(BusinessObject.class);
        bo = (BusinessObject)dbService.fetch(bo.getId());
        log.info("Fetched object ["+bo+"]");
        return bo;
    }
}
