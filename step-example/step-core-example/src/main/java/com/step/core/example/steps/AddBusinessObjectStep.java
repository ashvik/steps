package com.step.core.example.steps;

import com.step.core.PluginGateway;
import com.step.core.annotations.*;
import com.step.core.enums.ParameterValueType;
import com.step.core.example.entity.BusinessObject;
import com.step.core.example.services.DBService;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/6/13
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */

@StepDefinition
public class AddBusinessObjectStep {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @ExternalDependency
    private DBService dbService;

    @Input
    private BusinessObject bo;

    @InputAsList(listOf = IParameter.class)
    private List<IParameter> parameters;

    @InputAsSet(setOf = String.class)
    private Set<String> strings;

    @com.step.core.annotations.Parameter(valueType = ParameterValueType.OBJECT)
    private IParameter param;

    @Plugin(request = "fetch")
    private PluginGateway pluginGateway;

    //@Override
    public BusinessObject execute() throws Exception {
        //BusinessObject bo = getInput(BusinessObject.class);
        //throw new RuntimeException("Some Exception");
        dbService.save(bo);
        log.info("Saved object ["+bo+"]");
        if(bo == null){
            throw new IOException("");
        }

        return bo;
    }
}
