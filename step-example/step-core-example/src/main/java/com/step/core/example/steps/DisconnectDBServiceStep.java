package com.step.core.example.steps;

import com.step.core.AbstractResponseLessStep;
import com.step.core.annotations.ExternalDependency;
import com.step.core.annotations.GenericStepDefinition;
import com.step.core.enums.GenericStepType;
import com.step.core.example.services.DBService;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 10/5/13
 * Time: 9:13 PM
 * To change this template use File | Settings | File Templates.
 */

@GenericStepDefinition(name="disConnectDBServiceStep", stepType = GenericStepType.POST_STEP)
public class DisconnectDBServiceStep extends AbstractResponseLessStep {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @ExternalDependency
    private DBService dbService;

    @Override
    public void execute() {
        log.info("Disconnecting to DBService");
        dbService.disconnect();
    }
}
