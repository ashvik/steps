package com.step.informer;

import com.step.core.Configuration;
import com.step.core.repository.BasicStepRepository;
import com.step.informer.service.StepInformationService;
import com.step.informer.service.impl.BasicStepInformationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by amishra on 12/13/14.
 */
public abstract class DocumentationGenerator {
    private final static Log logger = LogFactory.getLog(DocumentationGenerator.class);

    public static void generateDocumentation(Configuration configuration, String info){
        StepInformationService stepInformationService = new BasicStepInformationService();
        BasicStepRepository basicStepRepository = new BasicStepRepository();

        basicStepRepository.setConfiguration(configuration);
        basicStepRepository.buildRepository();
        stepInformationService.setStepRepository(basicStepRepository);

        stepInformationService.generateDocumentation(info,configuration);
        logger.info("Documentation generated inside folder docs!");
    }
}
