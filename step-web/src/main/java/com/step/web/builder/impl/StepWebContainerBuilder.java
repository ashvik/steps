package com.step.web.builder.impl;

import com.step.core.BasicConfiguration;
import com.step.core.Configuration;
import com.step.core.container.StepContainer;
import com.step.core.container.impl.DefaultStepContainer;
import com.step.core.factory.ObjectFactory;
import com.step.web.StepWebContainer;
import com.step.web.builder.StepContainerBuilder;
import com.step.web.factory.ObjectFactoryInitializer;

import javax.servlet.ServletContext;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 6/23/13
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class StepWebContainerBuilder implements StepContainerBuilder<ServletContext> {
    private ServletContext servletContext;
    private Configuration configuration = new BasicConfiguration();
    private ObjectFactory objectFactory;
    private Builder builder = new Builder();

    @Override
    public StepContainer build(ServletContext servletContext) {
        this.servletContext = servletContext;
        return builder.buildSteps().buildConfig().buildObjectFactory().build();
    }

    private class Builder{
        StepContainer build(){
            StepContainer container = new StepWebContainer();
            container.setConfiguration(configuration);
            container.setObjectFactory(objectFactory);
            container.init();

            return container;
        }

        Builder buildSteps(){
            String[] packages = servletContext.getInitParameter("stepPackages").split("//,");
            configuration.setStepPackages(packages);
            return this;
        }

        Builder buildConfig(){
            String[] config = servletContext.getInitParameter("config").split("//,");
            configuration.setStepConfigurationFiles(config);
            return this;
        }

        Builder buildObjectFactory(){
            try{
                Class initClass = Class.forName(servletContext.getInitParameter("objectFactoryInit"));
                ObjectFactoryInitializer<ServletContext> init = (ObjectFactoryInitializer)initClass.newInstance();
                objectFactory = init.initObjectFactory(servletContext);
            }catch(Exception e){
                e.printStackTrace();
            }

            return this;
        }
    }
}
