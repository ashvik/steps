package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.GenericStepDefinition;
import com.step.core.annotations.StepDependency;
import com.step.core.enums.GenericStepType;
import com.step.core.example.services.DBService;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/5/13
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */

@GenericStepDefinition(name="connectDBServiceStep", stepType = GenericStepType.PRE_STEP)
public class ConnectDBServiceStep extends AbstractResponseLessStep {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @StepDependency
    private DBService dbService;

    @Override
    public void execute() {
        log.info("Connecting to DBService");
        dbService.connect();
    }
}
