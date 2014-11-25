package com.step.core;

import com.step.core.container.StepExecutionContainer;
import com.step.core.container.impl.DefaultStepExecutionContainer;
import com.step.core.factory.ObjectFactory;
import com.step.core.repository.BasicStepRepository;

/**
 * Created by amishra on 11/17/14.
 */
public enum StepExecutionContainerProvider {
    INSTANCE;

    private StepExecutionContainer stepExecutionContainer;

    public void init(Configuration configuration){
        stepExecutionContainer = new DefaultStepExecutionContainer();
        BasicStepRepository basicStepRepository = new BasicStepRepository();
        basicStepRepository.setConfiguration(configuration);
        basicStepRepository.buildRepository();

        stepExecutionContainer.setConfiguration(configuration);
        stepExecutionContainer.setStepRepository(basicStepRepository);
        stepExecutionContainer.init();
    }

    public void init(Configuration configuration, ObjectFactory objectFactory){
        init(configuration);
        if(objectFactory != null){
            stepExecutionContainer.setObjectFactory(objectFactory);
        }
    }

    public StepExecutionContainer getStepExecutionContainer(){
        if(stepExecutionContainer == null){
            throw new IllegalStateException("StepExecutionContainer has not initialized.");
        }
        return stepExecutionContainer;
    }
}
