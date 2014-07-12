package com.step.core.example.steps;

import com.step.core.AbstractResponsiveStep;
import com.step.core.annotations.StepDefinition;
import com.step.core.annotations.StepDependency;
import com.step.core.example.entity.BusinessObject;
import com.step.core.example.services.DBService;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/6/13
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */

@StepDefinition
public class AddBusinessObjectStep extends AbstractResponsiveStep<BusinessObject> {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @StepDependency
    private DBService dbService;

    @Override
    public BusinessObject execute() throws Exception {
        getStepExecutionContext().applyPluginRequest("nestedReq", new Object());
        BusinessObject bo = getInput(BusinessObject.class);
        BusinessObject co = null;
        dbService.save(bo);
        log.info("Saved object ["+bo+"]");
        if(bo == null){
            throw new IOException("");
        }//if(co == null){
           // throw new NoSuchMethodException("");
        //}
        return bo;
    }
}
